package com.java.yourfarmapp.ui.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.java.yourfarmapp.Adapter.MessagesAdapter;
import com.java.yourfarmapp.Model.MessagesModel;
import com.java.yourfarmapp.Model.OrderModel;
import com.java.yourfarmapp.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivityBak extends AppCompatActivity {
//
//    private DatabaseReference orderReference;
//
//    private CircleImageView cardViewCircleImage;
//    private TextView cardViewProductName;
//    private TextView cardViewProductPrice;
//
//    private ImageButton sendMessageButton, sendImageButton;
//    private Button markAsComplete;
//    private EditText userMessageInput;
//    private EditText finalPrice;
//    RecyclerView userMessagesList;
//    private final List<MessagesModel> messagesModelList = new ArrayList<>();
//    private LinearLayoutManager linearLayoutManager;
//    private MessagesAdapter messagesAdapter;
//
//    private String messageReceiverID, messageReceiverName;
//    private String messageSenderId;
//    private String farmerProfilePic;
//
//    private String cropPicture;
//
//    private String nameOfSender, nameOfReceiver;
//
//    String orderId, farmerId, dealerId, farmerName, dealerName, productId,
//            cropName, cropDescription, cropPrice, cropQuantity, cropTotalPrice;
//
//    private TextView receiverName;
//    private CircleImageView receiverProfileImage;
//
//    FirebaseUser mUser;
//
//    DatabaseReference rootRef;
//    DatabaseReference userRef;
//    DatabaseReference orderRef;
//
//    FirebaseAuth mAuth;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_chat);
//
//        rootRef = FirebaseDatabase.getInstance().getReference();
//
//        mAuth = FirebaseAuth.getInstance();
//        /**
//         * Retrieve values as intent.
//         */
//        messageSenderId = getIntent().getExtras().get("dealerId").toString();
//        messageReceiverID = getIntent().getExtras().get("farmerId").toString();
//
//        initializeFields();
//        displayReceiverInfo();
//
//        sendMessageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                sendMessage();
//            }
//        });
//
//        /**
//         * TODO Farmer will mark the deal as complete.
//         */
//
//        markAsComplete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                productPrompt();
//            }
//        });
//
//
//
//        retrieveCardViewProduct();
//        fetchMessages();
//    }
//
//    private void retrieveCardViewProduct() {
//        //Pass from intent of detailed product activity
//
//        productId = getIntent().getExtras().get("productId").toString();
//        cropName = getIntent().getExtras().get("cropName").toString();
//        cropPrice = getIntent().getExtras().get("cropPrice").toString();
//        cropPicture = getIntent().getExtras().get("productPicture").toString();
//
//        cardViewProductName.setText(cropName);
//        cardViewProductPrice.setText(cropPrice);
//        Picasso.get().load(cropPicture).fit().into(cardViewCircleImage);
//    }
//
//    /**
//     * Run this code to process the product order as complete on a set on click listener
//     * button, refer from Firebase to process order, to use the OrderModel. */
//
//    private void retrieveDataFromIntents() {
//        //Get the intent values from AddProductActivity
//        orderId = orderReference.push().getKey();
//        farmerId = getIntent().getExtras().get("farmerId").toString();
//        dealerId = getIntent().getExtras().get("dealerId").toString();
//        //dealerName = getIntent().getExtras().get("dealerName").toString();
//        farmerName = getIntent().getExtras().get("farmerName").toString();
//        productId = getIntent().getExtras().get("productId").toString();
//        cropName = getIntent().getExtras().get("cropName").toString();
//        cropDescription = getIntent().getExtras().get("cropDescription").toString();
//        cropPrice = getIntent().getExtras().get("cropPrice").toString();
//        cropQuantity = getIntent().getExtras().get("cropQuantity").toString();
//        OrderModel orderModel = new OrderModel();
//
//        Calendar calendarForDate = Calendar.getInstance();
//        SimpleDateFormat currentDate = new SimpleDateFormat("MM-dd-yyyy");
//        String saveCurrentDate = currentDate.format(calendarForDate.getTime());
//
//        Calendar calendarForTime = Calendar.getInstance();
//        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
//        String saveCurrentTime = currentTime.format(calendarForTime.getTime());
//
//
//        orderModel.setOrderId(orderId);
//        orderModel.setFarmerId(farmerId);
//        orderModel.setDealerId(dealerId);
//        //orderModel.setDealerName(dealerName);
//        orderModel.setFarmerName(farmerName);
//        orderModel.setProductId(productId);
//        orderModel.setProductName(cropName);
//        orderModel.setProductDescription(cropDescription);
//        orderModel.setProductPrice(cropPrice);
//        orderModel.setProductQuantity(cropQuantity);
//        orderModel.setOrderDate(saveCurrentDate);
//        orderModel.setOrderTime(saveCurrentTime);
//        orderModel.setProductTotalPrice(finalPriceText.getText().toString());
//
//    }
//
//    private void populateDataToOrderModel() {
//
//
//    }
//    private void processProductInformation() {
//        orderReference = FirebaseDatabase.getInstance().getReference().child("Order");
//
//        boolean isComplete = true;
//
//        EditText finalPriceText = findViewById(R.id.finalized_price);
//
//
//
//
//        //orderModel.isComplete(isComplete);
//        //Set isComplete
//
//        /*
//        * Order history from farmer will process the order.
//        */
//
//        orderReference.child(farmerId).child(orderId).setValue(orderModel)
//        .addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                Toast.makeText(ChatActivity.this, "Order has been processed.", Toast.LENGTH_SHORT).show();
//                /**
//                 * Hide views
//                 */
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(ChatActivity.this, "Error: " + e, Toast.LENGTH_SHORT).show();
//                Log.e("Order", e.getMessage().toString());
//            }
//        });
//    }
//
//    private void productPrompt() {
//        AlertDialog.Builder productPromptBuilder = new AlertDialog.Builder(ChatActivity.this);
//
//        productPromptBuilder.setTitle("Update Order");
//        productPromptBuilder.setMessage("Please complete the following information");
//
//        View productPromptView = LayoutInflater.from(ChatActivity.this).inflate(R.layout.layout_activity_process_order, null); //Change
//
//        productPromptBuilder.setNegativeButton("Cancel", ((dialogInterface, i) -> {
//            dialogInterface.dismiss();
//        }));
//
//        productPromptBuilder.setPositiveButton("Confirm", (((dialogInterface, i) -> {
//            processProductInformation();
//        })));
//
//        productPromptBuilder.setView(productPromptView);
//        AlertDialog dialog = productPromptBuilder.create();
//        dialog.show();
//    }
//
////    private void changeProductDetails() {
////        AlertDialog.Builder changeProductDetailsBuilder = new AlertDialog.Builder(ChatActivity.this);
////
////        changeProductDetailsBuilder.setTitle("Process the order.");
////        changeProductDetailsBuilder.setMessage("");
////
////
////        orderRef = FirebaseDatabase.getInstance().getReference("Order");
////
////        String orderId = orderRef.getKey();
////        String farmerId, dealerId, farmerName, dealerName, productId,
////                cropName, cropDescription, cropPrice, cropQuantity;
////
////        //TextViews
////        TextView editProductName;
////        TextView editProductCategory;
////        EditText editProductPrice;
////        EditText editProductQuantity;
////
////        editProductName = findViewById(R.id.edit_product_name);
////        editProductCategory = findViewById(R.id.edit_product_category);
////        editProductPrice = findViewById(R.id.edit_product_price);
////        editProductQuantity = findViewById(R.id.edit_product_quantity);
////
////        orderId = orderReference.push().getKey();
////        farmerId = getIntent().getExtras().get("farmerId").toString();
////        dealerId = getIntent().getExtras().get("dealerId").toString();
////        dealerName = getIntent().getExtras().get("dealerName").toString();
////        farmerName = getIntent().getExtras().get("farmerName").toString();
////        productId = getIntent().getExtras().get("productId").toString();
////        cropName = getIntent().getExtras().get("cropName").toString();
////        cropDescription = getIntent().getExtras().get("cropDescription").toString();
////        cropPrice = getIntent().getExtras().get("cropPrice").toString();
////        cropQuantity = getIntent().getExtras().get("cropQuantity").toString();
////
////
////
////        orderRef.child(orderId).addValueEventListener(new ValueEventListener() {
////            @Override
////            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                String productName = dataSnapshot.child("productName").toString();
////                String productCategory = dataSnapshot.child("productCategory").toString();
////                String productQuantity = dataSnapshot.child("productQuantity").toString();
////                String productPrice = dataSnapshot.child("productPrice").toString();
////
////                editProductName.setText(cropName);
////                editProductCategory.setText(productCategory);
////                editProductQuantity.setText(cropQuantity);
////                editProductPrice.setText(cropPrice);
////
////                OrderModel orderModel = new OrderModel();
////                orderModel.setProductName(productName);
////                orderModel.setProductCategory(productCategory);
////                orderModel.setProductQuantity(productQuantity);
////                orderModel.setProductPrice(productPrice);
////
////                orderRef.setValue(orderModel).addOnSuccessListener(new OnSuccessListener<Void>() {
////                    @Override
////                    public void onSuccess(Void aVoid) {
////                        Toast.makeText(ChatActivity.this, "Order has been updated", Toast.LENGTH_SHORT).show();
////                    }
////                }).addOnFailureListener(new OnFailureListener() {
////                    @Override
////                    public void onFailure(@NonNull Exception e) {
////                        Toast.makeText(ChatActivity.this, "Order has failed to update.", Toast.LENGTH_SHORT).show();
////                    }
////                });
////
////            }
////
////            @Override
////            public void onCancelled(@NonNull DatabaseError databaseError) {
////
////            }
////        });
////
////
////
////        //Firebase set
////        View itemView = LayoutInflater.from(ChatActivity.this).inflate(R.layout.layout_activity_show_chat_dialog, null); //Change
////
////        changeProductDetailsBuilder.setNegativeButton("Cancel", ((dialogInterface, i) -> {
////            dialogInterface.dismiss();
////        }));
////
////        changeProductDetailsBuilder.setNeutralButton("Confirm", (((dialogInterface, i) -> {
////            processProductInformation();
////        })));
////
////
////        changeProductDetailsBuilder.setView(itemView);
////        AlertDialog dialog = changeProductDetailsBuilder.create();
////        dialog.show();
////    }
//
//    private void fetchMessages() {
//        rootRef.child("Messages")
//                .child(messageSenderId)
//                .child(messageReceiverID)
//                .addChildEventListener(new ChildEventListener() {
//                    @Override
//                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                        if(dataSnapshot.exists()) {
//                            MessagesModel messagesModel = dataSnapshot.getValue(MessagesModel.class);
//                            messagesModelList.add(messagesModel);
//                            messagesAdapter.notifyDataSetChanged();
//
//                            Log.d("testing", "Sender ID: " + messageSenderId);
//                            Log.d("testing", "Receiver ID: " + messageReceiverID);
//                        } else {
//                            Toast.makeText(ChatActivity.this, "No existing chat.", Toast.LENGTH_SHORT).show();
//                            Log.d("testing", "Sender ID: " + messageSenderId);
//                            Log.d("testing", "Receiver ID: " + messageReceiverID);
//                        }
//                    }
//
//                    @Override
//                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                    }
//
//                    @Override
//                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//                    }
//
//                    @Override
//                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//    }
//
//    private void sendMessage() {
//        final String TAG = "sendMessage";
//        rootRef = FirebaseDatabase.getInstance().getReference();
//        messageReceiverID = getIntent().getExtras().get("farmerId").toString();
//        messageSenderId = mAuth.getCurrentUser().getUid().toString();
//
//        nameOfSender = FirebaseDatabase.getInstance().getReference()
//                .child("User").child(messageSenderId).child("fullName").toString();
//
//        String messageText = userMessageInput.getText().toString();
//
//        Log.d(TAG, "messageReceiverID: " + messageReceiverID);
//        Log.d(TAG, "messageSenderID: " + messageSenderId);
//        Log.d(TAG, "Name of Sender: " + nameOfSender);
//        Log.d(TAG, "Message Body: " + messageText);
//
//        if(TextUtils.isEmpty(messageText)) {
//            Toast.makeText(this, "You need to input a message first.", Toast.LENGTH_SHORT).show();
//        } else {
//
//            String messageSenderReference = "Messages/ " + messageSenderId + "/" + messageReceiverID;
//            String messageReceiverReference = "Messages/" + messageReceiverID + "/" + messageSenderId;
//
//            DatabaseReference userRef = rootRef.child("Messages").child(messageSenderId)
//                    .child(messageReceiverID).push();
//
//            String message_push_id = userRef.getKey();
//
//            Calendar calendarForDate = Calendar.getInstance();
//            SimpleDateFormat currentDate = new SimpleDateFormat("MM-dd-yyyy");
//            String saveCurrentDate = currentDate.format(calendarForDate.getTime());
//
//            Calendar calendarForTime = Calendar.getInstance();
//            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
//            String saveCurrentTime = currentTime.format(calendarForTime.getTime());
//
//            Map messageTextBody = new HashMap();
//                messageTextBody.put("message", messageText);
//                messageTextBody.put("time", saveCurrentTime);
//                messageTextBody.put("date", saveCurrentDate);
//                messageTextBody.put("from", messageSenderId);
//                messageTextBody.put("fromName", nameOfSender);
//                messageTextBody.put("type", "text");
//                messageTextBody.put("to", messageReceiverID);
//                messageTextBody.put("toName", messageReceiverName);
//
//            Map messageBodyDetails = new HashMap();
//                messageBodyDetails.put(messageSenderReference + "/" + message_push_id , messageTextBody);
//                messageBodyDetails.put(messageReceiverReference + "/" + message_push_id , messageTextBody);
//
//            rootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
//                @Override
//                public void onComplete(@NonNull Task task) {
//                    if(task.isSuccessful()) {
//                        Toast.makeText(ChatActivity.this, "Sent", Toast.LENGTH_SHORT).show();
//                        userMessageInput.setText("");
//                    } else {
//                        String messages = task.getException().getMessage();
//                        Toast.makeText(ChatActivity.this, messages, Toast.LENGTH_SHORT).show();
//                        userMessageInput.setText("");
//                    }
//
//                }
//            });
//        }
//    }
//
//
//    private void displayReceiverInfo() {
//        final String TAGS = "displayReceiverInfo";
//
//        messageReceiverID = getIntent().getExtras().get("farmerId").toString();
//        messageReceiverName = getIntent().getExtras().get("farmerName").toString();
//        productId = getIntent().getExtras().get("productId").toString();
//        farmerProfilePic = getIntent().getExtras().get("farmerProfilePictureCircle").toString();
//
//        receiverName.setText(messageReceiverName);
//        Glide.with(ChatActivity.this).load(farmerProfilePic).into(receiverProfileImage);
//
//        Log.d(TAGS, "Farmer ID: " + messageReceiverID);
//        Log.d(TAGS, "Farmer Name: " + messageReceiverName);
//        Log.d(TAGS, "Crop Product ID: " + productId);
//        Log.d(TAGS, "Farmer Profile Pic URL: " + farmerProfilePic);
//
//
//        FirebaseDatabase.getInstance().getReference().child("Product").child(productId)
//                .addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.exists()) {
//
//                } else {
//                    Toast.makeText(ChatActivity.this, "Some things are null.", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
//
//    private void initializeFields() {
//        userRef = FirebaseDatabase.getInstance().getReference();
//
//        sendMessageButton = findViewById(R.id.send_message_button);
//        sendImageButton = findViewById(R.id.send_image_file);
//        userMessageInput = findViewById(R.id.send_message);
//        markAsComplete = findViewById(R.id.mark_as_complete_card_view);
//        //finalPrice = findViewById(R.id.total_price);
//
//        cardViewCircleImage = findViewById(R.id.product_image_card_view);
//        cardViewProductName = findViewById(R.id.product_name_card_view);
//        cardViewProductPrice = findViewById(R.id.product_price_card_view);
//
//        receiverName = findViewById(R.id.custom_profile_name);
//        receiverProfileImage = findViewById(R.id.custom_profile_picture_circle);
//
//        messagesAdapter = new MessagesAdapter(messagesModelList);
//        userMessagesList = (RecyclerView) findViewById(R.id.messages_list_users);
//        linearLayoutManager = new LinearLayoutManager(this);
//
//        userMessagesList.setHasFixedSize(true);
//        userMessagesList.setLayoutManager(linearLayoutManager);
//        userMessagesList.setAdapter(messagesAdapter);
//    }
}
