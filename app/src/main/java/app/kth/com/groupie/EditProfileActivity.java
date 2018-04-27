package app.kth.com.groupie;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import app.kth.com.groupie.data.structure.PrivateProfile;

public class EditProfileActivity extends AppCompatActivity {
    private TextView emailTV;
    private TextView schoolTV;
    private CheckBox mathematicsCB;
    private CheckBox programmingCB;
    private CheckBox languageCB;
    private CheckBox businessAndEconCB;
    private CheckBox engineeringCB;
    private CheckBox naturalSciencesCB;
    private CheckBox lawAndPoliticalScienceCB;
    private CheckBox artAndMusicCB;
    private CheckBox otherCB;
    private EditText nameET;
    private EditText fieldOfStudyET;
    private EditText defaultLocationET;
    private EditText bioET;
    private ImageView profilePicture;
    private Button saveChangesButton;
    private PrivateProfile currentUserProfile;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        currentUserProfile = (PrivateProfile) getIntent().getSerializableExtra("CurrentUserProfile");
        emailTV = (TextView) findViewById(R.id.email_textView);
        schoolTV = (TextView) findViewById(R.id.school_textView);
        mathematicsCB = (CheckBox) findViewById(R.id.mathematicsCB);
        programmingCB = (CheckBox) findViewById(R.id.programmingCB);
        languageCB = (CheckBox) findViewById(R.id.languageCB);
        businessAndEconCB = (CheckBox) findViewById(R.id.business_and_economics_CB);
        engineeringCB = (CheckBox) findViewById(R.id.engineeringCB);
        naturalSciencesCB = (CheckBox) findViewById(R.id.natural_sciences_CB);
        lawAndPoliticalScienceCB = (CheckBox) findViewById(R.id.law_and_political_science_CB);
        artAndMusicCB = (CheckBox) findViewById(R.id.art_and_music_CB);
        otherCB = (CheckBox) findViewById(R.id.otherCB);
        nameET = (EditText) findViewById(R.id.email_edittext);
        fieldOfStudyET = (EditText) findViewById(R.id.fieldofstudy_edittext);
        defaultLocationET = (EditText) findViewById(R.id.default_location_editText);
        bioET = (EditText) findViewById(R.id.bio_editText);
        saveChangesButton = (Button) findViewById(R.id.save_changes_button);
        profilePicture = (ImageView) findViewById(R.id.profile_picture_imageView);
        displayThings();
    }

    public void displayThings(){
        emailTV.setText("Email: " + currentUser.getEmail());
        schoolTV.setText("School: " + currentUserProfile.getSchool());
        nameET.setText(currentUserProfile.getFirstName() + " " + currentUserProfile.getLastName());
        fieldOfStudyET.setText(currentUserProfile.getFieldOfStudy());
        defaultLocationET.setText(currentUserProfile.getStudyLocation());
        bioET.setText(currentUserProfile.getBio());
    }
}
