package app.kth.com.groupie.parent;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import app.kth.com.groupie.R;
import app.kth.com.groupie.utilities.Utility;

public class BrowserFragment extends Fragment {
    ParentActivity activity;
    private RecyclerView.Adapter mAdapter;
    private FilterChoice filterChoice;
    private Resources resources;
    private RecyclerView mRecycleView;
    private boolean[] filterSubjectTriggers = new boolean[9];
    private boolean[] filterDayOfMeetingTriggers = new boolean[7];
    private final Long DAY_IN_SECONDS = 86400l;
    private long[] daysInUNIX = new long[7];
    private SwipeRefreshLayout swipe;
    private ArrayList<Button> daysOfMeeting = new ArrayList<>();
    private ArrayList<ImageView> subjects = new ArrayList<>();
    private ArrayList<Integer> subjectResourceIds = new ArrayList<>();
    int[] y;
    private Button reset;

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInsatnceState) {
        calculateDaysInUNIX();
        y = new int[]{0};

        ViewGroup rootView = createRecycleView(inflater, container);

        initViews(rootView);
        initSubjectResourceIds();

        //logic for refreshing groups
        refreshPulldown(rootView);

        //Click logic for filters
        subjectClickableListener(rootView);
        dayOfMeetingClickableListener(rootView);

        //click logic for reset button
        resetFilterClickableListener(rootView);

        return rootView;
    }

    private void initSubjectResourceIds() {
        subjectResourceIds.add(R.string.language);
        subjectResourceIds.add(R.string.programming);
        subjectResourceIds.add(R.string.math);
        subjectResourceIds.add(R.string.naturalSciences);
        subjectResourceIds.add(R.string.engineering);
        subjectResourceIds.add(R.string.buisnessAndEconomics);
        subjectResourceIds.add(R.string.lawAndPoliticalScience);
        subjectResourceIds.add(R.string.artAndMusic);
        subjectResourceIds.add(R.string.other);
    }

    private void initViews(ViewGroup rootView) {
        //DAY OF MEETING VIEWS
        daysOfMeeting.add(rootView.findViewById(R.id.filter_day1_button));
        daysOfMeeting.add(rootView.findViewById(R.id.filter_day2_button));
        daysOfMeeting.add(rootView.findViewById(R.id.filter_day3_button));
        daysOfMeeting.add(rootView.findViewById(R.id.filter_day4_button));
        daysOfMeeting.add(rootView.findViewById(R.id.filter_day5_button));
        daysOfMeeting.add(rootView.findViewById(R.id.filter_day6_button));
        daysOfMeeting.add(rootView.findViewById(R.id.filter_day7_button));

        //SUBJECT VIEWS
        subjects.add(rootView.findViewById(R.id.language_filter_icon));
        subjects.add(rootView.findViewById(R.id.programming_filter_icon));
        subjects.add(rootView.findViewById(R.id.math_filter_icon));
        subjects.add(rootView.findViewById(R.id.science_filter_icon));
        subjects.add(rootView.findViewById(R.id.engineering_filter_icon));
        subjects.add(rootView.findViewById(R.id.business_filter_icon));
        subjects.add(rootView.findViewById(R.id.law_filter_icon));
        subjects.add(rootView.findViewById(R.id.music_filter_icon));
        subjects.add(rootView.findViewById(R.id.other_filter_icon));

        resources = getResources();
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

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void dayOfMeetingClickableListener(ViewGroup rootView) {
        daysOfMeeting.get(0).setText("Today");
        daysOfMeeting.get(1).setText("Tmrw");

        for (int i = 2; i <= 6; i++) {
            daysOfMeeting.get(i).setText(Utility.getWeekDay(daysInUNIX[i], false));
        }

        int i = 0;
        for (Button v: daysOfMeeting) {
            Log.d("subpos", i + " ...");
            if (i > 6) { i = 0;}
            int finalI = i;
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    filterByDayOfMeeting(finalI, daysOfMeeting.get(finalI));
                    initializeAdapter();
                }
            });
            i++;
        }
    }

    private void subjectClickableListener(ViewGroup rootView) {
        int i = 0;
        for (ImageView v: subjects) {
            Log.d("subpos", i + " ...");
            if (i > 8) { i = 0; }
            int finalI = i;
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    filterBySubject(finalI, subjectResourceIds.get(finalI), subjects.get(finalI));
                    initializeAdapter();
                }
            });
            i++;
        }
    }

    private void filterByDayOfMeeting(int position, Button button) {
        Log.d("POSITION", position + " ASDF");
        if (!filterDayOfMeetingTriggers[position]) {
            filterChoice.addDay(daysInUNIX[position]);
            filterDayOfMeetingTriggers[position] = true;
            button.setBackgroundColor(resources.getColor(R.color.darkNavy));
            button.setTextColor(resources.getColor(R.color.offWhite));

        } else {
            filterChoice.removeDay(daysInUNIX[position]);
            filterDayOfMeetingTriggers[position] = false;
            button.setBackgroundColor(resources.getColor(R.color.offWhite));
            button.setTextColor(resources.getColor(R.color.darkNavy));
        }
    }

    private void filterBySubject(int position, int subject, ImageView view){
        if (!filterSubjectTriggers[position]) {
            filterChoice.addSubject(getResources().getString(subject));
            filterChoice.printSubjects();
            filterSubjectTriggers[position] = true;
            view.setBackgroundColor(resources.getColor(R.color.lightGrey));
        } else {
            filterChoice.removeSubject(getResources().getString(subject));
            filterChoice.printSubjects();
            filterSubjectTriggers[position] = false;
            view.setBackgroundColor(resources.getColor(R.color.offWhite));
        }
    }

    private ViewGroup createRecycleView(LayoutInflater inflater, ViewGroup container) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_browser, container, false);

        mRecycleView = rootView.findViewById(R.id.group_list_recycle);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecycleView.setLayoutManager(mLayoutManager);
        this.filterChoice = activity.filterChoice;

        initializeAdapter();

        return rootView;
    }

    private void initializeAdapter() {
        mAdapter = new GroupAdapter(getActivity(), filterChoice, daysInUNIX);
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

    private void resetFilterClickableListener(ViewGroup rootView) {
        reset = rootView.findViewById(R.id.reset_button);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterChoice.reset();
                resetFilterViews();
            }
        });
    }

    private void resetFilterViews() {
       // for()
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

    private void calculateDaysInUNIX() {
        long todayUnix = getTodayUnix();
        daysInUNIX[0] = todayUnix;

        for(int i=1; i<daysInUNIX.length; i++) {
            daysInUNIX[i] = daysInUNIX[i-1] + DAY_IN_SECONDS;
        }
    }

    public static class FilterChoice {
        private HashMap<String, Boolean> subjects;
        private HashMap<Long, Boolean> daysOfMeeting;

        public FilterChoice() {
            subjects = new HashMap<>();
            daysOfMeeting = new HashMap<>();
        }

        public void addSubject(String subject) {
            subjects.put(subject, true);
        }

        public void removeSubject(String subject) {subjects.remove(subject); }

        public void addDay (Long day) {
            daysOfMeeting.put(day, true);
        }

        public void removeDay (Long day) {
            daysOfMeeting.remove(day);
        }

        public void printSubjects() {
            Log.d("filter", "PRINTING SUBJECTS");
            for (String subject: subjects.keySet()) {
                Log.d("filter", "SUBJECT : " + subject);
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

        public boolean isChosenDay(Long day) {
            if (daysOfMeeting.isEmpty())
                return true;

            for (Long d: daysOfMeeting.keySet()) {
                if (day.equals(d))
                    return true;
            }

            return false;
        }


        public void reset() {
            subjects.clear();
            daysOfMeeting.clear();
        }
    }




}
