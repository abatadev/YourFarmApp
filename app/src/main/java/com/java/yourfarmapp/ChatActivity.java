package com.java.yourfarmapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.java.yourfarmapp.Adapter.MessagesAdapter;
import com.java.yourfarmapp.Model.MessagesModel;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {


    private ImageButton sendMessageButton, sendImageButton;
    private EditText userMessageInput;
    RecyclerView userMessagesList;
    private final List<MessagesModel> messagesModelList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessagesAdapter messagesAdapter;

    private String messageReceiverID, messageReceiverName, productId;
    private String messageSenderId;
    private String farmerProfilePic;

    private TextView receiverName;
    private CircleImageView receiverProfileImage;

    DatabaseReference rootRef;
    DatabaseReference userRef;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mAuth = FirebaseAuth.getInstance();
        messageSenderId = mAuth.getCurrentUser().getUid().toString();
        messageReceiverID = getIntent().getExtras().get("farmerId").toString();

        initializeFields();
        displayReceiverInfo();

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        fetchMessages();
    }

    private void fetchMessages() {
        rootRef = FirebaseDatabase.getInstance().getReference();
        messageSenderId = mAuth.getCurrentUser().getUid().toString();
        messageReceiverID = getIntent().getExtras().get("farmerId").toString();

        rootRef.child("Messages")
                .child(messageSenderId)
                .child(messageReceiverID)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        if(dataSnapshot.exists()) {
                            MessagesModel message = dataSnapshot.getValue(MessagesModel.class);
                            messagesModelList.add(message);
                            messagesAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(ChatActivity.this, "No existing chat.", Toast.LENGTH_SHORT).show();
                            Log.d("testing", "Sender ID: " + messageSenderId);
                            Log.d("testing", "Receiver ID: " + messageReceiverID);
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void sendMessage() {
        rootRef = FirebaseDatabase.getInstance().getReference();
        messageReceiverID = getIntent().getExtras().get("farmerId").toString();
        messageSenderId = mAuth.getCurrentUser().getUid().toString();

        String messageText = userMessageInput.getText().toString();

        if(TextUtils.isEmpty(messageText)) {
            Toast.makeText(this, "You need to input a message first.", Toast.LENGTH_SHORT).show();
        } else {
            MessagesModel messagesModel;

            String messageSenderReference = "Messages/ " + messageSenderId + "/" + messageReceiverID;
            String messageReceiverReference = "Messages/" + messageReceiverID + "/" + messageSenderId;

            DatabaseReference userRef = rootRef.child("Messages").child(messageSenderId)
                    .child(messageReceiverID).push();

            String message_push_id = userRef.getKey();

            Calendar calendarForDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("MM-dd-yyyy");
            String saveCurrentDate = currentDate.format(calendarForDate.getTime());

            Calendar calendarForTime = Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
            String saveCurrentTime = currentTime.format(calendarForTime.getTime());


            Map messageTextBody = new HashMap();
            messageTextBody.put("message", messageText);
            messageTextBody.put("time", saveCurrentTime); // TO DO
            messageTextBody.put("date", saveCurrentDate); // TO DO
            messageTextBody.put("from", messageSenderId);
            messageTextBody.put("type", "text");
            messageTextBody.put("to", messageReceiverID);

            Map messageBodyDetails = new HashMap();
            messageBodyDetails.put(messageSenderReference + "/" + message_push_id , messageTextBody);
            messageBodyDetails.put(messageReceiverReference + "/" + message_push_id , messageTextBody);

            rootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()) {
                        Toast.makeText(ChatActivity.this, "Sent", Toast.LENGTH_SHORT).show();
                        userMessageInput.setText("");
                    } else {
                        String message = task.getException().getMessage();
                        Toast.makeText(ChatActivity.this, message, Toast.LENGTH_SHORT).show();
                        userMessageInput.setText("");
                    }

                }
            });
        }
    }


    private void displayReceiverInfo() {
        final String TAGS = "displayReceiverInfo";

        messageReceiverID = getIntent().getExtras().get("farmerId").toString();
        messageReceiverName = getIntent().getExtras().get("farmerName").toString();
        productId = getIntent().getExtras().get("productId").toString();
        farmerProfilePic = getIntent().getExtras().get("farmerProfilePictureCircle").toString();

        receiverName.setText(messageReceiverName);
        Glide.with(getApplicationContext()).load(farmerProfilePic).into(receiverProfileImage);

        Log.d("TAGS", "Farmer ID: " + messageReceiverID);
        Log.d("TAGS", "Farmer Name: " + messageReceiverName);
        Log.d("TAGS", "Crop Product ID: " + productId);
        Log.d("TAGS", "Farmer Profile Pic URL: " + farmerProfilePic);


        FirebaseDatabase.getInstance().getReference().child("Product").child(productId)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {

                } else {
                    Toast.makeText(ChatActivity.this, "Some things are null.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initializeFields() {
        sendMessageButton = findViewById(R.id.send_message_button);
        sendImageButton = findViewById(R.id.send_image_file);
        userMessageInput = findViewById(R.id.send_message);

        receiverName = findViewById(R.id.custom_profile_name);
        receiverProfileImage = findViewById(R.id.custom_profile_picture_circle);

        messagesAdapter = new MessagesAdapter(messagesModelList);
        userMessagesList = (RecyclerView) findViewById(R.id.messages_list_users);
        linearLayoutManager = new LinearLayoutManager(this);

        userMessagesList.setHasFixedSize(true);
        userMessagesList.setLayoutManager(linearLayoutManager);
        userMessagesList.setAdapter(messagesAdapter);
    }
}
