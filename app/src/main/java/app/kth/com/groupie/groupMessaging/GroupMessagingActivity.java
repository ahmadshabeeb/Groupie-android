package app.kth.com.groupie.groupMessaging;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import app.kth.com.groupie.data.Group;
import app.kth.com.groupie.data.structure.Message;
import app.kth.com.groupie.data.structure.Profile;
import app.kth.com.groupie.R;
import app.kth.com.groupie.editGroup.EditGroupActivity;
import app.kth.com.groupie.otherProfile.OtherProfieActivity;
import app.kth.com.groupie.parent.ParentActivity;
import app.kth.com.groupie.utilities.Utility;
import de.hdodenhof.circleimageview.CircleImageView;

public class GroupMessagingActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static class ProfileViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout mNavDrawerRelativeLayout;
        CircleImageView mNavProfilePictureImageView;
        TextView mProfileNameTextView;

        public ProfileViewHolder(View itemView) {
            super(itemView);
            mNavDrawerRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.navDrawerRelativeLayout);
            mNavProfilePictureImageView = (CircleImageView) itemView.findViewById(R.id.navProfilePictureImageView);
            mProfileNameTextView = (TextView) itemView.findViewById(R.id.navProfileNameTextView);
        }
    }

    public static class messageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView profilePictureImageView;
        TextView messageItemReceivedTextView;
        TextView senderTextView;
        ImageView messageItemImageView;
        RelativeLayout messageItemLayout;
        TextView messageItemSentTextView;
        TextView notificationMessageTextView;

        public messageViewHolder(View v) {
            super(v);
            senderTextView = (TextView) itemView.findViewById(R.id.senderTextView);
            messageItemReceivedTextView = (TextView) itemView.findViewById(R.id.messageItemReceivedTextView);
            profilePictureImageView = (CircleImageView) itemView.findViewById(R.id.profilePictureImageView);
            messageItemImageView = (ImageView) itemView.findViewById(R.id.messageReceivedImageView);
            messageItemLayout = (RelativeLayout) itemView.findViewById(R.id.messageItemLayout);
            messageItemSentTextView = (TextView) itemView.findViewById(R.id.messageItemSentTextView);
            notificationMessageTextView = (TextView) itemView.findViewById(R.id.notificationMessageTextView);
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick called.");
        }
    }

    private static final String TAG = "LogMainActivity";
    private FirebaseUser mCurrentUser;

    private Button mSendButton;
    private Button mProfilePictureButton;
    private RecyclerView mMessageRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private final String NOTIFICATION_MESSAGE_ID = "notification";
    private final String CHILD_CONVERSATIONS = "conversations";
    private final String CHILD_MESSAGES = "messages";
    private final String CHILD_GROUPS = "groups";
    private final String CHILD_USERS = "users";
    private String mConversationId;
    private Group mGroup;
    private String mGroupId;
    private ArrayList<Profile> mMemberProfiles;
    private DatabaseReference mGroupConversationRef;


    private TextView mGroupNotificationTextView;
    private EditText mMessageEditText;
    private TextView mEditableSubjectTextView;
    private TextView mEditableTopicTextView;
    private TextView mEditableLocationTextView;
    private TextView mEditableDayTextView;
    private TextView mEditableDescriptionTextView;
    private Button mLeaveGroupBtn;
    private Button mEditGroupBtn;
    private DrawerLayout mDrawerLayout;

    private RecyclerView mProfileRecyclerView;

    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<Message, messageViewHolder> mFirebaseAdapter;
    private boolean isFirst =true;

    private boolean userIsSender(Message msg) {
        Log.d(TAG, "userIsSender called");
        if (mCurrentUser!=null &&
                mCurrentUser.getUid().equals(msg.getSenderUserId())) return true; // change when get updated classes
        return false;
    }

    private boolean isNotificationMsg(Message msg) {
        Log.d(TAG, "isNotificationMsg called.");
        if (msg.getSenderUserId().equals(NOTIFICATION_MESSAGE_ID)){
            return true;
        }
        return false;
    }

    private void goToParentActivity() {
        Intent intent = new Intent(this, ParentActivity.class);
        startActivity(intent);
    }

    private void initDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void updateGroup() {
        mFirebaseDatabaseReference.child(CHILD_GROUPS).child(mGroupId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mGroup = dataSnapshot.getValue(Group.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void updateGroupInfo() {
        getGroupMembers();
        mEditGroupBtn.setVisibility(View.GONE);
        if (mGroup.getOwner().equals(mCurrentUser.getUid())) {
            if (!mGroup.getIsPublic()) {
                mGroupNotificationTextView.bringToFront();
                mGroupNotificationTextView.setVisibility(View.VISIBLE);
                mGroupNotificationTextView.setText("Private - this group is no longer accepting new members.");
            } else {
                mGroupNotificationTextView.setVisibility(View.GONE);
            }
            mEditGroupBtn.setVisibility(View.VISIBLE);
        }
        mEditableSubjectTextView.setText(mGroup.getSubject());
        mEditableTopicTextView.setText(mGroup.getTopic());
        mEditableLocationTextView.setText(mGroup.getLocation());
        mEditableDayTextView.setText(mGroup.getDateOfMeeting());
        mEditableDescriptionTextView.setText(mGroup.getDescription());
    }

    private void initialize() {
        mMessageRecyclerView = (RecyclerView) findViewById(R.id.messageRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
        mProfileRecyclerView = (RecyclerView) findViewById(R.id.nav_drawer_recycler_view);
        mProfileRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mGroupNotificationTextView = (TextView) findViewById(R.id.privateGroupNotificationTextView);
        mEditableSubjectTextView = (TextView) findViewById(R.id.editableSubjectTextView);
        mEditableTopicTextView = (TextView) findViewById(R.id.editableTopicTextView);
        mEditableLocationTextView = (TextView) findViewById(R.id.editableLocationTextView);
        mEditableDayTextView = (TextView) findViewById(R.id.editableDayTextView);
        mEditableDescriptionTextView = (TextView) findViewById(R.id.editableDescriptionTextView);
        mSendButton = (Button) findViewById(R.id.sendButton);
        mLeaveGroupBtn = (Button) findViewById(R.id.leaveGroupButton);
        mEditGroupBtn =(Button) findViewById(R.id.editGroupButton);
        mMessageEditText = (EditText) findViewById(R.id.messageEditText);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    }

    private boolean containsProfile(ArrayList<Profile> profiles, Profile profile) {
        int i=0;
        for (Profile pro: profiles) {
            if (profiles.get(i++).getUserId().equals(pro.getUserId())) return true;
        }
        return false;
    }

    private ArrayList<Profile> getGroupMembers() {
//        int j=0;
//        ArrayList<String> currentMemberIds = new ArrayList<String>();
//        for (Profile profile: mMemberProfiles) {
//            currentMemberIds.add(mMemberProfiles.get(j).getUserId());
//            j++;
//        }
        mMemberProfiles.clear();
        for (String userId: mGroup.getMembers().keySet()) {
//            if (!currentMemberIds.contains(userId)) {
                Log.d(TAG, userId + " user IDDDD");
                Utility.callCloudFunctions("dbUsersProfileGetPublic", userId)
                        .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                if (!task.isSuccessful()) {
                                    Exception e = task.getException();
                                    if (e instanceof FirebaseFunctionsException) {
                                        FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                                        FirebaseFunctionsException.Code code = ffe.getCode();
                                        String message = ffe.getMessage();
                                        Log.d(TAG, "ERROR CODE: " + code + " ... " + message);
                                    }
                                    Log.w(TAG, "onFailure", e);
                                } else {
                                    String result = task.getResult();
                                    Log.d(TAG, "profile result as a string: " + result);
                                    Gson gson = new Gson();
                                    Profile profile = gson.fromJson(result, Profile.class);
                                    mMemberProfiles.add(profile);
                                }
                            }
                        });
            //}
        }
        return new ArrayList<Profile>();
    }

    private int getProfileIndexFromMsg(Message msg) {
        int i=0;
        for (Profile profile : mMemberProfiles) {
            if (profile.getUserId().equals(msg.getSenderUserId())) return i;
            i++;
        }
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Intent intent = getIntent();
        mGroup = (Group) intent.getParcelableExtra("group");
        mGroupId = mGroup.getGroupId();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_messaging);

        initDrawer();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initialize();
        mMemberProfiles = new ArrayList<Profile>();
        getGroupMembers();

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference conversationsRef = mFirebaseDatabaseReference.child(CHILD_CONVERSATIONS);
        final DatabaseReference groupsRef = mFirebaseDatabaseReference.child(CHILD_GROUPS);

        mConversationId = mGroup.getConversationId();

        mEditGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "edit group button clicked. group id: "+mGroupId);
                goToEditGroupActivity();
            }
            });


        mGroupConversationRef = conversationsRef.child(mConversationId);

        SnapshotParser<Message> parser = new SnapshotParser<Message>() {
            @Override
            public Message parseSnapshot(DataSnapshot snapshot) {
                Log.d(TAG, "parseSnapshot called.");
                Message msg = snapshot.getValue(Message.class);
                return msg;
            }
        };

        FirebaseRecyclerOptions<Message> options =
                new FirebaseRecyclerOptions.Builder<Message>()
                        .setQuery(mGroupConversationRef.child(CHILD_MESSAGES), parser)
                        .build();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Message, messageViewHolder>(options) {
            @NonNull
            @Override
            public messageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                Log.d(TAG, "messageViewHolder created - means user is not the sender");
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
                return new messageViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(messageViewHolder holder, int position, Message msg) {
                Log.d(TAG, "onBindViewHolder called.");
                if (isNotificationMsg(msg)) {
                    Log.d(TAG, "isNotificationMsg if statement triggered1111111");
                    updateGroup();
//                    mViewHolder.setClickable(false);
                    holder.messageItemSentTextView.setVisibility(View.GONE);
                    holder.notificationMessageTextView.setVisibility(View.VISIBLE);
                    holder.senderTextView.setVisibility(View.GONE);
                    holder.profilePictureImageView.setVisibility(View.GONE);
                    holder.messageItemReceivedTextView.setVisibility(View.GONE);
                    holder.notificationMessageTextView.setText(msg.getName() + " " + msg.getText());
                } else {
                    if (userIsSender(msg)) {
//                        holder.setClickable(false);
                        holder.messageItemReceivedTextView.setVisibility(View.GONE);
                        holder.senderTextView.setVisibility(View.GONE);
                        holder.profilePictureImageView.setVisibility(View.GONE);
                        holder.notificationMessageTextView.setVisibility(View.GONE);
                        holder.messageItemSentTextView.setVisibility(View.VISIBLE);
                        holder.messageItemSentTextView.setText(msg.getText());
                    } else {
//                        mViewHolder.setClickable(true);
                        holder.messageItemSentTextView.setVisibility(View.GONE);
                        holder.notificationMessageTextView.setVisibility(View.GONE);
                        holder.senderTextView.setVisibility(View.VISIBLE);
                        holder.profilePictureImageView.setVisibility(View.VISIBLE);
                        holder.messageItemReceivedTextView.setVisibility(View.VISIBLE);
                        holder.messageItemReceivedTextView.setText(msg.getText());
                        if (mMemberProfiles.size() == mGroup.getNumberOfMembers()) {
                            int profileIndex = getProfileIndexFromMsg(msg);
                            holder.profilePictureImageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    goToOtherProfile(mMemberProfiles.get(profileIndex));
                                }
                            });

                            holder.senderTextView.setText(mMemberProfiles.get(profileIndex).getFirstName());
                            if (mCurrentUser.getPhotoUrl() != null){
                                holder.profilePictureImageView.setImageURI(Uri.parse(msg.getImageUrl()));
                            }
                        }
                    }
                }
            }
        };

        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int messageCount = mFirebaseAdapter.getItemCount();
                int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (messageCount-1) && lastVisiblePosition == (positionStart-1)) ) {
                    mMessageRecyclerView.scrollToPosition(positionStart);
                }
            }
        });
        mMessageRecyclerView.setAdapter(mFirebaseAdapter);

        class ProfileRecyclerViewAdapter extends RecyclerView.Adapter<ProfileViewHolder> {
            private ArrayList<Profile> profileCards;
            private Context context;

            private ProfileRecyclerViewAdapter(Context context, ArrayList<Profile> profileCards) {
                this.profileCards = profileCards;
                this.context = context;
                Log.d(TAG, "Adaptor");
            }

            @NonNull
            @Override
            public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View viewHolder = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.profile_item, parent, false);

                return new ProfileViewHolder(viewHolder);
            }

            @Override
            public void onBindViewHolder(ProfileViewHolder holder, int position) {
                Profile profile = profileCards.get(position);
                holder.mProfileNameTextView.setText(profile.getFirstName());
                holder.mNavDrawerRelativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "lastname." + profile.getLastName());
                        goToOtherProfile(profile);
                    }
                });
            }

            @Override
            public int getItemCount() {
                return this.profileCards.size();
            }
        }

        mProfileRecyclerView.setAdapter(new ProfileRecyclerViewAdapter(this, mMemberProfiles));

        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(140)});
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {}
            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                Log.d(TAG, "onDrawerOpened called.");
                updateGroupInfo();
            }
            @Override
            public void onDrawerClosed(@NonNull View drawerView) {}
            @Override
            public void onDrawerStateChanged(int newState) {}
        });

        mLeaveGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cloud function
                Log.d(TAG,"LEAVE GROUP");
                Utility.callCloudFunctions("dbGroupsLeave", mGroupId);
                goToParentActivity();
            }
        });
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg = new Message();
                msg.setText(mMessageEditText.getText().toString());
                msg.setSenderUserId(mCurrentUser.getUid());
                // use cloud function to fill in rest of sender data
                mGroupConversationRef.child(CHILD_MESSAGES)
                        .push().setValue(msg);
                mMessageEditText.setText("");
            }
        });
        Log.d(TAG, "end.");

    }

    private void goToOtherProfile(Profile profile) {
        Intent intent = new Intent(this, OtherProfieActivity.class);
        intent.putExtra("profile", profile);
        startActivity(intent);
    }

    private void goToEditGroupActivity() {
        Intent intent = new Intent(this, EditGroupActivity.class);
        intent.putExtra("group", mGroup);
        intent.putExtra("groupId", mGroupId);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
            mDrawerLayout.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.group_messaging, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            mDrawerLayout.openDrawer(GravityCompat.END);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        mDrawerLayout.closeDrawer(GravityCompat.END);
        return true;
    }


    @Override
    public void onPause() {
        mFirebaseAdapter.stopListening();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mFirebaseAdapter.startListening();
    }
}
