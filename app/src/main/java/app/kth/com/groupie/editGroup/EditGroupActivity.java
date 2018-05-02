package app.kth.com.groupie.editGroup;

import android.graphics.Paint;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import app.kth.com.groupie.R;
import app.kth.com.groupie.data.Group;

public class EditGroupActivity extends Activity {

    Group group;
    Button currentDayButton;
    String currentDate;
    EditText locationEditText;
    EditText descriptionEditText;
    SeekBar seek_bar;
    DatabaseReference dbr;
    Switch privateSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group);

        //Group group = (Group) getIntent().getSerializableExtra("group");
        group = getFakeGroup();

        initButtons();
        membersSeekBar();
        initSwitch();

        try {
            setDayButton();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        TextView subject = (TextView) findViewById(R.id.groupsubject_textview);
        subject.setText(group.getSubject());
        TextView topic = (TextView) findViewById(R.id.grouptopic_textview);
        topic.setText(group.getTopic());

        locationEditText = (EditText) findViewById(R.id.location_edittext);
        locationEditText.setText(group.getLocation());

        descriptionEditText = (EditText) findViewById(R.id.description_edittext);
        descriptionEditText.setText(group.getDescription());

        Button saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });

    }

    private void initSwitch(){
        privateSwitch = (Switch) findViewById(R.id.private_switch);
        if(group.getIsPublic()){
            privateSwitch.setChecked(true);
            privateSwitch.setText("public");
        } else{
            privateSwitch.setText("private");
        }
        privateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!privateSwitch.isChecked()){
                    privateSwitch.setText("private");
                } else {
                   privateSwitch.setText("public");
                }
            }
        });
    }

    private Group getFakeGroup(){
        Group group = new Group();
        group.setGroupId("-LB5FW2THw2tC7Z2sG-N");
        group.setConversationId("-LB5Fhf0xMTEoHzzxc5u");
        group.setTopic("Kalkulus");
        group.setSubject("Math");
        group.setMaxNumberOfMembers(7);
        group.setDateOfMeeting("04-05-2018");
        group.setHasMeetingDate(true);
        group.setLocation("KTH KISTA");
        group.setDescription("I want to do math");
        group.setIsPublic(true);
        group.setOwner("PncdUlIsTtZIiROftRPvchzjYUb2");
        group.setTimeOfCreation("27-04-2018");

        return group;
    }

    private void initButtons(){
        currentDate = group.getDateOfMeeting();
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
    private void switchCurrentButton(Button otherButton){
        currentDayButton.setBackgroundColor(getResources().getColor(R.color.offWhite));
        currentDayButton.setTextColor(getResources().getColor(R.color.darkNavy));
        currentDayButton = otherButton;
        currentDayButton.setBackgroundColor(getResources().getColor(R.color.darkNavy));
        currentDayButton.setTextColor(getResources().getColor(R.color.offWhite));
    }

    private void setDayButton() throws ParseException {
        if(!group.isHasMeetingDate()){
            currentDayButton = (Button) findViewById(R.id.day1_button);
            return;
        }
        int [] buttonId = {R.id.day1_button, R.id.day2_button, R.id.day3_button, R.id.day4_button,
                            R.id.day5_button, R.id.day6_button, R.id.day7_button};
        String meetingDay = group.getDateOfMeeting();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date dayInput = sdf.parse(meetingDay);
        Calendar c = Calendar.getInstance();

        for(int i = 0; i < 7; i++){
            if(c.getTime().before(dayInput)){
                c.add(Calendar.DATE, 1);
            } else{
                Button result = findViewById(buttonId[i]);
                result.setBackgroundColor(getResources().getColor(R.color.darkNavy));
                result.setTextColor(getResources().getColor(R.color.offWhite));
                currentDayButton = result;
                return;
            }
        }
    }

    private void switchCurrentDate(int i) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, i);  // number of days to add
        currentDate = sdf.format(c.getTime());
        group.setHasMeetingDate(true);
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

    public void membersSeekBar(){
        seek_bar = (SeekBar) findViewById(R.id.seekBar);
        final TextView seekBarTextView = (TextView) findViewById(R.id.maxmember_textview);
        final String message = "Max members: ";

        seek_bar.setMax(15);
        seek_bar.setProgress(group.getMaxNumberOfMembers());
        seekBarTextView.setText(message + seek_bar.getProgress());

        seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressValue;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressValue = progress;
                if (progress < 2){
                    progressValue = 2;
                }
                seekBarTextView.setText(message + progressValue);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBarTextView.setText(message + progressValue);
            }
        });
    }

    private void saveChanges(){
        int members = seek_bar.getProgress();
        if(members < 2){
            members = 2;
        }
        dbr = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> updatedvalues = new HashMap<String, Object>();
        updatedvalues.put("description", descriptionEditText.getText().toString());
        updatedvalues.put("location", locationEditText.getText().toString());
        updatedvalues.put("maxNumberOfMembers", members);
        updatedvalues.put("dateOfMeeting", currentDate);
        updatedvalues.put("hasMeetingDate", group.isHasMeetingDate());
        updatedvalues.put("public", privateSwitch.isChecked());

        dbr.child("groups").child(group.getGroupId()).updateChildren(updatedvalues);
        finish();
    }
}
