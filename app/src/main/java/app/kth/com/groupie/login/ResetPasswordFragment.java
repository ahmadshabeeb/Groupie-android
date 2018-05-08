package app.kth.com.groupie.login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import app.kth.com.groupie.R;
import app.kth.com.groupie.utilities.Utility;


public class ResetPasswordFragment extends Fragment {
    private FirebaseAuth mAuth;
    LoginActivity activity;
    EditText emailInput;

    public ResetPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        // Inflate the layout for this fragment
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_reset_password, container, false);

        //INPUT TEXT
        emailInput = (EditText)  rootview.findViewById(R.id.resetpassword_edittext);

        //RESET BUTTON
        Button resetButton = (Button) rootview.findViewById(R.id.resetpassword_button);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString();

                if(!email.isEmpty()) {
                    if (Utility.buttonTimeout(resetButton)) {
                        sendResetPasswordMail(v, email);
                    }
                } else {
                    Toast.makeText(getActivity(), "Enter an email", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageButton goBackButton = (ImageButton) rootview.findViewById(R.id.goback_button);
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.goToSignIn();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        // Inflate the layout for this fragment
        return rootview;
    }
    
    public void sendResetPasswordMail(final View view, String email){
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //PRINT?
                            Toast.makeText(getActivity(), "We've sent you a reset password link to your email", Toast.LENGTH_LONG).show();
                        } else {
                            //PRINT ERROR MESSAGE?
                            Toast.makeText(getActivity(), "Error :(", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (LoginActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
