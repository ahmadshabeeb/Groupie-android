package app.kth.com.groupie.parent;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

import app.kth.com.groupie.R;
import app.kth.com.groupie.data.Group;

public class BrowserFragment extends Fragment {
    ParentActivity activity;
    private ArrayList<Group> groups;
    private RecyclerView.Adapter mAdapter;
    private static int NUM_GROUPS_TO_LOAD = 10;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInsatnceState){
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_browser, container, false);
        RecyclerView mRecycleView = (RecyclerView) rootView.findViewById(R.id.group_list_recycle);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecycleView.setLayoutManager(mLayoutManager);

        groups = new ArrayList<>();
        //assign global variable groups to listen to group database
        getGroups();

        mAdapter = new GroupAdapter(getActivity(), groups);
        mRecycleView.setAdapter(mAdapter);

        return rootView;
    }

    private void getGroups() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("groups");

        Query nearestGroupMeetingQuery = databaseReference.orderByChild("meetingDateTimeStamp").limitToLast(NUM_GROUPS_TO_LOAD);

        nearestGroupMeetingQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Group group = dataSnapshot.getValue(Group.class);

                if (group.getIsPublic()) {
                    group.setGroupId(dataSnapshot.getKey());
                    groups.add(group);
                    Log.d("TAG", "GROUP KEY: " + group.getGroupId());
                }
                mAdapter.notifyDataSetChanged();
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (ParentActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }
}
