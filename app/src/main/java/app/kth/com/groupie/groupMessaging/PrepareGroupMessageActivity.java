package app.kth.com.groupie.groupMessaging;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;

import app.kth.com.groupie.R;
import app.kth.com.groupie.data.Group;
import app.kth.com.groupie.data.structure.Profile;
import app.kth.com.groupie.utilities.Utility;

public class PrepareGroupMessageActivity extends AppCompatActivity {

    boolean isFirst = true;
    Group mGroup;
    int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare_group_message);
        mMemberProfiles = new ArrayList<>();

        final Intent intent = getIntent();
        mGroup = (Group) intent.getParcelableExtra("group");

        Map<String, Boolean> map = mGroup.getMembers();
        if(!(map.containsKey(FirebaseAuth.getInstance().getCurrentUser().getUid()))) {
            map.put(FirebaseAuth.getInstance().getCurrentUser().getUid(), true);
            i++;
        }
        mGroup.setMembers(map);
        getMembers();
    }

    ArrayList<Profile> mMemberProfiles;
    String TAG = "Preparegroup";
    private void getMembers() {
        for (String userId : mGroup.getMembers().keySet()) {
                Log.d(TAG, userId + " user IDDDD");
                Utility.callCloudFunctions("dbUsersProfileGetPublic", userId)
                        .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                if (!task.isSuccessful()) {
                                    Exception e = task.getException();
                                    if (e instanceof FirebaseFunctionsException) {
                                        FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                                        FirebaseFunctionsException.Code code = ffe.getCode();
                                        String message = ffe.getMessage();
                                        Log.d(TAG, "ERROR CODE: " + code + " ... " + message);
                                    }
                                    Log.w(TAG, "onFailure", e);
                                } else {
                                    String result = task.getResult();
                                    Log.d(TAG, "profile result as a string: " + result);
                                    Gson gson = new Gson();
                                    Profile profile = gson.fromJson(result, Profile.class);
                                    mMemberProfiles.add(profile);

                                    if((mMemberProfiles.size() >= mGroup.getNumberOfMembers() + i) && isFirst){
                                        isFirst = false;
                                        Toast toast = Toast.makeText(getApplicationContext(), "" + mMemberProfiles.size(), Toast.LENGTH_LONG);
                                        toast.show();
                                        Intent intent = new Intent(getApplicationContext(), GroupMessagingActivity.class);
                                        intent.putExtra("group", mGroup);
                                        intent.putExtra("profiles", mMemberProfiles);
                                        finish();
                                        getApplicationContext().startActivity(intent);
                                    }
                                }
                            }
                        });
        }
    }

}
