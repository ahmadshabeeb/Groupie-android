package app.kth.com.groupie.parent;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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
import com.google.firebase.database.FirebaseDatabase;
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
import app.kth.com.groupie.data.recycleViewData.RecyclerHeader;
import app.kth.com.groupie.data.recycleViewData.RecyclerListItem;
import app.kth.com.groupie.groupMessaging.GroupMessagingActivity;
import app.kth.com.groupie.utilities.Utility;

public class GroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<RecyclerListItem> groupArrayList = new ArrayList<>();
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_GROUP = 1;

    private long[] daysInUNIX = new long[7];
    private int[] daysReference = new int[7];
    private boolean[] headers = new boolean[7];
    private final Long DAY_IN_SECONDS = 86400l;
    private Context context;
    private final int NUM_GROUPS_TO_LOAD = 100;
    private final DatabaseReference databaseReference;
    private BrowserFragment.FilterChoice filterChoice;
    private Resources resources;

    public GroupAdapter(Context context, BrowserFragment.FilterChoice filterChoice) {
        this.context = context;
        resources = context.getResources();
        calculateDaysInUNIX();
        this.filterChoice = filterChoice;
        databaseReference = FirebaseDatabase.getInstance().getReference().child("groups");
        setGroups(databaseReference);
    }

    private void setGroups(final DatabaseReference databaseReference) {
        Query nearestGroupMeetingQuery = databaseReference.orderByChild("meetingDateTimeStamp").limitToLast(NUM_GROUPS_TO_LOAD);

        nearestGroupMeetingQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Group group = dataSnapshot.getValue(Group.class);

                if (group.getIsPublic()) {
                    if (filterChoice.isChosenSubject(group.getSubject())) {
                        group.setGroupId(dataSnapshot.getKey());
                        addGroupToDataSet(group);
                    }
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
            groupArrayList.add(daysReference[0],group);
            updateReferences(1,6);
            if(!headers[0]){
                addHeader("Today",  daysReference[0]);
                headers[0] = true;
                updateReferences(0,6);
            }
            Log.d("TAG" , "added to Day1" + " ..." );

        }else if(groupMeetingDay == daysInUNIX[1]){
            groupArrayList.add(daysReference[1],group);
            updateReferences(2,6);
            if(!headers[1]){
                addHeader("Tomorrow", daysReference[1]);
                headers[1] = true;
                updateReferences(1,6);
            }
            Log.d("TAG" , "added to Day2" + " ..." );

        }else if(groupMeetingDay == daysInUNIX[2]){
            groupArrayList.add(daysReference[2],group);
            updateReferences(3,6);
            if(!headers[2]){
                addHeader(getWeekDay(daysInUNIX[2]) , daysReference[2]);
                headers[2] = true;
                updateReferences(2,6);
            }
            Log.d("TAG" , "added to Day3" + " ..." );

        }else if(groupMeetingDay == daysInUNIX[3]){
            groupArrayList.add(daysReference[3],group);
            updateReferences(4,6);
            if(!headers[3]){
                addHeader(getWeekDay(daysInUNIX[3]), daysReference[3]);
                headers[3] = true;
                updateReferences(3,6);
            }
            Log.d("TAG" ,  "added to Day4"+ " ..." );

        }else if(groupMeetingDay == daysInUNIX[4]){
            groupArrayList.add(daysReference[4],group);
            updateReferences(5,6);
            if(!headers[4]){
                addHeader(getWeekDay(daysInUNIX[4]), daysReference[4]);
                headers[4] = true;
                updateReferences(4,6);
            }
            Log.d("TAG" , "added to Day5" + " ..." );

        }else if(groupMeetingDay == daysInUNIX[5]){
            groupArrayList.add(daysReference[5],group);
            updateReferences(6,6);
            if(!headers[5]){
                addHeader(getWeekDay(daysInUNIX[5]), daysReference[5]);
                headers[5] = true;
                updateReferences(5,6);
            }
            Log.d("TAG" , "added to Day6" + " ..." );

        }else if(groupMeetingDay == daysInUNIX[6]){
            groupArrayList.add(daysReference[6],group);
            if(!headers[6]){
                addHeader(getWeekDay(daysInUNIX[6]), daysReference[6]);
                headers[6] = true;
                updateReferences(6,6);
            }
            Log.d("TAG" , "added to Day7" + " ..." );

        }else {
            // do not add the group
            Log.d("TAG" , " NOT ADDED ..." );
        }

    }

    private void addHeader(String day, int position){
        RecyclerHeader header = new RecyclerHeader();
        header.setDay(day);
        groupArrayList.add(position, header);
    }

    private String getWeekDay(long day){
        Date date = new Date(day * 1000l);
        DateFormat sdf = new SimpleDateFormat("EEEE");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String weekDay = sdf.format(date);
        Log.d("Week day" , weekDay + "..." );
        return weekDay;
    }

    private void updateReferences(int start, int end){
        for(int i=start; i<=end; i++){
            daysReference[i] += 1;
            Log.d("TAG" , "updated ref " + i + "   value: " + daysReference[i] );
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
    private void calculateDaysInUNIX(){
        long todayUnix = getTodayUnix();
//        Log.d("TAG", todayUnix + " ...");
        daysInUNIX[0] = todayUnix;

        for(int i=1; i<daysInUNIX.length; i++){
            daysInUNIX[i] = daysInUNIX[i-1] + DAY_IN_SECONDS;
//            Log.d("TAG" , dayReference[i] + " ...");
        }
    }


    //-------------------BINDING DATA----------------------//

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

    public class HeaderViewHolder extends RecyclerView.ViewHolder{
        TextView header = (TextView) itemView.findViewById(R.id.header);

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (groupArrayList == null) {
            return 0;
        } else {
            return groupArrayList.get(position).isHeader() ? TYPE_HEADER : TYPE_GROUP;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        switch (viewType) {
            case TYPE_HEADER:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_group_header, parent, false);
                return new HeaderViewHolder(v);
            case TYPE_GROUP:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_group, parent, false);
                return new GroupViewHolder(v);
            default:
                throw new IllegalArgumentException("NO CASE FOR TYPE " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RecyclerListItem item = groupArrayList.get(position);

        if (item.isHeader()){
            RecyclerHeader header = (RecyclerHeader) item;
            ((HeaderViewHolder) holder).header.setText(header.getDay());

        }else {
            Group group = (Group) item;
            Log.d("TAG", "after ordering: " + group.getMeetingDateTimeStamp());

            setFields(group, (GroupViewHolder) holder);
            setSubjectImage(group, (GroupViewHolder) holder);
            setJoinGroupButton(group, (GroupViewHolder) holder);
        }
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
