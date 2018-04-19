package app.kth.com.groupie.firstLogin;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

import app.kth.com.groupie.R;

public class UserProfileFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    private String [] domains = {"kth.se", "su.se", "ki.se", "hhs.se",
            "sh.se", "kmh.se", "shh.se", "fhs.se", "esh.se", "konstfack.se",
            "smi.se", "uniarts.se", "kkh.se", "ths.se", "chiro-student.se", "gih.se", "rkh.se"};
    private String [] nameOfDomains = {"Kungliga Tekniska Högskolan", "Stockholms Universitet", "Karolinska Institutet",
            "Handelshögskolan", "Södertörn Högskola", "Kungliga Musikhögskolan", "Sophiahemmet Högskola",
            "Försvarshögskolan", "Ersta Sköndal Högskola", "Konstfack", "Stockholms Musikpedagogiska Högskola",
            "Stockholms Konstnärliga Högskola", "Kungliga Konsthögskolan", "Teologiska Högskolan Stockholm",
            "Skandinaviska Kiropraktorhögskolan", "Gymnastik- och idrottshögskolan", "Röda Korsets Högskolaf"};

    FirebaseAuth mAuth;
    EditText firstName;
    EditText lastName;
    EditText fieldOfStudy;
    EditText studyLocation;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_user_profile, container, false);
        mAuth = FirebaseAuth.getInstance();

        //INPUT EDITTEXT FIELDS
        firstName = (EditText) rootview.findViewById(R.id.firstname_edittext);
        lastName = (EditText) rootview.findViewById(R.id.lastname_edittext);
        fieldOfStudy = (EditText) rootview.findViewById(R.id.fieldofstudy_edittext);
        studyLocation = (EditText) rootview.findViewById(R.id.studylocation_edittext);

        Button nextButton = (Button) rootview.findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addInfoAndMoveOn(v);
            }
        });

        return rootview;
    }

    private void addInfoAndMoveOn(View v){
        String firstNameInput = firstName.getText().toString();
        String lastNameInput = lastName.getText().toString();
        String schoolInput = getNameOfSchool();
        String fieldOfStudyInput = fieldOfStudy.getText().toString();
        String studyLocationInput = studyLocation.getText().toString();

        if((firstNameInput.length() < 1) | (lastNameInput.length() < 1) | (fieldOfStudyInput.length() < 1) |
                (studyLocationInput.length() < 1)){

        } else {
            FirstLoginActivity activity = (FirstLoginActivity) getActivity();
            activity.addInfoToProfile(firstNameInput, lastNameInput, schoolInput,
                    fieldOfStudyInput, studyLocationInput, v);
        }
    }

    private String getNameOfSchool(){
        String email = mAuth.getCurrentUser().getEmail();
        String [] split = email.split("@");
        String domain = split[1];
        for (int index = 0; index < domains.length; index++){
            if(domain.equals(domains[index])){
                return nameOfDomains[index];
            }
        }
        return "";

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


}
