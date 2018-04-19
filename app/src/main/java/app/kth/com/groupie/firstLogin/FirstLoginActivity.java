package app.kth.com.groupie.firstLogin;

import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import app.kth.com.groupie.data.structure.PrivateProfile;

import app.kth.com.groupie.R;
import app.kth.com.groupie.login.ResetPasswordFragment;
public class FirstLoginActivity extends AppCompatActivity {

    PrivateProfile privateProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_login);

        UserProfileFragment firstFragment = new UserProfileFragment();
        getSupportFragmentManager().beginTransaction()
                .add(app.kth.com.groupie.R.id.fragment_container, firstFragment).commit();

        privateProfile = new PrivateProfile();

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

    public void printBar(String message, View view){
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public void addBioToProfile(String bio, View v){
        privateProfile.setBio(bio);
        printBar(bio + privateProfile.getFirstName(), v);
    }


}
