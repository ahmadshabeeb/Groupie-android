package app.kth.com.groupie.parent;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.functions.FirebaseFunctionsException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import app.kth.com.groupie.R;
import app.kth.com.groupie.data.Group;
import app.kth.com.groupie.groupMessaging.GroupMessagingActivity;
import app.kth.com.groupie.utilities.Utility;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {
    private ArrayList<Group> groupArrayList = new ArrayList<>();
    private long[] daysInUNIX = new long[7];
    private int[] daysReference = new int[7];

    private final Long DAY_IN_SECONDS = 86400l;
    private Context context;
    private static int NUM_GROUPS_TO_LOAD = 10;

    public GroupAdapter(final DatabaseReference databaseReference, Context context) {
        this.context = context;
        calculateDaysInUNIX();
        setGroups(databaseReference);
    }

    private void calculateDaysInUNIX(){
        long todayUnix = getTodayUnix();
//        Log.d("TAG", todayUnix + " ...");
        daysInUNIX[0] = todayUnix;

        for(int i=1; i<daysInUNIX.length; i++){
            daysInUNIX[i] = daysInUNIX[i-1] + DAY_IN_SECONDS;
//            Log.d("TAG" , dayReference[i] + " ...");
        }
    }
    private long getTodayUnix(){
        Date today = new Date();
        DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String todayDate = sdf.format(today);
        long todayUnix = 0;

        try {
            todayUnix = sdf.parse(todayDate).getTime()/1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return todayUnix;
    }


    private void setGroups(final DatabaseReference databaseReference){

        Query nearestGroupMeetingQuery = databaseReference.orderByChild("meetingDateTimeStamp").limitToLast(NUM_GROUPS_TO_LOAD);

        nearestGroupMeetingQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Group group = dataSnapshot.getValue(Group.class);

                if (group.getIsPublic()) {
                    group.setGroupId(dataSnapshot.getKey());
                    addGroupToDataSet(group);
                    //groupArrayList.add(group);
                }
                notifyDataSetChanged();
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

    private void addGroupToDataSet(Group group){
        long groupMeetingDay = group.getMeetingDateTimeStamp();
        Log.d("TAG", "group meeting day:" + groupMeetingDay);

        if(groupMeetingDay == daysInUNIX[0]){
            groupArrayList.add(0,group);
            modifyReferences(1,6);
            Log.d("TAG" , "added to Day1" + " ..." );

        }else if(groupMeetingDay == daysInUNIX[1]){
            groupArrayList.add(daysReference[1],group);
            modifyReferences(2,6);
            Log.d("TAG" , "added to Day2" + " ..." );

        }else if(groupMeetingDay == daysInUNIX[2]){
            groupArrayList.add(daysReference[2],group);
            modifyReferences(3,6);
            Log.d("TAG" , "added to Day3" + " ..." );

        }else if(groupMeetingDay == daysInUNIX[3]){
            groupArrayList.add(daysReference[3],group);
            modifyReferences(4,6);
            Log.d("TAG" ,  "added to Day4"+ " ..." );

        }else if(groupMeetingDay == daysInUNIX[4]){
            groupArrayList.add(daysReference[4],group);
            modifyReferences(5,6);
            Log.d("TAG" , "added to Day5" + " ..." );

        }else if(groupMeetingDay == daysInUNIX[5]){
            groupArrayList.add(daysReference[5],group);
            modifyReferences(6,6);
            Log.d("TAG" , "added to Day6" + " ..." );

        }else if(groupMeetingDay == daysInUNIX[6]){
            groupArrayList.add(daysReference[6],group);
            Log.d("TAG" , "added to Day7" + " ..." );

        }else {
            // do not add the group
            Log.d("TAG" , " NOT ADDED ..." );
        }

    }

    private void modifyReferences(int start, int end){
        for(int i=start; i<=end; i++){
            daysReference[i] += 1;
            Log.d("TAG" , "updated ref " + i + "   value: " + daysReference[i] );
        }
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout parent = (RelativeLayout) itemView.findViewById(R.id.group_card_view);
        ImageView subjectImage = (ImageView) itemView.findViewById(R.id.subject_imageview);
        TextView subject = (TextView) itemView.findViewById(R.id.group_subject_textview);
        TextView topic = (TextView) itemView.findViewById(R.id.group_topic_textview);
        TextView location = (TextView) itemView.findViewById(R.id.group_location_textview);
        TextView description = (TextView) itemView.findViewById(R.id.group_description_textview);
        Button joinGroupBtn = (Button) itemView.findViewById(R.id.join_group_btn);

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
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_group, parent, false);
        return new GroupViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        final Group group = groupArrayList.get(position);

        Log.d("TAG", "after ordering: " + group.getMeetingDateTimeStamp());

        setFields(group, holder);
        setSubjectImage(group, holder);
        setJoinGroupButton(group, holder);
    }

    @Override
    public int getItemCount() {
        return groupArrayList.size();
    }

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
            case "Language" :
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

            case "Natural Science" :
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

    private void setJoinGroupButton (final Group group, GroupViewHolder holder) {
        holder.joinGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String groupId = group.getGroupId();

                Utility.callCloudFunctions("dbGroupsJoin", groupId)
                        .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                if (!task.isSuccessful()) {
                                    Exception e = task.getException();

                                    if (e instanceof FirebaseFunctionsException) {
                                        FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                                        FirebaseFunctionsException.Code code = ffe.getCode();
                                        //Object details = ffe.getDetails();
                                        String message = ffe.getMessage();
                                        Log.d("TAG", "EROR CODE: " + code + " ... " + message);
                                    }

                                    Log.w("TAG", "onFailure", e);
                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    return;
                                } else {
                                    String result = task.getResult();
                                    context.startActivity(new Intent(context , GroupMessagingActivity.class));
                                }
                            }
                        });
            }
        });
    }
}
