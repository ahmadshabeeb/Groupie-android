package app.kth.com.groupie.groupMessaging;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
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
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import app.kth.com.groupie.Data.Structure.Message;
import app.kth.com.groupie.R;
//import de.hdodenhof.circleimageview.CircleImageView;

public class GroupMessagingActivity extends AppCompatActivity {

    public static class receivedMessageViewHolder extends RecyclerView.ViewHolder {
  //      CircleImageView profilePictureImageView;
        TextView messageItemReceivedTextView;
        TextView senderTextView;
        ImageView messageReceivedImageView;
        RelativeLayout messageItemLayout;

        public receivedMessageViewHolder(View v) {
            super(v);
            senderTextView = (TextView) itemView.findViewById(R.id.senderTextView);
            messageItemReceivedTextView = (TextView) itemView.findViewById(R.id.messageItemReceivedTextView);
       //     profilePictureImageView = (CircleImageView) itemView.findViewById(R.id.profilePictureImageView);
            messageReceivedImageView = (ImageView) itemView.findViewById(R.id.messageReceivedImageView);
            messageItemLayout = (RelativeLayout) itemView.findViewById(R.id.messageItemLayout);
        }

    }

    public static class sentMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageItemSentTextView;
        ImageView messageSentImageView;
        RelativeLayout messageItemLayout;

        public sentMessageViewHolder(View v) {
            super(v);
            messageItemSentTextView = (TextView) itemView.findViewById(R.id.messageItemSentTextView);
            messageSentImageView = (ImageView) itemView.findViewById(R.id.messageSentImageView);
            messageItemLayout = (RelativeLayout) itemView.findViewById(R.id.messageItemLayout);
        }

    }

    private static final String TAG = "LogMainActivity";
    private boolean mUserIsSender;
    private FirebaseUser mCurrentUser;

    private Button mSendButton;
    private Button mProfilePictureButton;
    private RecyclerView mMessageRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private final String CHILD_MESSAGES = "messages";
    private EditText mMessageEditText;
    private ConstraintLayout mConstraintLayout = findViewById(R.id.constraintLayout);

    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<Message, RecyclerView.ViewHolder> mFirebaseAdapter;


    private boolean userIsSender(Message msg) {
        Log.d(TAG, "userIsSender called");
        if (mCurrentUser.getUid().equals(msg.getSenderUserId())) return true; // change when get updated classes
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_messaging);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();


        Log.d(TAG, "onCreate called.");
        mMessageRecyclerView = (RecyclerView) findViewById(R.id.messageRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        SnapshotParser<Message> parser = new SnapshotParser<Message>() {
            @Override
            public Message parseSnapshot(DataSnapshot snapshot) {
                Log.d(TAG, "parseSnapshot called.");
                Message msg = snapshot.getValue(Message.class);
                if (userIsSender(msg)) {
                    mUserIsSender = true;
                } else {
                    mUserIsSender = false;
                }
                return msg;
            }
        };

        DatabaseReference messagesDatabaseRef = mFirebaseDatabaseReference.child(CHILD_MESSAGES);
        FirebaseRecyclerOptions<Message> options =
                new FirebaseRecyclerOptions.Builder<Message>()
                        .setQuery(messagesDatabaseRef, parser)
                        .build();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Message, RecyclerView.ViewHolder>(options) {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                if (mUserIsSender) {
                    Log.d(TAG, "sentMessageViewHolder created - means user is the sender");
                    return new sentMessageViewHolder(inflater.inflate(R.layout.message_item_sent, parent, false));
                }
                else{
                    Log.d(TAG, "receivedMessageViewHolder created - means user is not the sender");
                    return new receivedMessageViewHolder(inflater.inflate(R.layout.message_item_received, parent, false));
                }
            }
            @Override
            protected void onBindViewHolder(RecyclerView.ViewHolder holder, int position, Message msg) {
                Log.d(TAG, "onBindViewHolder called.");
                if (mUserIsSender) {
                    ((sentMessageViewHolder) holder).messageItemSentTextView.setText(msg.getText());
                } else {
                    ((receivedMessageViewHolder) holder).messageItemReceivedTextView.setText(msg.getText());
                    // ((receivedMessageViewHolder) holder).profilePictureImageView.setImageURI(Uri.parse(msg.getImageUrl()));
                    ((receivedMessageViewHolder) holder).senderTextView.setText(msg.getSenderUserId()); // for now puts sender ID as the name
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










