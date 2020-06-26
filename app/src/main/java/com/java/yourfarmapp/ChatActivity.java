package com.java.yourfarmapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {


    private ImageButton sendMessageButton, sendImageButton;
    private EditText userMessageInput;
    private RecyclerView userMessagesList;

    private String messageRecieverID, messageRecieverName, productId;
    private String farmerProfilePic;

    private TextView receiverName;
    private CircleImageView receiverProfileImage;


    DatabaseReference rootRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        initializeFields();
        displayReceiverInfo();
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
