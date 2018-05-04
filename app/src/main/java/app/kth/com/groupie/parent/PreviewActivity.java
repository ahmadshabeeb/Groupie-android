package app.kth.com.groupie.parent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctionsException;

import app.kth.com.groupie.R;
import app.kth.com.groupie.data.Group;
import app.kth.com.groupie.groupMessaging.GroupMessagingActivity;
import app.kth.com.groupie.utilities.Utility;

public class PreviewActivity extends Activity {
    private TextView subject;
    private TextView location;
    private TextView dayOfMeeting;
    private TextView description;
    private Button joinButton;
    private Group group;
    private ImageView subjectIcon;
    private int subjectID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        subjectIcon = findViewById(R.id.subject_icon);
        subjectID = getIntent().getExtras().getInt("SubjectID");
        joinButton =  findViewById(R.id.join_button);
        group = getIntent().getParcelableExtra("group");
        subject = findViewById(R.id.what);
        location = findViewById(R.id.where);
        dayOfMeeting = findViewById(R.id.when);
        description = findViewById(R.id.description);
        getWindow().setLayout((int)(width * 0.8), (int)(height * 0.6));
        displayThings();
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinGroup();
            }
        });
    }

    public void displayThings(){
        subject.setText("What: " + group.getSubject());
        if(group.getLocation() != null){
            location.setText("Where: " + group.getLocation());
        }else{
            location.setText("Where: Location Not Specified");
        }
        dayOfMeeting.setText("When: " + group.getDateOfMeeting());
        description.setText(group.getDescription());
        subjectIcon.setImageResource(subjectID);
    }

    public void joinGroup(){
        final String groupId = group.getGroupId();

        Utility.callCloudFunctions("dbGroupsJoin", groupId)
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
                                Log.d("TAG", "ERROR CODE: " + code + " ... " + message);
                            }
                            Log.w("TAG", "onFailure", e);
                            return;
                        } else {
                            String result = task.getResult();
                            Intent intent = new Intent(PreviewActivity.this , GroupMessagingActivity.class);
                            Log.d("TAG", "JOINING THIS GROUP " + group.getGroupId());
                            intent.putExtra("group", group);
                            startActivity(intent);
                        }
                    }
                });
    }
}
