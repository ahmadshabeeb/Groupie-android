package app.kth.com.groupie.firstLogin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import app.kth.com.groupie.Data.Structure.PrivateProfile;
import app.kth.com.groupie.R;


public class FirstLoginActivity extends AppCompatActivity {


    PrivateProfile privateProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_login);

        privateProfile = new PrivateProfile();

    }


    public void addInfoToProfile(String firstName, String lastName, String school, String fieldOfStudy, View v){
        //ADD INFO TO THE CREATED PRIVATE PROFILE
        EditText

    }
}
