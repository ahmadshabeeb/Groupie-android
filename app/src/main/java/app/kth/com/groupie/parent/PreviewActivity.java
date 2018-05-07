package app.kth.com.groupie.parent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.gson.Gson;

import java.util.ArrayList;

import app.kth.com.groupie.R;
import app.kth.com.groupie.data.Group;
import app.kth.com.groupie.data.structure.Profile;
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
    private ArrayList<Profile> previewProfiles;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private int subjectID;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_preview);

        previewProfiles = new ArrayList<>();
        group = getIntent().getParcelableExtra("group");
        makeProfileList(group);

        recyclerView = (RecyclerView) findViewById(R.id.profile_preview_recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        adapter = new ProfilePreviewAdapter(previewProfiles, context);
        recyclerView.setAdapter(adapter);

        subjectIcon = findViewById(R.id.subject_icon);
        subjectID = getIntent().getExtras().getInt("SubjectID");
        joinButton =  findViewById(R.id.join_button);
        subject = findViewById(R.id.what);
        location = findViewById(R.id.where);
        dayOfMeeting = findViewById(R.id.when);
        description = findViewById(R.id.description);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width * 0.8), (int)(height * 0.6));

        displayGroupInfo();
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinGroup();
            }
        });
    }

    public void displayGroupInfo(){
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
                            finish();
                        }
                    }
                });
    }

    public void makeProfileList(Group group){
        for (String userID : group.getMembers().keySet()){
            Log.d("tag", "makeProfileList: " + userID);
            Utility.callCloudFunctions("dbUsersProfileGetPublic", userID).
                    addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if(!task.isSuccessful()){
                                Exception e = task.getException();
                                if (e instanceof FirebaseFunctionsException) {
                                    FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                                    FirebaseFunctionsException.Code code = ffe.getCode();
                                    String message = ffe.getMessage();
                                    Log.d("tag", "ERROR CODE: " + code + " ... " + message);
                                }
                                Log.w("tag", "onFailure", e);
                            }else {
                                String result = task.getResult();
                                Gson gson = new Gson();
                                Profile profile = gson.fromJson(result, Profile.class);
                                Log.d("tag", "firstName: " + profile.getFirstName());
                                previewProfiles.add(profile);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
        }
    }
}
