package app.kth.com.groupie.createGroup;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import app.kth.com.groupie.R;

public class CreateGroupActivity extends AppCompatActivity {


    Button currentDayButton;
    String currentDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        ImageButton goBack = (ImageButton) findViewById(R.id.back_button);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        switchCurrentDate(0);
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        //SET UP DATE BUTTONS
        final Button day1Button = findViewById(R.id.day1_button);
        day1Button.setText(getDay(dayOfWeek));
        day1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchCurrentButton(day1Button);
                switchCurrentDate(0);
            }
        });

        currentDayButton = day1Button;
        final Button day2Button = findViewById(R.id.day2_button);
        day2Button.setText(getDay(dayOfWeek+1));
        day2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchCurrentButton(day2Button);
                switchCurrentDate(1);
            }
        });

        final Button day3Button = findViewById(R.id.day3_button);
        day3Button.setText(getDay(dayOfWeek +2));
        day3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchCurrentButton(day3Button);
                switchCurrentDate(2);

            }
        });

        final Button day4Button = findViewById(R.id.day4_button);
        day4Button.setText(getDay(dayOfWeek +3));
        day4Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchCurrentButton(day4Button);
                switchCurrentDate(3);
            }
        });


        final Button day5Button = findViewById(R.id.day5_button);
        day5Button.setText(getDay(dayOfWeek +4));
        day5Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchCurrentButton(day5Button);
                switchCurrentDate(4);
            }
        });

        final Button day6Button = findViewById(R.id.day6_button);
        day6Button.setText(getDay(dayOfWeek +5));
        day6Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchCurrentButton(day6Button);
                switchCurrentDate(5);
            }
        });

        final Button day7Button = findViewById(R.id.day7_button);
        day7Button.setText(getDay(dayOfWeek +6));
        day7Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchCurrentButton(day7Button);
                switchCurrentDate(6);
            }
        });

    }

    private String getDay(int dayOfWeek){
        if(dayOfWeek > 7){
            dayOfWeek -= 7;
        }

        switch (dayOfWeek){
            case 2:
                return "MON";
            case 3:
                return "TUES";
            case 4:
                return "WEDS";
            case 5:
                return "THUR";
            case 6:
                return "FRI";
            case 7:
                return "SAT";
            case 1:
                return "SUN";
        }
        return null;
    }

    private void switchCurrentButton(Button otherButton){
        currentDayButton.setBackgroundColor(getResources().getColor(R.color.offWhite));
        currentDayButton.setTextColor(getResources().getColor(R.color.darkNavy));
        currentDayButton = otherButton;
        currentDayButton.setBackgroundColor(getResources().getColor(R.color.darkNavy));
        currentDayButton.setTextColor(getResources().getColor(R.color.offWhite));
    }
    private void switchCurrentDate(int i) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, i);  // number of days to add
        currentDate = sdf.format(c.getTime());
    }

    public void printBar(String message, View view){
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public void createGroup(View v){

    }

}
