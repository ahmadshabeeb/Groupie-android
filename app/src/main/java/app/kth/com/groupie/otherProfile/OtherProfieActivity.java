package app.kth.com.groupie.otherProfile;

import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import app.kth.com.groupie.R;
import app.kth.com.groupie.data.structure.Profile;

public class OtherProfieActivity extends AppCompatActivity {

    Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profie);

        //profile = GET FROM INTENT

        profile = getFakeProfile();
        initTextviews();

    }

    private Profile getFakeProfile(){
        Profile fake = new Profile();
        fake.setFirstName("Daniel");
        fake.setLastName("Muresu");
        fake.setSchool("Kungliga Tekniska Högskolan");
        fake.setBio("I like to study math and programming and stuff like that and more stuff and more stuff and more stuff and more stuff and more stuff");
        fake.setFieldOfStudy("ICT");
        return fake;
    }

    private void initTextviews(){
        TextView firstname = (TextView) findViewById(R.id.profilename_textview);
        firstname.setText(profile.getFirstName());

        TextView lastname = (TextView) findViewById(R.id.lastname_textview);
        lastname.setText(profile.getLastName());

        TextView school = (TextView) findViewById(R.id.profileschool_textview);
        school.setText(profile.getSchool());

        TextView bio = (TextView) findViewById(R.id.profilebio_textview);
        bio.setText(profile.getBio());

        TextView field = (TextView) findViewById(R.id.profilefield_textview);
        field.setText(profile.getFieldOfStudy());

        ImageView report = (ImageView) findViewById(R.id.reportuser_imageview);
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(getApplicationContext(), "User Reported", Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }
}
