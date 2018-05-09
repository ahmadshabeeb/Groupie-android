package app.kth.com.groupie.parent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import app.kth.com.groupie.R;
import app.kth.com.groupie.data.Group;
import app.kth.com.groupie.groupMessaging.GroupMessagingActivity;
import app.kth.com.groupie.groupMessaging.PrepareGroupMessageActivity;


public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.GroupViewHolder> {
    private List<Group> groupArrayList;
    private Context context;
    private final int NUM_GROUPS_TO_LOAD = 100;
    private final DatabaseReference databaseReference;
    private RelativeLayout progressBar;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private TextView userErrorMessage;

    public HomeAdapter(Context context, RelativeLayout progressBar, TextView userErrorMessage) {
        this.context = context;
        this.progressBar = progressBar;
        this.userErrorMessage = userErrorMessage;

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("groups");
        groupArrayList = new ArrayList<>();
        getGroupsFromDatabase(databaseReference);
        runTimeOut();
    }

    //--------------DATASET-----------------------//
    private void getGroupsFromDatabase(final DatabaseReference databaseReference) {
        Query nearestGroupMeetingQuery = databaseReference.orderByChild("meetingDateTimeStamp").limitToLast(NUM_GROUPS_TO_LOAD);

        nearestGroupMeetingQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Group group = dataSnapshot.getValue(Group.class);

                //Add group to browser if it is public and matches user subject and date selection
                if (group.getIsPublic()) {
                    if (group.isHasMeetingDate() && isUserMember(group)) {
                        group.setGroupId(dataSnapshot.getKey());
                        groupArrayList.add(group);
                        notifyDataSetChanged();
                        stopLoadingProgressBar();
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void runTimeOut() {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                timer.cancel();
                ((Activity)context).runOnUiThread(new Runnable() {
                    public void run() {
                        if (groupArrayList.size() == 0) { //  Timeout
                            noGroupsToDisplay();
                        }
                    }
                });
            }
        };

        // Setting timeout of 10 sec to the request
        timer.schedule(timerTask, 5000L);
    }

    private void noGroupsToDisplay() {
        stopLoadingProgressBar();
        userErrorMessage.setVisibility(View.VISIBLE);
    }


    private void stopLoadingProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    private boolean isUserMember(Group group){
        boolean isMember = false;
        for (Map.Entry<String, Boolean> entry: group.getMembers().entrySet()) {
            if(currentUser != null && entry.getKey().equals(currentUser.getUid()))
                isMember = true;
        }
        return isMember;
    }

    /**
     *  THIS SECTION OF CODE IS WHERE GROUP DATA IS BOUND TO THE VIEWS
     */
    public class GroupViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout parent = (RelativeLayout) itemView.findViewById(R.id.group_card_view);
        ImageView subjectImage = (ImageView) itemView.findViewById(R.id.subject_imageview);
        TextView subject = (TextView) itemView.findViewById(R.id.group_subject_textview);
        TextView topic = (TextView) itemView.findViewById(R.id.group_topic_textview);
        TextView location = (TextView) itemView.findViewById(R.id.group_location_textview);
        TextView description = (TextView) itemView.findViewById(R.id.group_description_textview);

        // fixed textViews
        TextView topicTitle = (TextView) itemView.findViewById(R.id.topic_textview_fixed);
        TextView locationTitle = (TextView) itemView.findViewById(R.id.location_textview_fixed);
        TextView descriptionTitle = (TextView) itemView.findViewById(R.id.description_textview_fixed);

        public GroupViewHolder(View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public HomeAdapter.GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_group_home, parent, false);
                return new GroupViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        Group group = groupArrayList.get(position);
            setFields(group, holder);
            setSubjectImage(group, holder);
            setCardViewToBeClickable(group, holder);

    }

    @Override
    public int getItemCount() {
        return groupArrayList.size();
    }

    //-----------------HELPER METHODS--------------
    private void setFields(Group group, GroupViewHolder holder) {
        holder.subject.setText(group.getSubject());

        // Empty fields are hidden
        if (group.getTopic() == null) {
            holder.topic.setText("");
            holder.topicTitle.setVisibility(View.GONE);
        } else {
            holder.topic.setText(group.getTopic());
            holder.topicTitle.setVisibility(View.VISIBLE);
        }

        if (group.getLocation() == null) {
            holder.location.setText("");
            holder.locationTitle.setVisibility(View.GONE);
        } else {
            holder.location.setText(group.getLocation());
            holder.locationTitle.setVisibility(View.VISIBLE);
        }

        if (group.getDescription()== null) {
            holder.description.setText("");
            holder.descriptionTitle.setVisibility(View.GONE);
        } else {
            holder.description.setText(group.getDescription());
            holder.descriptionTitle.setVisibility(View.VISIBLE);
        }
    }

    private void setSubjectImage(Group group, GroupViewHolder holder) {
        // show right image based on the subject

        switch (group.getSubject()){
            case "Language":
                //replace by the right image
                holder.subjectImage.setBackgroundResource(R.drawable.language);
                break;

            case "Programming" :
                holder.subjectImage.setBackgroundResource(R.drawable.programming);
                break;

            case "Math" :
                holder.subjectImage.setBackgroundResource(R.drawable.math);
                break;

            case "Business and Economics" :
                holder.subjectImage.setBackgroundResource(R.drawable.business);
                break;

            case "Engineering" :
                holder.subjectImage.setBackgroundResource(R.drawable.engineering);
                break;

            case "Natural Sciences" :
                holder.subjectImage.setBackgroundResource(R.drawable.science);
                break;

            case "Law and Political Science" :
                holder.subjectImage.setBackgroundResource(R.drawable.law);
                break;

            case "Art and Music" :
                holder.subjectImage.setBackgroundResource(R.drawable.music);
                break;

            case "Other" :
                holder.subjectImage.setBackgroundResource(R.drawable.other);
                break;

            default :
                holder.subjectImage.setBackgroundResource(R.drawable.other);
                break;
        }
    }

    private void setCardViewToBeClickable(final Group group, GroupViewHolder holder){
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context , PrepareGroupMessageActivity.class);
                i.putExtra("group" , (Parcelable) group);
                context.startActivity(i);
            }
        });
    }
}
