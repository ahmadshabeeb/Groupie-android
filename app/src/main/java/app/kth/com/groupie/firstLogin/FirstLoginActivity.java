package app.kth.com.groupie.firstLogin;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.functions.FirebaseFunctions;

import org.json.JSONObject;

import app.kth.com.groupie.data.structure.PrivateProfile;

import app.kth.com.groupie.R;
import app.kth.com.groupie.login.LoginActivity;
import app.kth.com.groupie.parent.ParentActivity;
import app.kth.com.groupie.utilities.Utility;

public class FirstLoginActivity extends AppCompatActivity {
    private FirebaseFunctions addProfileFunction;
    private PrivateProfile privateProfile;
    FirebaseAuth mAuth;
    private boolean registrationCompleted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_login);

        mAuth = FirebaseAuth.getInstance();
        UserProfileFragment firstFragment = new UserProfileFragment();
        getSupportFragmentManager().beginTransaction()
                .add(app.kth.com.groupie.R.id.fragment_container, firstFragment).commit();
        addProfileFunction = FirebaseFunctions.getInstance();
        privateProfile = new PrivateProfile();
    }

    @Override
    public void onBackPressed(){
        // don't let the user go back
        finish();
        mAuth.signOut();
    }

    public void addInfoToProfile(String firstName, String lastName, String school,
                                 String fieldOfStudy, String studyLocation, View v){
        //ADD INFO TO THE CREATED PRIVATE PROFILE
        //MAKE FIRST LETTER CAPITAL LETTER
        privateProfile.setFirstName(capitalFirst(firstName));
        privateProfile.setLastName(capitalFirst(lastName));
        privateProfile.setSchool(capitalFirst(school));
        privateProfile.setFieldOfStudy(capitalFirst(fieldOfStudy));
        privateProfile.setStudyLocation(capitalFirst(studyLocation));

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, new BiographyFragment());
        ft.commit();
    }

    private String capitalFirst(String string){
        if(string.length() <= 1){
            return null;
        }
        String s1 = string.substring(0,1).toUpperCase();
        String capitalizedName = s1 + string.substring(1);
        return capitalizedName;
    }


    public void addBioToProfile(String bio){
        privateProfile.setBio(bio);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, new FavoriteSubjectFragment());
        ft.commit();
    }

    public void addFavoriteSubject(String subject){
        privateProfile.setFavoriteSubject(subject);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, new ProfilePictureFragment());
        ft.commit();
    }

    public void addProfilePicture(String imageUri){
        privateProfile.setProfilePicture(imageUri);
      //  Log.d("tag", );
        addToDatabase(privateProfile);
        registrationCompleted = true;
        finish();
    }

    public void addToDatabase(PrivateProfile privateProfile){
        JSONObject profileJson = Utility.toJson(privateProfile);
        addProfileFunction.getHttpsCallable("dbUsersCreate").call(profileJson);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if (!registrationCompleted) {
            mAuth.signOut();
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        if (!registrationCompleted) {
            mAuth.signOut();
        }
    }
}
