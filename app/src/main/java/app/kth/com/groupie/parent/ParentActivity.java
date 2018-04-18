package app.kth.com.groupie.parent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import app.kth.com.groupie.CreateGroupActivity;
import app.kth.com.groupie.EditProfileActivity;
import app.kth.com.groupie.R;
import app.kth.com.groupie.SettingsActivity;
import app.kth.com.groupie.groupMessaging.GroupMessagingActivity;
import app.kth.com.groupie.login.LoginActivity;

public class ParentActivity extends AppCompatActivity {
    HomeFragment homeFragment;
    ProfileFragment profileFragment;
    BrowserFragment browserFragment;

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

        homeFragment = new HomeFragment();
        browserFragment = new BrowserFragment();
        profileFragment = new ProfileFragment();

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
                Intent intent = new Intent(this, CreateGroupActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

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
}
