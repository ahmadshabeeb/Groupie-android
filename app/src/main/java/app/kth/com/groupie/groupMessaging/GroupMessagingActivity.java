package app.kth.com.groupie.groupMessaging;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import app.kth.com.groupie.Data.Group;
import app.kth.com.groupie.Data.Structure.Message;
import app.kth.com.groupie.Data.Structure.Profile;
import app.kth.com.groupie.R;
import app.kth.com.groupie.parent.ParentActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class GroupMessagingActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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
    private String mConversationId;
    private String mGroupId;
    private Group mGroup;
    private DatabaseReference mGroupConversationRef;
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick called.");
            goToParentActivity();
        }
    };
    private View mViewHolder;
    private TextView mGroupNotificationTextView;
    private EditText mMessageEditText;

    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<Message, messageViewHolder> mFirebaseAdapter;


    private Group genInitGroup() {
        return new Group("","","","",new ArrayList<Profile>(),
                0, 0, null, null,
                "", true, null, true, "");
    }
    private boolean userIsSender(Message msg) {
        Log.d(TAG, "userIsSender called");
        if (mCurrentUser!=null &&
                mCurrentUser.getUid().equals(msg.getSenderUserId())) return true; // change when get updated classes
        return false;
    }

    private boolean isNotificationMsg(Message msg) {
        Log.d(TAG, "isNotificationMsg called.");
        if (msg.getSenderUserId()!=null &&
                msg.getSenderUserId().equals(NOTIFICATION_MESSAGE_ID)) return true;
        return false;
    }
    private void goToParentActivity() {
        Intent intent = new Intent(this, ParentActivity.class);
        startActivity(intent);
    }

    private DatabaseReference getMessageRef(DataSnapshot snapshot) {
        return snapshot.getRef().child(CHILD_MESSAGES);
    }

    private void initDrawer() {
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

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
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mGroup = dataSnapshot.getValue(Group.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_messaging);
        initDrawer();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Intent intent = getIntent();
        //mGroupId = intent.getStringExtra("groupId");

        mGroupId = "-LAmufsgVQ7QI5m7UWZp";

        Log.d(TAG, "onCreate called.");
        mMessageRecyclerView = (RecyclerView) findViewById(R.id.messageRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference conversationsRef = mFirebaseDatabaseReference.child(CHILD_CONVERSATIONS);
        final DatabaseReference groupsRef = mFirebaseDatabaseReference.child(CHILD_GROUPS);

        mGroup = genInitGroup();
//        mConversationId = "-LAmugAzwSrEnmylD1V_";
        mConversationId = mGroup.getConversationId();

        mGroupNotificationTextView = (TextView) findViewById(R.id.privateGroupNotificationTextView);

        if (mGroup.getOwner()
                //.equals(mCurrentUser.getUid()
                .equals("")
                ) {
            mGroupNotificationTextView.bringToFront();
            mGroupNotificationTextView.setVisibility(View.VISIBLE);
            mGroupNotificationTextView.setText("Private - this group is no longer accepting new members.");
        } else {
            mGroupNotificationTextView.setVisibility(View.GONE);
        }

        if (conversationsRef.child(mConversationId)!= null) {
            mGroupConversationRef = conversationsRef.child(mConversationId);
        } else {
            // edit
            mGroupConversationRef = mFirebaseDatabaseReference.child(CHILD_MESSAGES);
            goToParentActivity();
        }

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
                mViewHolder = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
                mViewHolder.findViewById(R.id.profilePictureImageView).setOnClickListener(mOnClickListener);
                return new messageViewHolder(mViewHolder);
            }
            @Override
            protected void onBindViewHolder(messageViewHolder holder, int position, Message msg) {
                Log.d(TAG, "onBindViewHolder called.");
                if (isNotificationMsg(msg)) {
                    updateGroup();
                    mViewHolder.setClickable(false);
                    holder.messageItemReceivedTextView.setVisibility(View.GONE);
                    holder.senderTextView.setVisibility(View.GONE);
                    holder.profilePictureImageView.setVisibility(View.GONE);
                    holder.messageItemSentTextView.setVisibility(View.GONE);
                    holder.notificationMessageTextView.setText(msg.getName() + " " + msg.getText());
                } else {
                    if (userIsSender(msg)) {
                        mViewHolder.setClickable(false);
                        holder.messageItemReceivedTextView.setVisibility(View.GONE);
                        holder.senderTextView.setVisibility(View.GONE);
                        holder.profilePictureImageView.setVisibility(View.GONE);
                        holder.messageItemSentTextView.setText(msg.getText());
                    } else {
                        mViewHolder.setClickable(true);
                        holder.messageItemSentTextView.setVisibility(View.GONE);
                        holder.messageItemReceivedTextView.setText(msg.getText());
//                    holder.senderTextView.setText(msg.getName());
//                    holder.profilePictureImageView.setImageURI(Uri.parse(msg.getImageUrl()));
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

        mMessageEditText = (EditText) findViewById(R.id.messageEditText);
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

        mSendButton = (Button) findViewById(R.id.sendButton);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg = new Message();
                msg.setText(mMessageEditText.getText().toString());
                mGroupConversationRef.child(CHILD_MESSAGES)
                        .push().setValue(msg);
                mMessageEditText.setText("");
            }
        });
        Log.d(TAG, "end.");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            drawer.openDrawer(GravityCompat.END);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
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
