package com.java.yourfarmapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.java.yourfarmapp.Model.ProductModel;

public class AddProductActivity extends AppCompatActivity {

    private static final String TAG = "AddProduct.java";

    DatabaseReference productReference;

    ImageView cropImage;
    EditText cropType, cropName, cropPrice;

    TextView dealer_name;
    TextView farmer_name;

    Button buttonSave;

    ProductModel productModel;

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        cropImage = (ImageView) findViewById(R.id.crop_image);
        cropType = (EditText) findViewById(R.id.crop_type);
        cropName = (EditText) findViewById(R.id.crop_name);
        cropPrice = (EditText) findViewById(R.id.crop_price);

        buttonSave = (Button) findViewById(R.id.save_product_button);

        productModel = new ProductModel();
        productReference = FirebaseDatabase.getInstance().getReference().child("Product");
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productModel.setCropType(cropType.getText().toString());
                productModel.setCropName(cropName.getText().toString());
                productModel.setCropPrice(cropPrice.getText().toString());
                productReference.push().setValue(productModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AddProductActivity.this, "Product has been posted successfully",
                                Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Post submit success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddProductActivity.this, "ERROR: Could not post product: "
                                + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "postSubmit:onFailureListener: " + e.getMessage());
                    }
                });

//                Toast.makeText(AddProduct.this, "...", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
