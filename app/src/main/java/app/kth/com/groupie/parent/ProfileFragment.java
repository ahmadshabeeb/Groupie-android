package app.kth.com.groupie.parent;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import app.kth.com.groupie.R;
import app.kth.com.groupie.data.structure.PrivateProfile;


public class ProfileFragment extends Fragment {
    ParentActivity activity;
    private ImageView settingsButton;
    private ImageView profilePicture;
    private Button editProfileButton;
    private TextView schoolName;
    private TextView majorName;
    private TextView defaultLocation;
    private TextView firstName;
    private TextView lastName;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private PrivateProfile currentUserProfile;
    private RecyclerView myGroupHistoryRecycleView;
    private RecyclerView.Adapter myGroupHistoryAdapter;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInsatnceState){
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_profile, container, false);

        profilePicture = (ImageView) rootView.findViewById(R.id.profile_picture);
        editProfileButton = (Button) rootView.findViewById(R.id.to_edit_profile);
        schoolName = (TextView) rootView.findViewById(R.id.profile_school_name);
        majorName = (TextView) rootView.findViewById(R.id.profile_major_name);
        defaultLocation = (TextView) rootView.findViewById(R.id.profile_default_location);
        firstName = (TextView) rootView.findViewById(R.id.profile_first_name);
        lastName = (TextView) rootView.findViewById(R.id.profile_last_name);
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        currentUser = mAuth.getCurrentUser();
        currentUserProfile = activity.currentUserProfile;

        if (currentUserProfile != null) {
            displayProfileValues(currentUserProfile);
        }

        getUserProfile();

        //Button toGroup = (Button) rootView.findViewById(R.id.sign_out);
        //        toGroup.setOnClickListener(new View.OnClickListener() {
        //            public void onClick(View view){
        //                //go to group activity
        //                activity.toGroupMessagingActivity();
        //            }
        //        });
//        Button signOut = (Button) rootView.findViewById(R.id.sign_out);
//        signOut.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view){
//                mAuth.signOut();
//                currentUser = null;
//                startActivity(new Intent(getActivity(), LoginActivity.class));
//            }
//        });

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                //go to group activity
                activity.toEditProfileActivity(currentUserProfile);
            }
        });

//        settingsButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view){
//                //go to group activity
//            }
//        });

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        myGroupHistoryRecycleView = rootView.findViewById(R.id.group_history_recycler_view);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(getActivity());
        myGroupHistoryRecycleView.setLayoutManager(LayoutManager);
        myGroupHistoryAdapter = new GroupHistoryAdapter(getActivity(),progressBar);
        myGroupHistoryRecycleView.setAdapter(myGroupHistoryAdapter);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (ParentActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    public void getUserProfile() {
        databaseReference.child("users").child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUserProfile = dataSnapshot.child("profile").getValue(PrivateProfile.class);
                displayProfileValues(currentUserProfile);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void displayProfileValues(PrivateProfile currentUserProfile){
        schoolName.setText(currentUserProfile.getSchool());
        majorName.setText(currentUserProfile.getFieldOfStudy());
        defaultLocation.setText(currentUserProfile.getStudyLocation());
        firstName.setText(currentUserProfile.getFirstName());
        lastName.setText(currentUserProfile.getLastName());

        if (currentUserProfile.getProfilePicture() != null){
            String urlImage = currentUserProfile.getProfilePicture();
            Glide.with(ProfileFragment.this)
                    .load(urlImage)
                    .into(profilePicture);
        } else {
            profilePicture.setImageResource(R.mipmap.ic_profile);
        }
    }

    public void printBar(String message, View view){
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}