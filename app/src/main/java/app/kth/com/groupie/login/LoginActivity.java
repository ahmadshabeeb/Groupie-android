package app.kth.com.groupie.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import app.kth.com.groupie.firstLogin.FirstLoginActivity;
import app.kth.com.groupie.parent.ParentActivity;
import app.kth.com.groupie.registration.RegistrationActivity;

public class LoginActivity extends AppCompatActivity {

    String errorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(app.kth.com.groupie.R.layout.activity_login);

        if (findViewById(app.kth.com.groupie.R.id.fragment_container) != null) {

            if (savedInstanceState != null) {
                return;
            }

            if(getIntent().hasExtra("goToResetFromRegistration")){
                ResetPasswordFragment firstFragment = new ResetPasswordFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(app.kth.com.groupie.R.id.fragment_container, firstFragment).commit();
            } else {
                LoginFragment firstFragment = new LoginFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(app.kth.com.groupie.R.id.fragment_container, firstFragment).commit();
            }
        }
    }

    @Override
    public void onBackPressed(){
        // don't let the user go back
        this.finishAffinity();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
    }

    public void goToReset() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(app.kth.com.groupie.R.id.fragment_container, new ResetPasswordFragment());
        ft.commit();
    }

    public void goToSignIn() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(app.kth.com.groupie.R.id.fragment_container, new LoginFragment());
        ft.commit();
    }

    public void goToRegistration(){
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    public void goToHome(){
        Intent intent = new Intent(this, ParentActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }
    public void toFirstLogin(){
        Intent intent = new Intent(this, FirstLoginActivity.class);
        startActivity(intent);
    }
}
