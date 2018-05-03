package app.kth.com.groupie;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Context;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;

import app.kth.com.groupie.data.structure.PrivateProfile;
import app.kth.com.groupie.firstLogin.ProfilePictureFragment;

public class EditProfileActivity extends AppCompatActivity {
    private TextView emailTV;
    private TextView schoolTV;
    private final int PICK_IMAGE_REQUEST = 1234;
    private int checkedButtonID;
    private EditText nameET;
    private EditText fieldOfStudyET;
    private EditText defaultLocationET;
    private EditText bioET;
    private ImageView profilePicture;
    private Button saveChangesButton;
    private PrivateProfile currentUserProfile;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private RadioGroup radioGroup;
    private Uri filePath;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private TextView msgText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        msgText = findViewById(R.id.errorTextView);
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        currentUser = mAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        currentUserProfile = (PrivateProfile) getIntent().getSerializableExtra("CurrentUserProfile");
        emailTV = (TextView) findViewById(R.id.email_textView);
        schoolTV = (TextView) findViewById(R.id.school_textView);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        nameET = (EditText) findViewById(R.id.name_editText);
        fieldOfStudyET = (EditText) findViewById(R.id.field_of_study_editText);
        defaultLocationET = (EditText) findViewById(R.id.default_location_editText);
        bioET = (EditText) findViewById(R.id.bio_editText);
        saveChangesButton = (Button) findViewById(R.id.save_changes_button);
        profilePicture = (ImageView) findViewById(R.id.profile_picture_imageView);
        displayThings();
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyCurrentUserProfile();
            }
        });
    }


    public void displayThings(){
        emailTV.setText("Email: " + currentUser.getEmail());
        schoolTV.setText("School: " + currentUserProfile.getSchool());
        nameET.setText(currentUserProfile.getFirstName() + " " + currentUserProfile.getLastName());
        fieldOfStudyET.setText(currentUserProfile.getFieldOfStudy());
        defaultLocationET.setText(currentUserProfile.getStudyLocation());
        if(currentUserProfile.getBio() != null){
            bioET.setText(currentUserProfile.getBio());
        }
        preChooseFavoriteSubject();
    }

    public void preChooseFavoriteSubject(){
        switch(currentUserProfile.getFavoriteSubject()){
            case "Mathematics" :
                radioGroup.check(R.id.mathematicsRB);
                break;
            case "Language" :
                radioGroup.check(R.id.languageRB);
                break;
            case "Programming" :
                radioGroup.check(R.id.programmingRB);
                break;
            case "Law and Political Science" :
                radioGroup.check(R.id.law_and_political_science_RB);
                break;
            case "Art and Music" :
                radioGroup.check(R.id.art_and_music_RB);
                break;
            case "Engineering" :
                radioGroup.check(R.id.engineeringRB);
                break;
            case "Business and Economics" :
                radioGroup.check(R.id.business_and_economics_RB);
                break;
            case "Natural Sciences" :
                radioGroup.check(R.id.natural_science_RB);
                break;
            case "Other":
                radioGroup.check(R.id.otherRB);
                break;
        }
    }

    public void modifyCurrentUserProfile(){
        msgText.setText(" ");
        String fos = fieldOfStudyET.getText().toString();
        String bio = bioET.getText().toString();
        String studyLocation = defaultLocationET.getText().toString();
        checkedButtonID = radioGroup.getCheckedRadioButtonId();
        String[] firstAndLastName = nameET.getText().toString().split(" ", 2);
        if(firstAndLastName.length > 1 && !firstAndLastName[0].isEmpty() && !firstAndLastName[1].isEmpty()){
            currentUserProfile.setFirstName(capitalFirst(firstAndLastName[0]));
            currentUserProfile.setLastName(capitalFirst(firstAndLastName[1]));
            if (!bio.isEmpty()){
                currentUserProfile.setBio(bio);
            }
            if(!fos.isEmpty() && !studyLocation.isEmpty()){
                currentUserProfile.setStudyLocation(studyLocation);
                currentUserProfile.setFieldOfStudy(fos);
                switch(checkedButtonID){
                    case R.id.mathematicsRB :
                        currentUserProfile.setFavoriteSubject("Mathematics");
                        break;
                    case R.id.languageRB :
                        currentUserProfile.setFavoriteSubject("Language");
                        break;
                    case R.id.programmingRB :
                        currentUserProfile.setFavoriteSubject("Programming");
                        break;
                    case R.id.law_and_political_science_RB :
                        currentUserProfile.setFavoriteSubject("Law and Political Science");
                        break;
                    case R.id.art_and_music_RB :
                        currentUserProfile.setFavoriteSubject("Art and Music");
                        break;
                    case R.id.engineeringRB :
                        currentUserProfile.setFavoriteSubject("Engineering");
                        break;
                    case R.id.business_and_economics_RB :
                        currentUserProfile.setFavoriteSubject("Business and Economics");
                        break;
                    case R.id.natural_science_RB :
                        currentUserProfile.setFavoriteSubject("Natural Sciences");
                        break;
                    case R.id.otherRB :
                        currentUserProfile.setFavoriteSubject("Other");
                        break;
                }
                updateInDatabase();
            } else {
                msgText.setText("Enter study location and field of study please!");
            }
        } else {
            msgText.setText("Please fill in First and Last name in this format (FirstName LastName)");
        }
    }

    private String capitalFirst(String string){
        if(string.length() <= 1){
            return null;
        }
        String s1 = string.substring(0,1).toUpperCase();
        String capitalizedName = s1 + string.substring(1);
        return capitalizedName;
    }

    public void chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profilePicture.setImageBitmap(bitmap);
                uploadImage();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(EditProfileActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            Uri url = taskSnapshot.getDownloadUrl();
                            Log.d("tag", "onSuccess: " +url );
                            currentUserProfile.setProfilePicture(url.toString());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(EditProfileActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    public void updateInDatabase(){
        databaseReference.child("users").child(currentUser.getUid()).child("profile").setValue(currentUserProfile);
        finish();
    }

}
