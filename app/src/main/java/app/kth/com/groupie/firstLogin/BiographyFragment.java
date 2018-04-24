package app.kth.com.groupie.firstLogin;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import app.kth.com.groupie.R;

public class BiographyFragment extends Fragment {
    EditText biography;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_biography, container, false);
        biography = (EditText) rootview.findViewById(R.id.bio_editText);
        Button nextButton = (Button) rootview.findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBio();
            }
        });
        return rootview;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void addBio(){
        String bio = biography.getText().toString();
        if(bio.length() < 1){
            bio = null;
        }
        FirstLoginActivity activity = (FirstLoginActivity) getActivity();
        activity.addBioToProfile(bio);
    }

}
