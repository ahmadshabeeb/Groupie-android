package app.kth.com.groupie.parent;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInsatnceState) {
        calculateDaysInUNIX();

        ViewGroup rootView = createRecycleView(inflater, container);

        swipe = (SwipeRefreshLayout) rootView.findViewById(R.id.browser_swipe);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initializeAdapter();
                swipe.setRefreshing(false);
            }
        });

        //Click logic for filters
        subjectClickableListener(rootView);
        dayOfMeetingClickableListener(rootView);

        return rootView;
    }

    private void dayOfMeetingClickableListener(ViewGroup rootView) {
        final Button day1 = (Button) rootView.findViewById(R.id.filter_day1_button);
        day1.setText("Today");
        final Button day2 = (Button) rootView.findViewById(R.id.filter_day2_button);
        day2.setText("Tmrw");
        final Button day3 = (Button) rootView.findViewById(R.id.filter_day3_button);
        day3.setText(Utility.getWeekDay(daysInUNIX[2], false));
        final Button day4 = (Button) rootView.findViewById(R.id.filter_day4_button);
        day4.setText(Utility.getWeekDay(daysInUNIX[3], false));
        final Button day5 = (Button) rootView.findViewById(R.id.filter_day5_button);
        day5.setText(Utility.getWeekDay(daysInUNIX[4], false));
        final Button day6 = (Button) rootView.findViewById(R.id.filter_day6_button);
        day6.setText(Utility.getWeekDay(daysInUNIX[5], false));
        final Button day7 = (Button) rootView.findViewById(R.id.filter_day7_button);
        day7.setText(Utility.getWeekDay(daysInUNIX[6], false));

        day1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterByDayOfMeeting(0, (Button)v);
                initializeAdapter();
            }
        });

        day2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterByDayOfMeeting(1, (Button)v);
                initializeAdapter();
            }
        });

        day3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterByDayOfMeeting(2, (Button)v);
                initializeAdapter();
            }
        });

        day4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterByDayOfMeeting(3, (Button)v);
                initializeAdapter();
            }
        });

        day5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterByDayOfMeeting(4, (Button)v);
                initializeAdapter();
            }
        });

        day6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterByDayOfMeeting(5, (Button)v);
                initializeAdapter();
            }
        });

        day7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterByDayOfMeeting(6, (Button)v);
                initializeAdapter();
            }
        });
    }

    private void subjectClickableListener(ViewGroup rootView) {
        resources = getResources();

        final ImageView filterLanguage = (ImageView) rootView.findViewById(R.id.language_filter_icon);
        final ImageView filterProgramming = (ImageView) rootView.findViewById(R.id.programming_filter_icon);
        final ImageView filterMath = (ImageView) rootView.findViewById(R.id.math_filter_icon);
        final ImageView filterScience = (ImageView) rootView.findViewById(R.id.science_filter_icon);
        final ImageView filterEngineering = (ImageView) rootView.findViewById(R.id.engineering_filter_icon);
        final ImageView filterBusiness = (ImageView) rootView.findViewById(R.id.business_filter_icon);
        final ImageView filterLaw = (ImageView) rootView.findViewById(R.id.law_filter_icon);
        final ImageView filterMusic = (ImageView) rootView.findViewById(R.id.music_filter_icon);
        final ImageView filterOther = (ImageView) rootView.findViewById(R.id.other_filter_icon);

        filterLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterBySubject(0,R.string.language, filterLanguage);
                Log.d("HHH", "here");
                initializeAdapter();
            }
        });

        filterProgramming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterBySubject(1,R.string.programming, filterProgramming );
                initializeAdapter();
            }
        });

        filterMath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterBySubject(2,R.string.math, filterMath);
                initializeAdapter();
            }
        });

        filterScience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterBySubject(3,R.string.naturalSciences, filterScience);
                initializeAdapter();
            }
        });

        filterEngineering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterBySubject(4,R.string.engineering, filterEngineering);
                initializeAdapter();
            }
        });

        filterBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterBySubject(5,R.string.buisnessAndEconomics, filterBusiness);
                initializeAdapter();
            }
        });

        filterLaw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterBySubject(6,R.string.lawAndPoliticalScience, filterLaw);
                initializeAdapter();
            }
        });

        filterMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterBySubject(7,R.string.artAndMusic, filterMusic);
                initializeAdapter();
            }
        });

        filterOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterBySubject(8,R.string.other, filterOther);
                initializeAdapter();
            }
        });
    }

    private void filterByDayOfMeeting(int position, Button button){
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

        mRecycleView = (RecyclerView) rootView.findViewById(R.id.group_list_recycle);
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
    }


}
