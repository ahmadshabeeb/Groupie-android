package app.kth.com.groupie.parent;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

import app.kth.com.groupie.R;
import app.kth.com.groupie.data.Group;
import app.kth.com.groupie.utilities.Utility;

public class HomeFragment extends Fragment {
    ParentActivity activity;
    private RecyclerView mRecycleView;
    private RecyclerView.Adapter mAdapter;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipe;
    private ViewGroup rootView;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInsatnceState){
        this.context = getActivity();
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);
        mRecycleView = rootView.findViewById(R.id.group_list_recycle_home);
        progressBar = rootView.findViewById(R.id.progressBar);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mRecycleView.setLayoutManager(mLayoutManager);
        initializeAdapter();

        getRecommendedGroup();

        refreshPulldown(rootView);

        return rootView;
    }

    private void initializeAdapter() {
        mAdapter = new HomeAdapter(getActivity(), progressBar);
        mRecycleView.setAdapter(mAdapter);
    }

    private void getRecommendedGroup() {
        Utility.callCloudFunctions("dbGroupsGetRecommended", null)
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
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            String result = task.getResult();
                            Log.d("TAG" , result + "");
                            Gson gson = new Gson();
                            Group[] groups = gson.fromJson(result, Group[].class);
                            createRecommendedGroupsViews(groups);
                            }
                    }
                });
    }

    private void createRecommendedGroupsViews (Group[] groups){
        ArrayList<Group> arrayList = new ArrayList<Group>(Arrays.asList(groups));
        RecyclerView homeRecycleView = rootView.findViewById(R.id.list_group_recycle_home_recommended);
        LinearLayoutManager homeLayoutManager = new LinearLayoutManager(getActivity());
        homeRecycleView.setLayoutManager(homeLayoutManager);
        RecommendedGroupAdapter recommendedGroupAdapter = new RecommendedGroupAdapter(context, arrayList, progressBar);
        homeRecycleView.setAdapter(recommendedGroupAdapter);
    }


    private void refreshPulldown(ViewGroup rootView) {
        swipe = rootView.findViewById(R.id.browser_swipe);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initializeAdapter();
                swipe.setRefreshing(false);
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
