package app.kth.com.groupie.groupMessaging;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import app.kth.com.groupie.Data.Structure.Message;
import app.kth.com.groupie.EditProfileActivity;
import app.kth.com.groupie.R;
import app.kth.com.groupie.parent.ParentActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import app.kth.com.groupie.R;

public class GroupMessagingActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ParentActivity activity;

    public static class messageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView profilePictureImageView;
        TextView messageItemReceivedTextView;
        TextView senderTextView;
        ImageView messageItemImageView;
        RelativeLayout messageItemLayout;
        TextView messageItemSentTextView;

        public messageViewHolder(View v) {
            super(v);
            senderTextView = (TextView) itemView.findViewById(R.id.senderTextView);
            messageItemReceivedTextView = (TextView) itemView.findViewById(R.id.messageItemReceivedTextView);
            profilePictureImageView = (CircleImageView) itemView.findViewById(R.id.profilePictureImageView);
            messageItemImageView = (ImageView) itemView.findViewById(R.id.messageReceivedImageView);
            messageItemLayout = (RelativeLayout) itemView.findViewById(R.id.messageItemLayout);
            messageItemSentTextView = (TextView) itemView.findViewById(R.id.messageItemSentTextView);
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
    private final String CHILD_CONVERSATIONS = "conversations";
    private final String CHILD_MESSAGES = "messages";
    private final String CHILD_USERS = "users";
    private String mConversationId;
    private DataSnapshot mConversationSnapshot;
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick called.");
            goToParentActivity();
        }
    };
    private View mViewHolder;

    private EditText mMessageEditText;

    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<Message, messageViewHolder> mFirebaseAdapter;


    private boolean userIsSender(Message msg) {
        Log.d(TAG, "userIsSender called");
        if (mCurrentUser!=null &&
                mCurrentUser.getUid().equals(msg.getSenderUserId())) return true; // change when get updated classes
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_messaging);
        initDrawer();

        // what to do if user comes to group messaging from the create group screen.
        Intent intent = getIntent();
        mConversationId = intent.getStringExtra("conversationId");

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        Log.d(TAG, "onCreate called.");
        mMessageRecyclerView = (RecyclerView) findViewById(R.id.messageRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
//        final DatabaseReference conversationsRef = mFirebaseDatabaseReference.child(CHILD_CONVERSATIONS);
//        conversationsRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Iterable<DataSnapshot> conversations = dataSnapshot.getChildren();
//                for (DataSnapshot conversationSnapshot: conversations) {
//                    if (conversationSnapshot.getKey().equals(mConversationId)) {
//                        mConversationSnapshot = conversationSnapshot;
//                    } else {
//                        goToParentActivity();
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {}
//        });

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
                        //               .setQuery(getMessageRef(mConversationSnapshot), parser)
                        .setQuery(mFirebaseDatabaseReference.child(CHILD_MESSAGES), parser)
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
                if (userIsSender(msg)) {
                    mViewHolder.setClickable(false);
                    holder.messageItemReceivedTextView.setVisibility(View.GONE);
                    holder.senderTextView.setVisibility(View.GONE);
                    holder.profilePictureImageView.setVisibility(View.GONE);
                    ((messageViewHolder) holder).messageItemSentTextView.setText(msg.getText());
                } else {
                    mViewHolder.setClickable(true);
                    holder.messageItemSentTextView.setVisibility(View.GONE);
                    ((messageViewHolder) holder).messageItemReceivedTextView.setText(msg.getText());
//                    ((messageViewHolder) holder).senderTextView.setText(msg.getName());
//                    ((messageViewHolder) holder).profilePictureImageView.setImageURI(Uri.parse(msg.getImageUrl()));
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
                mFirebaseDatabaseReference.child(CHILD_MESSAGES)
                        .push().setValue(msg);
                mMessageEditText.setText("");
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
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
