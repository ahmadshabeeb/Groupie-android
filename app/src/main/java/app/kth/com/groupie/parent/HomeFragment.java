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
import android.widget.RelativeLayout;
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
    private RecyclerView myGorupsRecycleView;
    private RecyclerView recommendedGroupsRecycleView;
    private RecyclerView.Adapter myGroupsAdapter;
    private RecyclerView.Adapter recommendedGroupsAdapter;
    private RelativeLayout myGroupsprogressBar;
    private RelativeLayout recommendedGroupsprogressBar;
    private SwipeRefreshLayout swipe;
    private ViewGroup rootView;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInsatnceState){
        this.context = getActivity();
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);
        myGorupsRecycleView = rootView.findViewById(R.id.group_list_recycle_home);
        myGroupsprogressBar = rootView.findViewById(R.id.progressBar_my_groups);
        recommendedGroupsprogressBar = rootView.findViewById(R.id.progressBar_recommended_groups);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        myGorupsRecycleView.setLayoutManager(mLayoutManager);
        initializeMyGroupsAdapter();

        getRecommendedGroup();

        refreshPulldown(rootView);

        return rootView;
    }

    private void initializeMyGroupsAdapter() {
        myGroupsAdapter = new HomeAdapter(getActivity(), myGroupsprogressBar);
        myGorupsRecycleView.setAdapter(myGroupsAdapter);
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
        recommendedGroupsRecycleView = rootView.findViewById(R.id.list_group_recycle_home_recommended);
        LinearLayoutManager homeLayoutManager = new LinearLayoutManager(getActivity());
        recommendedGroupsRecycleView.setLayoutManager(homeLayoutManager);
        recommendedGroupsAdapter = new RecommendedGroupAdapter(context, arrayList, recommendedGroupsprogressBar);
        recommendedGroupsRecycleView.setAdapter(recommendedGroupsAdapter);
    }

    private void refreshPulldown(ViewGroup rootView) {
        swipe = rootView.findViewById(R.id.browser_swipe);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initializeMyGroupsAdapter();
                getRecommendedGroup();
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
