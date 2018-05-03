package app.kth.com.groupie.parent;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import app.kth.com.groupie.R;

public class HomeFragment extends Fragment {
    ParentActivity activity;
    private RecyclerView mRecycleView;
    private RecyclerView.Adapter mAdapter;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipe;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInsatnceState){
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);
        mRecycleView = rootView.findViewById(R.id.group_list_recycle_home);
        progressBar = rootView.findViewById(R.id.progressBar);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mRecycleView.setLayoutManager(mLayoutManager);
        initializeAdapter();

        refreshPulldown(rootView);

        return rootView;
    }


    private void initializeAdapter() {
        mAdapter = new HomeAdapter(getActivity(), progressBar);
        mRecycleView.setAdapter(mAdapter);
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
