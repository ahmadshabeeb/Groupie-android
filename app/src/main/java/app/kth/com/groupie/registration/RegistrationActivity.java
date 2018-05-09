package app.kth.com.groupie.registration;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import app.kth.com.groupie.R;
import app.kth.com.groupie.login.LoginActivity;
import app.kth.com.groupie.utilities.Utility;


public class RegistrationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private View view = this.view;
    private String errorMsg = null;
    private SpannableString resetPassword  = new SpannableString("Did you forget your password?");
    private TextView error;
    private TextView resetPopUp;
    private ImageButton backButton;
    private Button registerButton;
    private CheckBox termsCheckBox;
    private String [] domains = {"kth.se", "su.se", "ki.se", "hhs.se", "sh.se", "kmh.se", "shh.se", "fhs.se", "esh.se", "konstfack.se", "smi.se", "uniarts.se", "kkh.se", "ths.se", "chiro-student.se", "gih.se", "rkh.se"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();
        error = (TextView) findViewById(R.id.errorText);
        resetPassword.setSpan(new UnderlineSpan(), 0, resetPassword.length(), 0);
        registerButton = (Button) findViewById(R.id.registerButton);
        resetPopUp = (TextView) findViewById(R.id.resetPasswordText);
        backButton = (ImageButton) findViewById(R.id.goback_button);
        termsCheckBox = (CheckBox) findViewById(R.id.termsAndService_checkbox);
    }

    @Override
    public void onStart() {
        super.onStart();
        toRegister();
    }

    public void toRegister(){
        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (Utility.buttonTimeout(registerButton))
                    registerUser();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go back to login
                finish();

            }
        });

        resetPopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to reset Password
                goToResetFromRegistration();

            }
        });

    }

    private void registerUser() {
        resetPopUp.setText("");
        // Do something in response to button click
        String[] emailAndPassword = getEmailandPassword(view);
        if (emailAndPassword[1].equals(emailAndPassword[2])){
            if (checkEmail(emailAndPassword[0])){
                if (!emailAndPassword[1].isEmpty()){
                    if (termsCheckBox.isChecked()){
                        createAccount(emailAndPassword[0], emailAndPassword[1], view);
                    }
                    else {
                        errorMsg = "You need to accept the terms and service to sign up!";
                    }
                }
            }else{
                errorMsg = "Your email needs to have one of Stockholm's Universities' domain in order to register!";
            }
        } else {
            errorMsg = "Passwords didn't match!";
        }
        error.setText(errorMsg);
        errorMsg = null;
    }

    public void createAccount(String email, String password, final View view){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful() && errorMsg == null){
                            // sign up successful update UI to sign in this user
                            Log.d("success", "sign up successful");
                            //update user instance
                            FirebaseUser user = mAuth.getCurrentUser();
                            //send verification email and sign them out so they are not allowed to sign in again until they verify
                            verify();
                            signOut();
                            //send this user to login page
                            errorMsg="Congratulations! we have sent you a confirmation email with a link that you need to click before you are able to log in." + "\n" + "We look forward to seeing you on groupie :)";
                            resetPopUp.setText("");
                        } else {
                            try {
                                throw task.getException();
                            } catch(FirebaseAuthUserCollisionException emailExists){
                                errorMsg = "this email is already registered with an account, please try a different email";
                                resetPopUp.setText(resetPassword);
                            } catch (FirebaseAuthWeakPasswordException weakPassword){
                                errorMsg = "password is too weak";
                            } catch (FirebaseAuthInvalidCredentialsException malformedEmail){
                                errorMsg = "invalid email, please try again or use a different email";
                            } catch (Exception e){
                                errorMsg = "Something went wrong :( Please try again!";
                            }
                        }
                    error.setText(errorMsg);
                    errorMsg = null;
                    }
                });
    }

    public void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                        }
                        else{
                            // If sign in fails, display a message to the user.
                            errorMsg = "Authentication Failed";
                            String error = ((TextView) findViewById(R.id.errorText)).getText().toString();
                            error = errorMsg;
                        }
                    }
                });
    }

    public String[] getEmailandPassword(View view){
        String email =  ((EditText)findViewById(R.id.emailText)).getText().toString();
        String password = ((EditText) findViewById(R.id.passwordText)).getText().toString();
        String cpassword = ((EditText) findViewById(R.id.cPasswordText)).getText().toString();
        if(password.isEmpty()){
            errorMsg = "please fill out all the fields!";
            error.setText(errorMsg);
            errorMsg = null;
        }
        return new String[]{email, password, cpassword};
    }

    public void printBar(String message, View view){
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public boolean checkEmail(String email){
        if(!email.isEmpty()){
            String[] emailSplitter = email.split("@");
            if(emailSplitter.length > 1){
                String domain = emailSplitter[1];
                for (String domain1 : domains){
                    if (domain.equals(domain1)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void signOut(){
        mAuth.signOut();
    }

    public void verify() {
        mAuth.getCurrentUser().sendEmailVerification();
    }

    public void goToLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void goToResetFromRegistration(){
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("goToResetFromRegistration", true);
        startActivity(intent);
    }
}
