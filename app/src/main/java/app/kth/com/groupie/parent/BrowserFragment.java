package app.kth.com.groupie.parent;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
    private List<Button> daysOfMeeting;
    private List<ImageView> subjects;
    private List<Integer> subjectResourceIds;
    private Button reset;
    private ProgressBar progressBar;
    private RelativeLayout card;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInsatnceState) {
        //initialize views
        ViewGroup rootView = createRecycleView(inflater, container);
        initViews(rootView);
        initSubjectResourceIds();
        refreshPulldown(rootView);

        //set filter listeners
        subjectClickableListeners();
        dayOfMeetingClickableListeners();
        resetFilterClickableListener(rootView);



        return rootView;
    }

    // ----------------MAIN METHODS--------------
    private ViewGroup createRecycleView(LayoutInflater inflater, ViewGroup container) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_browser, container, false);

        mRecycleView = rootView.findViewById(R.id.group_list_recycle);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecycleView.setLayoutManager(mLayoutManager);
        this.filterChoice = activity.filterChoice;

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        initializeAdapter();

        return rootView;
    }

    private void initViews(ViewGroup rootView) {
        ArrayList<Button> buttonArrayList = new ArrayList<>();

        //DAY OF MEETING VIEWS
        buttonArrayList.add((Button) rootView.findViewById(R.id.filter_day1_button));
        buttonArrayList.add((Button) rootView.findViewById(R.id.filter_day2_button));
        buttonArrayList.add((Button) rootView.findViewById(R.id.filter_day3_button));
        buttonArrayList.add((Button) rootView.findViewById(R.id.filter_day4_button));
        buttonArrayList.add((Button) rootView.findViewById(R.id.filter_day5_button));
        buttonArrayList.add((Button) rootView.findViewById(R.id.filter_day6_button));
        buttonArrayList.add((Button) rootView.findViewById(R.id.filter_day7_button));
        daysOfMeeting = buttonArrayList;

        //SUBJECT VIEWS
        ArrayList<ImageView> imageViewArrayList = new ArrayList<>();
        imageViewArrayList.add((ImageView) rootView.findViewById(R.id.language_filter_icon));
        imageViewArrayList.add((ImageView) rootView.findViewById(R.id.programming_filter_icon));
        imageViewArrayList.add((ImageView) rootView.findViewById(R.id.math_filter_icon));
        imageViewArrayList.add((ImageView) rootView.findViewById(R.id.science_filter_icon));
        imageViewArrayList.add((ImageView) rootView.findViewById(R.id.engineering_filter_icon));
        imageViewArrayList.add((ImageView) rootView.findViewById(R.id.business_filter_icon));
        imageViewArrayList.add((ImageView) rootView.findViewById(R.id.law_filter_icon));
        imageViewArrayList.add((ImageView) rootView.findViewById(R.id.music_filter_icon));
        imageViewArrayList.add((ImageView) rootView.findViewById(R.id.other_filter_icon));
        subjects = imageViewArrayList;

        resources = getResources();
    }

    private void initSubjectResourceIds() {
        ArrayList<Integer> integerArrayList = new ArrayList<>();
        integerArrayList.add(R.string.language);
        integerArrayList.add(R.string.programming);
        integerArrayList.add(R.string.math);
        integerArrayList.add(R.string.naturalSciences);
        integerArrayList.add(R.string.engineering);
        integerArrayList.add(R.string.buisnessAndEconomics);
        integerArrayList.add(R.string.lawAndPoliticalScience);
        integerArrayList.add(R.string.artAndMusic);
        integerArrayList.add(R.string.other);
        subjectResourceIds = integerArrayList;
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

    private void subjectClickableListeners() {
        subjects.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterBySubject(0, subjectResourceIds.get(0), subjects.get(0));
                initializeAdapter();
            }
        });

        subjects.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterBySubject(1, subjectResourceIds.get(1), subjects.get(1));
                initializeAdapter();
            }
        });

        subjects.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterBySubject(2, subjectResourceIds.get(2), subjects.get(2));
                initializeAdapter();
            }
        });

        subjects.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterBySubject(3, subjectResourceIds.get(3), subjects.get(3));
                initializeAdapter();
            }
        });

        subjects.get(4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterBySubject(4, subjectResourceIds.get(4), subjects.get(4));
                initializeAdapter();
            }
        });

        subjects.get(5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterBySubject(5, subjectResourceIds.get(5), subjects.get(5));
                initializeAdapter();
            }
        });

        subjects.get(6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterBySubject(6, subjectResourceIds.get(6), subjects.get(6));
                initializeAdapter();
            }
        });

        subjects.get(7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterBySubject(7, subjectResourceIds.get(7), subjects.get(7));
                initializeAdapter();
            }
        });

        subjects.get(8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterBySubject(8, subjectResourceIds.get(8), subjects.get(8));
                initializeAdapter();
            }
        });
    }

    private void dayOfMeetingClickableListeners() {
        calculateDaysInUNIX();

        daysOfMeeting.get(0).setText("Today");
        daysOfMeeting.get(1).setText("Tmrw");

        for (int i = 2; i <= 6; i++) {
            daysOfMeeting.get(i).setText(Utility.getWeekDay(daysInUNIX[i], false));
        }

        daysOfMeeting.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterByDayOfMeeting(0, daysOfMeeting.get(0));
                initializeAdapter();
            }
        });

        daysOfMeeting.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterByDayOfMeeting(1, daysOfMeeting.get(1));
                initializeAdapter();
            }
        });

        daysOfMeeting.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterByDayOfMeeting(2, daysOfMeeting.get(2));
                initializeAdapter();
            }
        });

        daysOfMeeting.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterByDayOfMeeting(3, daysOfMeeting.get(3));
                initializeAdapter();
            }
        });

        daysOfMeeting.get(4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterByDayOfMeeting(4, daysOfMeeting.get(4));
                initializeAdapter();
            }
        });

        daysOfMeeting.get(5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterByDayOfMeeting(5, daysOfMeeting.get(5));
                initializeAdapter();
            }
        });

        daysOfMeeting.get(6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterByDayOfMeeting(6, daysOfMeeting.get(6));
                initializeAdapter();
            }
        });
    }

    private void resetFilterClickableListener(ViewGroup rootView) {
        reset = rootView.findViewById(R.id.reset_button);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterChoice.reset();
                resetFilterViews();
                initializeAdapter();
            }
        });
    }

    //----------------HELPER METHODS-------------
    private void filterBySubject(int position, int subject, ImageView view){
        if (!filterSubjectTriggers[position]) {
            filterChoice.addSubject(getResources().getString(subject));
            filterSubjectTriggers[position] = true;
            view.setBackgroundColor(resources.getColor(R.color.lightGrey));
        } else {
            filterChoice.removeSubject(getResources().getString(subject));
            filterSubjectTriggers[position] = false;
            view.setBackgroundColor(resources.getColor(R.color.offWhite));
        }
    }

    private void filterByDayOfMeeting(int position, Button button) {
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

    private void initializeAdapter() {
        mAdapter = new BrowserAdapter(getActivity(), filterChoice, daysInUNIX, progressBar);
        mRecycleView.setAdapter(mAdapter);
    }

    //FRONT END FOR RESET BUTTON THAT DESELECTS USER INPUTS
    private void resetFilterViews() {
        subjects.get(0).setBackgroundColor(resources.getColor(R.color.offWhite));
        subjects.get(1).setBackgroundColor(resources.getColor(R.color.offWhite));
        subjects.get(2).setBackgroundColor(resources.getColor(R.color.offWhite));
        subjects.get(3).setBackgroundColor(resources.getColor(R.color.offWhite));
        subjects.get(4).setBackgroundColor(resources.getColor(R.color.offWhite));
        subjects.get(5).setBackgroundColor(resources.getColor(R.color.offWhite));
        subjects.get(6).setBackgroundColor(resources.getColor(R.color.offWhite));
        subjects.get(7).setBackgroundColor(resources.getColor(R.color.offWhite));
        subjects.get(8).setBackgroundColor(resources.getColor(R.color.offWhite));
        filterSubjectTriggers = new boolean[9];

        daysOfMeeting.get(0).setBackgroundColor(resources.getColor(R.color.offWhite));
        daysOfMeeting.get(0).setTextColor(resources.getColor(R.color.darkNavy));
        daysOfMeeting.get(1).setBackgroundColor(resources.getColor(R.color.offWhite));
        daysOfMeeting.get(1).setTextColor(resources.getColor(R.color.darkNavy));
        daysOfMeeting.get(2).setBackgroundColor(resources.getColor(R.color.offWhite));
        daysOfMeeting.get(2).setTextColor(resources.getColor(R.color.darkNavy));
        daysOfMeeting.get(3).setBackgroundColor(resources.getColor(R.color.offWhite));
        daysOfMeeting.get(3).setTextColor(resources.getColor(R.color.darkNavy));
        daysOfMeeting.get(4).setBackgroundColor(resources.getColor(R.color.offWhite));
        daysOfMeeting.get(4).setTextColor(resources.getColor(R.color.darkNavy));
        daysOfMeeting.get(5).setBackgroundColor(resources.getColor(R.color.offWhite));
        daysOfMeeting.get(5).setTextColor(resources.getColor(R.color.darkNavy));
        daysOfMeeting.get(6).setBackgroundColor(resources.getColor(R.color.offWhite));
        daysOfMeeting.get(6).setTextColor(resources.getColor(R.color.darkNavy));

        filterDayOfMeetingTriggers = new boolean[7];
    }

    private void calculateDaysInUNIX() {
        long todayUnix = getTodayInUNIX();
        daysInUNIX[0] = todayUnix;

        for(int i=1; i<daysInUNIX.length; i++) {
            daysInUNIX[i] = daysInUNIX[i-1] + DAY_IN_SECONDS;
        }
    }

    private long getTodayInUNIX(){
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

    //-----------FILTER CLASS-------------
    public static class FilterChoice {
        private HashMap<String, Boolean> subjects;
        private HashMap<Long, Boolean> daysOfMeeting;

        public FilterChoice() {
            subjects = new HashMap<>();
            daysOfMeeting = new HashMap<>();
        }

        private void addSubject(String subject) {
            subjects.put(subject, true);
        }

        private void removeSubject(String subject) {subjects.remove(subject); }

        private void addDay (Long day) {
            daysOfMeeting.put(day, true);
        }

        private void removeDay (Long day) {
            daysOfMeeting.remove(day);
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

        public boolean isEmpty(){
            return subjects.isEmpty() && daysOfMeeting.isEmpty();
        }
    }

    //-------------FRAGMENT RELATED METHODS---------------
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (ParentActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        resetFilterViews();
        filterChoice.reset();
        activity = null;
    }
}