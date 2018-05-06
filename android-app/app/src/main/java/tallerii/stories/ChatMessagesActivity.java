package tallerii.stories;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import tallerii.stories.helpers.MessagesAdapter;
import tallerii.stories.network.apimodels.ChatMessage;

public class ChatMessagesActivity extends AppCompatActivity {

    private RecyclerView mChatsRecyclerView;
    private EditText mMessageEditText;
    private DatabaseReference mMessagesDBRef;
    private List<ChatMessage> mMessagesList = new ArrayList<>();

    private String receiverId;
    private String receiverName;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_messages);

        //initialize the views
        mChatsRecyclerView = findViewById(R.id.messagesRecyclerView);
        mMessageEditText = findViewById(R.id.messageEditText);
        mChatsRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setStackFromEnd(true);
        mChatsRecyclerView.setLayoutManager(mLayoutManager);

        //init Firebase
        mMessagesDBRef = FirebaseDatabase.getInstance().getReference().child("Messages");

        //get receiverId from intent
        receiverId = "2";
        currentUserId = "1";
    }

    public void sendMessage(View v) {
        String message = mMessageEditText.getText().toString();
        //TODO use our user id
        //Todo disable button if empty
        if(message.isEmpty()){
            Toast.makeText(ChatMessagesActivity.this, "You must enter a message", Toast.LENGTH_SHORT).show();
        }else {
            //message is entered, send
            sendMessageToFirebase(message, currentUserId, receiverId);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*Query and populate chat messages*/
        queryMessages();
        /*sets title bar with recepient name*/
        queryRecipientName(receiverId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryMessages();
    }

    private void sendMessageToFirebase(String message, String senderId, String receiverId){
        mMessagesList.clear();

        ChatMessage newMsg = new ChatMessage(message, senderId, receiverId);
        mMessagesDBRef.push().setValue(newMsg).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful()){
                    //error
                    Toast.makeText(ChatMessagesActivity.this, "Error " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ChatMessagesActivity.this, "Message sent successfully!", Toast.LENGTH_SHORT).show();
                    mMessageEditText.setText(null);
                    hideSoftKeyboard();
                }
            }
        });


    }

    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            assert inputMethodManager != null;
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void queryMessages(){
        mMessagesDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mMessagesList.clear();

                for(DataSnapshot snap: dataSnapshot.getChildren()){
                    ChatMessage chatMessage = snap.getValue(ChatMessage.class);
                    //TODO get them with a select like query?
                    //TODO this listens to all changes???????? cant we just be notified of necessary? investigate
                    assert chatMessage != null;
                    if(chatMessage.getSenderId().equals(currentUserId) && chatMessage.getReceiverId().equals(receiverId) || chatMessage.getSenderId().equals(receiverId) && chatMessage.getReceiverId().equals(currentUserId)){
                        mMessagesList.add(chatMessage);
                    }

                }
                populateMessagesRecyclerView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //TODO manage cancels?? is this possible?
            }
        });
    }

    private void populateMessagesRecyclerView(){
        MessagesAdapter adapter = new MessagesAdapter(mMessagesList, this);
        mChatsRecyclerView.setAdapter(adapter);
    }

    private void queryRecipientName(final String receiverId){
        //TODO get and set receiver name
        receiverName = "Guido puto";
    }
}
