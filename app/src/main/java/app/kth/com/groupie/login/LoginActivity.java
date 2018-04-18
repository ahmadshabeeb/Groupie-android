package app.kth.com.groupie.login;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

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
            LoginFragment firstFragment = new LoginFragment();
            //firstFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(app.kth.com.groupie.R.id.fragment_container, firstFragment).commit();
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
    }

    public void goToReset(View v) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(app.kth.com.groupie.R.id.fragment_container, new ResetPasswordFragment());
        ft.commit();
    }

    public void goToSignIn(View v) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(app.kth.com.groupie.R.id.fragment_container, new LoginFragment());
        ft.commit();
    }
}
