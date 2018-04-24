package app.kth.com.groupie.createGroup;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import app.kth.com.groupie.R;
import app.kth.com.groupie.data.Group;

public class CreateGroupActivity extends AppCompatActivity {


    Button currentDayButton;
    String currentDate;
    FirebaseAuth mAuth;
    private static SeekBar seek_bar;
    private static TextView seekBarTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        mAuth = FirebaseAuth.getInstance();
        membersSeekBar();

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

    public void membersSeekBar(){
        seek_bar = (SeekBar) findViewById(R.id.members_seekbar);
        seekBarTextView = (TextView) findViewById(R.id.seekBar_textview);

        seek_bar.setMax(15);
        seek_bar.setProgress(5);
        seekBarTextView.setText("max number of members in group: " + seek_bar.getProgress());

        seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressValue;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressValue = progress;
                if (progress < 2){
                    progressValue = 2;
                }
                seekBarTextView.setText("max number of members in group: " + progressValue);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBarTextView.setText("max number of members in group: " + progressValue);
            }
        });
    }

    //GET VALUES FOR THE GROUP
    private String getTopic(){
        EditText topic = (EditText) findViewById(R.id.topic_edittext);
        String enteredTopic =  topic.getText().toString();
        if (enteredTopic.length() < 1){
            return null;
        }
        return enteredTopic;
    }

    private String getDescription(){
        EditText description = (EditText) findViewById(R.id.description_edittext);
        String enteredDesc =  description.getText().toString();
        if (enteredDesc.length() < 1){
            return null;
        }
        return enteredDesc;
    }

    private String getLocation(){
        EditText location = (EditText) findViewById(R.id.location_edittext);
        String enteredLoc =  location.getText().toString();
        if (enteredLoc.length() < 1){
            return null;
        }
        return enteredLoc;
    }

    private String getTodayDate(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(date);
    }

    public void createGroup(){
        Group group = new Group();

        int maxNumberOfMembers = seek_bar.getProgress();
        if(maxNumberOfMembers < 2){
            maxNumberOfMembers = 2;
        }

        group.setTimeOfCreation(getTodayDate());
        group.setTopic(getTopic());
        group.setHasMeetingDate(true);
        group.setDescription(getDescription());
        group.setDateOfMeeting(currentDate);
        group.setOwner(mAuth.getCurrentUser().getUid());
        group.setPublic(true);
        group.setLocation(getLocation());
        group.setNumberOfMembers(1);
        group.setMaxNumberOfMembers(maxNumberOfMembers);

        Map<String, Boolean> members = new HashMap<>();
        members.put(mAuth.getCurrentUser().getUid(), true);
        group.setMembers(members);

    }

}
