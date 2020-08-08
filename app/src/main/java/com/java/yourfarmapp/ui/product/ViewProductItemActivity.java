package com.java.yourfarmapp.ui.product;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.java.yourfarmapp.Model.RatingModel;
import com.java.yourfarmapp.ui.chat.ChatActivity;
import com.java.yourfarmapp.Model.ProductModel;
import com.java.yourfarmapp.Model.UserModel;
import com.java.yourfarmapp.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewProductItemActivity extends AppCompatActivity {

    TextView productName, productDescription, productPrice, productQuantity, farmerName, farmerPhoneNumber;
    FloatingActionButton ratingBarFab;
    FloatingActionButton cartFab;

    ImageView productImage;
    CircleImageView farmerProfileImage;

    RatingBar productRating;

    Button callFarmer, textFarmer, chatFarmer;

    DatabaseReference userReference;
    DatabaseReference productReference;
    DatabaseReference ratingReference;

    FirebaseUser mUser;
    FirebaseAuth mAuth;

    String dealerName, userId, cropProductId, farmerId, productId;
    ProductModel productModel = new ProductModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product_item);

        cropProductId = getIntent().getExtras().get("cropProductId").toString();

        initializeViews();
        initializeDatabase();
        getProductDetails(cropProductId);
        getUserDetails(cropProductId);

        ratingBarFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRatingDialog();
            }
        });

        cartFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPurchaseDialog();
            }
        });
    }

    private void openPurchaseDialog() {

    }

    private void startChatFarmerActivity() {
        Intent chatIntent = new Intent(ViewProductItemActivity.this, ChatActivity.class);
        chatIntent.putExtra("dealerId", userId);
        chatIntent.putExtra("dealerName", dealerName);
        chatIntent.putExtra("farmerId", productModel.getUserKey());
        chatIntent.putExtra("farmerName", productModel.getFullName());
        chatIntent.putExtra("productId", productModel.getCropProductID());
        chatIntent.putExtra("cropName", productModel.getCropName());
        chatIntent.putExtra("cropPrice", productModel.getCropPrice());
        chatIntent.putExtra("farmerProfilePictureCircle", productModel.getFarmerProfilePic());
        chatIntent.putExtra("productPicture", productModel.getCropImage());
        chatIntent.putExtra("cropDescription", productModel.getCropDescription());
        chatIntent.putExtra("cropPrice", productModel.getCropPrice());
        chatIntent.putExtra("cropQuantity", productModel.getCropQuantity());
        startActivity(chatIntent);
    }

    private void initializeDatabase() {
        productReference = FirebaseDatabase.getInstance().getReference().child("Product");
        userReference = FirebaseDatabase.getInstance().getReference().child("User");
        ratingReference = FirebaseDatabase.getInstance().getReference().child("Rating");
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = mUser.getUid();
    }

    private void initializeViews() {
        productName = findViewById(R.id.product_name);
        productDescription = findViewById(R.id.product_description);
        productPrice = findViewById(R.id.product_price);

        farmerName = findViewById(R.id.farmer_name);
        farmerPhoneNumber = findViewById(R.id.farmer_phone_number);

        productImage = findViewById(R.id.product_image);
        farmerProfileImage = findViewById(R.id.farmer_profile_picture);

        productRating = findViewById(R.id.product_rating);
        ratingBarFab = findViewById(R.id.btn_rating);
        cartFab = findViewById(R.id.btn_cart);

        callFarmer = findViewById(R.id.call_farmer);
        textFarmer = findViewById(R.id.text_farmer);
        chatFarmer = findViewById(R.id.chat_farmer);
    }

    private void getUserDetails(String productId) {
        FirebaseDatabase.getInstance().getReference().child("User").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dealerName = dataSnapshot.child("fullName").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("Product").child(cropProductId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            ProductModel productModel = dataSnapshot.getValue(ProductModel.class);

                            productName.setText(productModel.getCropName());
                            productDescription.setText("Description: " + productModel.getCropDescription());
                            productPrice.setText("₱: " + productModel.getCropPrice());
                            productRating.setRating(productModel.getRatingCount());

                            //Rating Bar
                            farmerName.setText(productModel.getFullName());
                            farmerPhoneNumber.setText(productModel.getNumber());

                            //Image Views
                            //Glide.with(getApplicationContext()).load(productModel.getCropImage()).into(productImage);
                            Picasso.get().load(productModel.getCropImage()).fit().into(productImage);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

        FirebaseDatabase.getInstance().getReference().child("User").child(userId)
            .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        UserModel userModel = dataSnapshot.getValue(UserModel.class);
                        Picasso.get().load(userModel.getProfilePic()).fit().into(farmerProfileImage);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }

    private void getProductDetails(String cropProductId) {
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = mUser.getUid();

        FirebaseDatabase.getInstance().getReference().child("Product").child(cropProductId)
            .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        productModel = dataSnapshot.getValue(ProductModel.class);

                        callFarmer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startCallFarmer();
                            }
                        });

                        textFarmer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startTextFarmer();
                            }
                        });

                        chatFarmer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startChatFarmerActivity();
                            }
                        });

                    } else {
                        Toast.makeText(ViewProductItemActivity.this, "Unable to get user details.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        //Retrieve rating values
        FirebaseDatabase.getInstance().getReference("Rating").child(cropProductId).child("Total").child("totalRating")
         .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    float rating = Float.parseFloat(snapshot.getValue().toString());
                    productRating.setRating(rating);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void openRatingDialog() {
        mAuth = FirebaseAuth.getInstance();

        AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ViewProductItemActivity.this);
        builder.setTitle("Rating");
        builder.setMessage("What do you think of this product?");

        View itemView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_rate, null); //Change

        final RatingBar ratingBarStars = itemView.findViewById(R.id.rating_bar_comment);
        final EditText ratingBarEditText = itemView.findViewById(R.id.rating_edit_text);

        final String userId = mAuth.getCurrentUser().getUid();
        final String userEmail = mAuth.getCurrentUser().getEmail();

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                RatingModel ratingModel = new RatingModel();
                ratingModel.setComment(ratingBarEditText.getText().toString());
                ratingModel.setDealerId(userId);
                ratingModel.setFarmerId(farmerId);
                ratingModel.setDealerName(dealerName);
                ratingModel.setFarmerName(productModel.getFullName());
                ratingModel.setRatingValue(ratingBarStars.getRating());
                submitRatingToFirebase(ratingModel);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.setView(itemView);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void submitRatingToFirebase(final RatingModel ratingModel) {
        FirebaseDatabase.getInstance().getReference("Rating")
            .child(cropProductId)
            .push()
            .setValue(ratingModel)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        addRatingToProduct(ratingModel.getRatingValue());
                    }
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ViewProductItemActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

    }
    private void addRatingToProduct(final float ratingValue) {
        ratingReference
                .child(cropProductId)
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        ProductModel productModel = new ProductModel();

                        if(productModel.getRatingValue() == null) {
                            productModel.setRatingValue(0d);
                        }

                        if(productModel.getRatingCount() == 0) {
                            productModel.setRatingCount(0);
                        }

                        double sumRating = productModel.getRatingValue() + ratingValue;
                        int ratingCount = productModel.getRatingCount();
                        ratingCount++;

                        Map<String, Object> updateData = new HashMap<>();
                        updateData.put("totalRating", sumRating);
                        updateData.put("totalCount", ratingCount);

                        dataSnapshot.getRef()
                            .child("Total")
                            .updateChildren(updateData)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(ViewProductItemActivity.this, "Thank you!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ViewProductItemActivity.this, "Error: " , Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ViewProductItemActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
    }


    private boolean isTelephonyEnabled(){
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        return telephonyManager != null && telephonyManager.getSimState()== TelephonyManager.SIM_STATE_READY;
    }


    private void startCallFarmer() {
        if(isTelephonyEnabled()) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + farmerPhoneNumber.toString()));
            startActivity(intent);
        }
    }

    private void startTextFarmer() {
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
        smsIntent.setData(Uri.parse("sms:"));
        smsIntent.putExtra("address", farmerPhoneNumber.toString());
        startActivity(smsIntent);
    }

    private void startChatFarmer() {

    }
}
