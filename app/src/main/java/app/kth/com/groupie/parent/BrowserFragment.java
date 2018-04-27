package app.kth.com.groupie.parent;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.HashMap;

import app.kth.com.groupie.R;

public class BrowserFragment extends Fragment {
    ParentActivity activity;
    private RecyclerView.Adapter mAdapter;
    private FilterChoice filterChoice;
    private Resources resources;
    private RecyclerView mRecycleView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInsatnceState) {
        ViewGroup rootView = createRecycleView(inflater, container);
        //filterChoice.addSubject("Math");
        subjectClickableListener(rootView);
        filterChoice.printSubjects();
        return rootView;
    }

    private void subjectClickableListener(ViewGroup rootView) {
        resources = getResources();

        ImageView filterLanguage = (ImageView) rootView.findViewById(R.id.language_filter_icon);
        ImageView filterProgramming = (ImageView) rootView.findViewById(R.id.programming_filter_icon);
        ImageView filterMath = (ImageView) rootView.findViewById(R.id.math_filter_icon);
        ImageView filterScience = (ImageView) rootView.findViewById(R.id.science_filter_icon);
        ImageView filterEnginerring = (ImageView) rootView.findViewById(R.id.engineering_filter_icon);
        ImageView filterBusiness = (ImageView) rootView.findViewById(R.id.business_filter_icon);
        ImageView filterLaw = (ImageView) rootView.findViewById(R.id.law_filter_icon);
        ImageView filterMusic = (ImageView) rootView.findViewById(R.id.music_filter_icon);
        ImageView filterOther = (ImageView) rootView.findViewById(R.id.other_filter_icon);

        filterLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "CLICK HERE!!!!!");
                filterChoice.addSubject("Language");
                filterChoice.printSubjects();
                initializeAdapter();
            }
        });

    }

    private ViewGroup createRecycleView(LayoutInflater inflater, ViewGroup container) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_browser, container, false);

        mRecycleView = (RecyclerView) rootView.findViewById(R.id.group_list_recycle);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        mRecycleView.setLayoutManager(mLayoutManager);

        this.filterChoice = activity.filterChoice;

        initializeAdapter();

        return rootView;
    }

    private void initializeAdapter() {
        mAdapter = new GroupAdapter(getActivity(), filterChoice);
        mRecycleView.setAdapter(mAdapter);
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

    public static class FilterChoice {
        private HashMap<String, Boolean> subjects;

        public FilterChoice() {
            subjects = new HashMap<>();
        }

        public void addSubject(String subject) {
            subjects.put(subject, true);
        }

        public void printSubjects() {
            Log.d("TAG", "PRINTING SUBJECTS");
            for (String subject: subjects.keySet()) {
                Log.d("TAG", "SUBJECT : " + subject);
            }
        }

        public boolean isChosenSubject(String groupSubject) {
            if (subjects.isEmpty())
                return true;

            for (String subject: subjects.keySet()) {
                if (subject.equals(groupSubject))
                    return true;
            }

            return false;
        }
    }

}
