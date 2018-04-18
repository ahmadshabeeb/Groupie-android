package app.kth.com.groupie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import app.kth.com.groupie.registration.RegistrationActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }
}
