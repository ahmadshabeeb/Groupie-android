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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import app.kth.com.groupie.R;

public class BrowserFragment extends Fragment {
    ParentActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInsatnceState){
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_browser, container, false);

        RecyclerView mRecycleView = (RecyclerView) rootView.findViewById(R.id.group_list_recycle);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        mRecycleView.setLayoutManager(mLayoutManager);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("groups");

        RecyclerView.Adapter mAdapter = new GroupAdapter(databaseReference, getActivity());

        mRecycleView.setAdapter(mAdapter);

        return rootView;
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
