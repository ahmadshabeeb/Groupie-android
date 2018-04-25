package app.kth.com.groupie.parent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import app.kth.com.groupie.CreateGroupActivity;
import app.kth.com.groupie.EditProfileActivity;
import app.kth.com.groupie.R;
import app.kth.com.groupie.SettingsActivity;
import app.kth.com.groupie.data.Group;
import app.kth.com.groupie.firstLogin.FirstLoginActivity;
import app.kth.com.groupie.groupMessaging.GroupMessagingActivity;
import app.kth.com.groupie.login.LoginActivity;
import app.kth.com.groupie.registration.RegistrationActivity;

public class ParentActivity extends AppCompatActivity {
    HomeFragment homeFragment;
    ProfileFragment profileFragment;
    BrowserFragment browserFragment;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
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
    public void onStart(){
        super.onStart();
        //Check if user already signed in and update UI
        currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public void updateUI(FirebaseUser currentUser){
        if (currentUser == null){
            toLoginActivity();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);
        homeFragment = new HomeFragment();
        browserFragment = new BrowserFragment();
        profileFragment = new ProfileFragment();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
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
//                Intent intent = new Intent(this, CreateGroupActivity.class);
//                startActivity(intent);

                createGroup();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void createGroup() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        Group group = new Group();
        group.setSubject("Math");
        group.setTopic("Calculus");
        group.setDescription("In the library");
        group.setDateOfMeeting("20-04-2018");
        group.setTimeOfCreation("19-04-18");
        group.setHasMeetingDate(true);
        group.setLocation("KTH kista");
        group.setMaxNumberOfMembers(8);
        group.setOwner(mAuth.getUid());
        group.setPublic(true);

        HashMap<String, Boolean> members = new HashMap<>();
        members.put(mAuth.getUid(), true);
        group.setMembers(members);

        String groupKey = db.child("groups").push().getKey();
        db.child("groups/" + groupKey).setValue(group);
    }

    public void signOut(){
        mAuth.signOut();
        onStart();
    }

    public void toGroupMessagingActivity(){
        Intent intent = new Intent(this, GroupMessagingActivity.class);
        startActivity(intent);
    }
    public void toEditProfileActivity(){
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }
    public void toSettingActivity(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void toLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void toRegistrationActivity(){
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

}
