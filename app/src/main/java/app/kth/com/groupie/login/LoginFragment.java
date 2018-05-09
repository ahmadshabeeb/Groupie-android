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
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;

import app.kth.com.groupie.R;
import app.kth.com.groupie.parent.ParentActivity;
import app.kth.com.groupie.utilities.Utility;


public class LoginFragment extends Fragment {
    private FirebaseAuth mAuth;
    private String errorMessage;
    private LoginActivity activity = new LoginActivity();
    private EditText inputEmail;
    private EditText inputPassword;
    private TextView errorTextView;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        errorMessage = "";
        mAuth = FirebaseAuth.getInstance();
        // Inflate the layout for this fragment
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_login, container, false);

        //LOG IN BUTTON
        Button loginbutton = (Button) rootview.findViewById(R.id.signin_button);
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utility.buttonTimeout(loginbutton))
                    startlogin(v);
            }
        });

        //SIGN UP BUTTON
        Button signup = (Button) rootview.findViewById(R.id.signup_button);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utility.buttonTimeout(signup))
                    activity.goToRegistration();
            }
        });

        //EDITTEXT AND TEXTVIEWS
        inputEmail = (EditText) rootview.findViewById(R.id.email_edittext);
        inputPassword = (EditText) rootview.findViewById(R.id.password_edittext);
        errorTextView = (TextView) rootview.findViewById(R.id.errorMessage);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        TextView forgotPassword = rootview.findViewById(R.id.forgotpassword_textview);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.goToReset();
            }
        });

        return rootview;
    }

    public void startlogin(View view){
        String userEmail = inputEmail.getText().toString();
        String userPasswors = inputPassword.getText().toString();

        errorMessage = checkEmailandPassword(userEmail, userPasswors);
        if(errorMessage == null){
            logIn(userEmail, userPasswors, view);
        } else{
            errorTextView.setText(errorMessage);
        }
    }

    public void logIn(String email, String password, final View view) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //UPDATE USER   INTERFACE move on to homepage?
                            if(mAuth.getCurrentUser().isEmailVerified()) {
                                databaseReference.child("users").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()){
                                            activity.goToHome();
                                        }else{
                                            activity.toFirstLogin();
                                            if(mAuth.getCurrentUser() != null){
                                                activity.goToHome();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            } else {
                                printErrorMessage("Please Verify Your Email To Log In");
                                mAuth.signOut();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            try {
                                throw task.getException();
                            } catch(FirebaseAuthInvalidUserException invalidEmail){
                                printErrorMessage("*Incorrect email address or password");
                            } catch(FirebaseAuthInvalidCredentialsException invalidPassword){
                                printErrorMessage("*Incorrect email address or password");
                            } catch (Exception e){
                                printErrorMessage("*Unexpected error, please try again later");
                            }
                        }
                    }
                });
    }

    private void printErrorMessage(String errorMessage){
        errorTextView.setText(errorMessage);
    }

    private String checkEmailandPassword(String email, String password){
        if((email.length() == 0) | (password.length() == 0)){
            return "*Please enter both email and password";
        }
        return null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (LoginActivity) getActivity();
    }

}





