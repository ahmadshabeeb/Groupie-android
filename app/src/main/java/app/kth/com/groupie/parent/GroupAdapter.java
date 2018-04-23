package app.kth.com.groupie.parent;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import app.kth.com.groupie.R;
import app.kth.com.groupie.data.Group;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {
    private ArrayList<Group> groupArrayList = new ArrayList<>();
    private Context context;

    public GroupAdapter(DatabaseReference databaseReference, Context context) {
        this.context = context;

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Group group = dataSnapshot.getValue(Group.class);
                groupArrayList.add(group);
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

    public class GroupViewHolder extends RecyclerView.ViewHolder {
        TextView subject = (TextView) itemView.findViewById(R.id.group_subject_textview);
        TextView topic = (TextView) itemView.findViewById(R.id.group_topic_textview);
        TextView location = (TextView) itemView.findViewById(R.id.group_location_textview);
        TextView description = (TextView) itemView.findViewById(R.id.group_description_textview);
        RelativeLayout parent = (RelativeLayout) itemView.findViewById(R.id.group_card_view);

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
        //on click shit
        Group group = groupArrayList.get(position);

        holder.subject.setText(group.getSubject());
        holder.topic.setText(group.getTopic());
        holder.location.setText(group.getLocation());
        holder.description.setText(group.getDescription());
    }

    @Override
    public int getItemCount() {
        return groupArrayList.size();
    }
}
