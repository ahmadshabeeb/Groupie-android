package app.kth.com.groupie;

import android.content.Intent;
<<<<<<< HEAD
import android.support.v7.app.AppCompatActivity;
=======
>>>>>>> development
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;



import app.kth.com.groupie.parent.ParentActivity;

import app.kth.com.groupie.login.LoginActivity;

import app.kth.com.groupie.registration.RegistrationActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Intent intent = new Intent(this, RegistrationActivity.class);
//        startActivity(intent);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
       // Intent intent = new Intent(this, ParentActivity.class);
        //startActivity(intent);
    }
}
