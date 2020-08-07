package com.java.yourfarmapp.ui.chat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.java.yourfarmapp.Model.ChatModel;
import com.java.yourfarmapp.Model.OrderModel;
import com.java.yourfarmapp.Model.UserModel;
import com.java.yourfarmapp.R;
import com.java.yourfarmapp.SignInActivity;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    // TODO Implement Chat RecyclerView, ChatSend, ChatReceiver

    // Nav bar
    private TextView navProfileName;
    private CircleImageView navImageView;

    // Product Card View
    private CardView productCardView;
    private TextView productNameCardView, productPriceCardView;
    private CircleImageView productImageBarCardView;
    private Button markAsCompleteCardView;

    // Chat
    private TextView messageInput;
    private ImageButton sendMessage;

    // Firebase
    private FirebaseDatabase mDatabase;
    private DatabaseReference orderRef, userRef, chatRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    //Views
    ChatAdapter chatAdapter;
    List<ChatModel> chatModelList;
    RecyclerView recyclerView;

    // Variables
    String productId, cropName, cropPrice, cropPicture, saveCurrentTime, saveCurrentDate,
            orderId, farmerId, dealerId, farmerName, dealerName, cropDescription, cropQuantity, currentUserId;

    private String receiverName, senderId, senderName, message;

    OrderModel orderModel = new OrderModel();

    boolean orderComplete = true;

    Calendar calendarForTime, calendarForDate;
    SimpleDateFormat currentDate, currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initializeViews();
        initializeDatabase();
        checkAccountType();
        retrieveDataFromIntents();
        retrieveCardViewProduct();

        Log.d("onCreate()", "Dealer ID: " + dealerId);

        markAsCompleteCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processOrderAsComplete();
            }
        });

        sendMessage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String message = messageInput.getText().toString();

                if(TextUtils.isEmpty(message)) {
                    Toast.makeText(ChatActivity.this, "Message cannot be blank.", Toast.LENGTH_SHORT).show();
                } else {
                    sendChatMessage(dealerId, dealerName, farmerId, farmerName, message);
                }
            }
        });

        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ChatModel chatModel = dataSnapshot.getValue(ChatModel.class);
                //Log.d("readChat", "Sender ID: " + chatModel.getSenderId().toString());
                //Log.d("readChat", "Receiver ID: " + chatModel.getReceiverId().toString());
                //Log.d("readChat", "Message: " + chatModel.getMessage().toString());

                readChatMessages(currentUserId, farmerId); // Read chat message of the person who sent the message, currentUserId is the receiver.
                //currentUserId, receiverId
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void initializeViews() {
        navProfileName = findViewById(R.id.custom_profile_name);
        navImageView = findViewById(R.id.custom_profile_picture);

        productCardView = findViewById(R.id.product_card_view);
        productNameCardView = findViewById(R.id.product_name_card_view);
        productPriceCardView = findViewById(R.id.product_price_card_view);
        productImageBarCardView = findViewById(R.id.product_image_card_view);
        markAsCompleteCardView = findViewById(R.id.mark_as_complete_card_view);

        messageInput = findViewById(R.id.send_message);
        sendMessage = findViewById(R.id.send_message_button);

        recyclerView = findViewById(R.id.messages_list_users);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

    }

    private void initializeDatabase() {
        mDatabase = FirebaseDatabase.getInstance();

        orderRef = mDatabase.getReference("Order");
        userRef = mDatabase.getReference("User");
        chatRef = mDatabase.getReference("Chat");
    }

    private void checkAccountType() {
        final String TAG = "checkAccountType";

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUserId = mUser.getUid();

        if(currentUserId == null) {
            Intent intent = new Intent(ChatActivity.this, SignInActivity.class);
            startActivity(intent);
        }

        Log.d(TAG, "User ID: " + currentUserId);

        userRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean accountIsFarmer = dataSnapshot.child("farmer").getValue(Boolean.class);
                if(accountIsFarmer) {
                    // Show button
                    markAsCompleteCardView.setVisibility(View.VISIBLE);
                } else {
                    // Hide button
                    markAsCompleteCardView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void retrieveDataFromIntents() {
        //Get the intent values from AddProductActivity
        orderId = orderRef.push().getKey();
        farmerId = getIntent().getExtras().get("farmerId").toString();
        dealerId = getIntent().getExtras().get("dealerId").toString();
        dealerName = getIntent().getExtras().get("dealerName").toString();
        farmerName = getIntent().getExtras().get("farmerName").toString();
        productId = getIntent().getExtras().get("productId").toString();
        cropName = getIntent().getExtras().get("cropName").toString();
        cropDescription = getIntent().getExtras().get("cropDescription").toString();
        cropPrice = getIntent().getExtras().get("cropPrice").toString();
        cropQuantity = getIntent().getExtras().get("cropQuantity").toString();

        orderModel = new OrderModel();

        orderModel.setOrderId(orderId);
        orderModel.setFarmerId(farmerId);
        orderModel.setDealerId(dealerId);
//        orderModel.setDealerName(dealerName);
        orderModel.setFarmerName(farmerName);
        orderModel.setProductId(productId);
        orderModel.setProductName(cropName);
        orderModel.setProductDescription(cropDescription);
        orderModel.setProductPrice(cropPrice);
        orderModel.setProductQuantity(cropQuantity);
        orderModel.setOrderDate(saveCurrentDate);
        orderModel.setOrderTime(saveCurrentTime);
        orderModel.setComplete(orderComplete);
    }

    private void retrieveCardViewProduct() {
        productId = getIntent().getExtras().get("productId").toString();
        cropName = getIntent().getExtras().get("cropName").toString();
        cropPrice = getIntent().getExtras().get("cropPrice").toString();
        cropPicture = getIntent().getExtras().get("productPicture").toString();

        productNameCardView.setText(cropName);
        productPriceCardView.setText(cropPrice);
        Picasso.get().load(cropPicture).fit().into(productImageBarCardView);
    }

    @SuppressLint("SimpleDateFormat")
    private void processOrderAsComplete() {

        calendarForDate = Calendar.getInstance();
        currentDate = new SimpleDateFormat("MM-dd-yyyy");
        saveCurrentDate = currentDate.format(calendarForDate.getTime());

        calendarForTime = Calendar.getInstance();
        currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime = currentTime.format(calendarForTime.getTime());

        orderId = orderRef.push().getKey();
        orderRef.child(farmerId).child(orderId).setValue(orderModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(ChatActivity.this, "Order processed.", Toast.LENGTH_SHORT).show();
                if (orderModel.isComplete()) {
                    markAsCompleteCardView.setText("Complete");
                    markAsCompleteCardView.setClickable(false);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChatActivity.this, "Order failed to process.", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void sendChatMessage(String senderId, String senderName, String receiverId, String receiverName, String message) {
        ChatModel chatModel = new ChatModel();

//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("senderId", senderId);
//        hashMap.put("senderName", senderName);
//        hashMap.put("receiverId", receiverId);
//        hashMap.put("receiverName", receiverName);
//        hashMap.put("message", message);

        chatModel.setSenderId(senderId);
        chatModel.setSenderName(senderName);
        chatModel.setReceiverId(receiverId);
        chatModel.setReceiverName(receiverName);
        chatModel.setMessage(message);

        chatRef.push().setValue(chatModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                messageInput.setText("");
            }
        });
    }

    private void readChatMessages(final String currentUserId, final String receiverId) {
        final String TAG = "readChatMessages";

        chatModelList = new ArrayList<>();

        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatModelList.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatModel chatModel = snapshot.getValue(ChatModel.class);

                    String receiverId = chatModel.getReceiverId();
                    String receiverName = chatModel.getReceiverName();
                    String senderId = chatModel.getSenderId();
                    String senderName = chatModel.getSenderName();
                    String message = chatModel.getMessage();

//                    if(chatModel.getReceiverId().equals(currentUserId) && chatModel.getSenderId().equals(receiverId) ||
//                        chatModel.getReceiverId().equals(receiverId) && chatModel.getSenderId().equals(currentUserId)) {

                    Log.d(TAG, "============");
                    Log.d(TAG, "Current User ID: " + currentUserId);
                    Log.d(TAG, "Receiver ID: " + receiverId.toString());
//                    Log.d("TAG", "Sender ID: " + senderId.toString());
//                    Log.d("TAG", "Message: " + message.toString());
                    Log.d("TAG", "============");

                    if(receiverId.equals(currentUserId) && senderId.equals(receiverId) ||
                            receiverId.equals(receiverId) && senderId.equals(currentUserId)) {

                        chatModelList.add(chatModel);

                    } else {
                        Log.d("TAG", "If block failed.");
                    }

                    chatAdapter = new ChatAdapter(ChatActivity.this, chatModelList);
                    recyclerView.setAdapter(chatAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        recyclerView.setAdapter(chatAdapter);
    }
}
