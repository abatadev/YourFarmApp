package com.java.yourfarmapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.java.yourfarmapp.Model.MessagesModel;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {


    private ImageButton sendMessageButton, sendImageButton;
    private EditText userMessageInput;
    private RecyclerView userMessagesList;

    private String messageRecieverID, messageRecieverName, productId;
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

        initializeFields();
        displayReceiverInfo();

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        String messageText = userMessageInput.getText().toString();

        if(TextUtils.isEmpty(messageText)) {
            Toast.makeText(this, "You need to input a message first.", Toast.LENGTH_SHORT).show();
        } else {
            MessagesModel messagesModel;

            String messageSenderReference = "Messages/ " + messageSenderId + "/" + messageRecieverID;
            String messageReceiverReference = "Messages/" + messageRecieverID + "/" + messageSenderId;

            DatabaseReference userRef = rootRef.child("Messages").child(messageSenderId)
                    .child(messageRecieverID).push();

            String message_push_id = userRef.getKey();

            Map messageTextBody = new HashMap();
            messageTextBody.put("message", messageText);
            //messageTextBody.put("time", saveCurrentTime); // TO DO
            //messageTextBody.put("date", saveCurrentDate); // TO DO
            messageTextBody.put("from", messageSenderId);
        }
    }

    private String calendarCurrentDate() {
        Calendar calendarForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM-dd-yyyy");
        String saveCurrentDate = currentDate.format(calendarForDate.getTime());

        return saveCurrentDate;
    }

    private String calendarCurrentTime() {
        Calendar calendarForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        String saveCurrentTime = currentTime.format(calendarForTime.getTime());

        return saveCurrentTime;
    }

    private void displayReceiverInfo() {
        final String TAGS = "displayReceiverInfo";

        messageRecieverID = getIntent().getExtras().get("farmerId").toString();
        messageRecieverName = getIntent().getExtras().get("farmerName").toString();
        productId = getIntent().getExtras().get("productId").toString();
        farmerProfilePic = getIntent().getExtras().get("farmerProfilePictureCircle").toString();

        receiverName.setText(messageRecieverName);
        Glide.with(getApplicationContext()).load(farmerProfilePic).into(receiverProfileImage);

        Log.d("TAGS", "Farmer ID: " + messageRecieverID);
        Log.d("TAGS", "Farmer Name: " + messageRecieverName);
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
    }
}
