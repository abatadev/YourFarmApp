package com.java.yourfarmapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.java.yourfarmapp.Model.ProductModel;

public class ViewProductItem extends AppCompatActivity {

    TextView productName;
    TextView productDescription;
    TextView productPrice;
    TextView farmerName;
    TextView farmerPhoneNumber;

    ImageView productImage;
    ImageView farmerProfileImage;

    RatingBar productRating;

    Button callFarmer;
    Button textFarmer;
    Button chatFarmer;

    FirebaseDatabase userReference;
    FirebaseDatabase productReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product_item);

        productName = findViewById(R.id.product_name);
        productDescription = findViewById(R.id.product_description);
        productPrice = findViewById(R.id.product_price);

        farmerName = findViewById(R.id.farmer_name);
        farmerPhoneNumber = findViewById(R.id.farmer_phone_number);

        productImage = findViewById(R.id.product_image);
        farmerProfileImage = findViewById(R.id.farmer_profile_picture);

        productRating = findViewById(R.id.product_rating);

        callFarmer = findViewById(R.id.call_farmer);
        textFarmer = findViewById(R.id.text_farmer);
        chatFarmer = findViewById(R.id.chat_farmer);

        String cropProductId = getIntent().getStringExtra("cropProductId");

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
                startChatFarmer();
            }
        });
        
        getProductDetails(cropProductId);

    }

    private void getProductDetails(String productId) {
        FirebaseDatabase.getInstance().getReference().child("Product").child(productId)
        .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    ProductModel productModel = dataSnapshot.getValue(ProductModel.class);


                    productName.setText(productModel.getCropName());
                    productDescription.setText(productModel.getCropDescription());
                    productPrice.setText(productModel.getCropPrice());

                    //Rating Bar
                    farmerName.setText(productModel.getFullName());
                    farmerPhoneNumber.setText(productModel.getNumber());

                    //Image Views
                    Glide.with(getApplicationContext()).load(productModel.getCropImage()).into(productImage);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void loadProduct(ProductModel productModel) {

    }

    private void startCallFarmer() {
    }

    private void startTextFarmer() {

    }

    private void startChatFarmer() {

    }
}
