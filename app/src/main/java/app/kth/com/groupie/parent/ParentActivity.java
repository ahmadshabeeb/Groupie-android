package app.kth.com.groupie.parent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import app.kth.com.groupie.EditProfileActivity;
import app.kth.com.groupie.R;
import app.kth.com.groupie.SettingsActivity;
import app.kth.com.groupie.createGroup.CreateGroupActivity;
import app.kth.com.groupie.data.structure.PrivateProfile;
import app.kth.com.groupie.editGroup.EditGroupActivity;
import app.kth.com.groupie.groupMessaging.GroupMessagingActivity;
import app.kth.com.groupie.login.LoginActivity;

public class ParentActivity extends AppCompatActivity {
    HomeFragment homeFragment;
    ProfileFragment profileFragment;
    BrowserFragment browserFragment;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference databaseReference;
    PrivateProfile currentUserProfile;
    private boolean userloogedIn = false;
    public BrowserFragment.FilterChoice filterChoice;
    private boolean isCreateGroupPressed = false;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                    return true;
                case R.id.navigation_dashboard:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, browserFragment).commit();
                    return true;
                case R.id.navigation_profile:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();
                    return true;
            }
            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        homeFragment = new HomeFragment();
        browserFragment = new BrowserFragment();
        profileFragment = new ProfileFragment();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //Filtering for groups in browser
        filterChoice= new BrowserFragment.FilterChoice();
    }

    @Override
    public void onStart(){
        Log.d("tag", "onStart: We here");
        super.onStart();
        if (currentUser == null){
            toLoginActivity();
        } else{
            getUserProfile();
            getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
            BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.parent_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.action_create_group:
                if (!isCreateGroupPressed) {
                    isCreateGroupPressed = true;
                    Intent intent = new Intent(this, CreateGroupActivity.class);
                    startActivity(intent);
                } else {
                    isCreateGroupPressed = false;
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void toGroupMessagingActivity(){
        Intent intent = new Intent(this, GroupMessagingActivity.class);
        startActivity(intent);
    }
    public void toEditProfileActivity(PrivateProfile currentUserProfile){
        Intent intent = new Intent(this, EditProfileActivity.class);
        intent.putExtra("CurrentUserProfile", currentUserProfile);
        startActivity(intent);
    }
    public void toSettingActivity(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void toLoginActivity(){
        userloogedIn = true;
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void goToEditGroup(){
        Intent intent = new Intent(this, EditGroupActivity.class);
        startActivity(intent);
    }

    public void getUserProfile(){
        databaseReference.child("users").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUserProfile = dataSnapshot.child("profile").getValue(PrivateProfile.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}