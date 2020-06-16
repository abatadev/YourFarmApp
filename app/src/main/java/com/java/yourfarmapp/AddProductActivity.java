package com.java.yourfarmapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.java.yourfarmapp.Model.CategoryModel;
import com.java.yourfarmapp.Model.ProductModel;
import com.java.yourfarmapp.Model.UserModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddProductActivity extends AppCompatActivity {
    private FirebaseAuth.AuthStateListener fireAuthListener;

    FirebaseUser currentUser;

    private static final String TAG = "AddProduct.java";
    String selectedSpinnerId = "";
    String test = "";

    DatabaseReference productReferenceToSpinner;
    DatabaseReference productReference;
    DatabaseReference categoryReference;
    DatabaseReference userReference;

    FirebaseUser mUser;
    DataSnapshot dataSnapshot;

    private ImageView cropImage;
    private TextView categoryTextView;

    private EditText cropName;
    private EditText cropPrice;
    private EditText cropQuantity;
    private EditText cropDescription;

    private Spinner categorySpinner;
    private Spinner quantitySpinner;

    private Button buttonSave;

    UserModel userModel;
    ProductModel productModel; // Model
    CategoryModel categoryModel;

    @Override
    protected  void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        cropImage = (ImageView) findViewById(R.id.crop_image);
        categoryTextView = findViewById(R.id.category_text_view);
        cropName = (EditText) findViewById(R.id.crop_name);
        cropPrice = (EditText) findViewById(R.id.crop_price);
        cropQuantity = findViewById(R.id.crop_quantity);
        cropDescription = findViewById(R.id.product_description);

        categorySpinner = findViewById(R.id.category_spinner);
        quantitySpinner = findViewById(R.id.quantity_spinner);

        buttonSave = (Button) findViewById(R.id.save_product_button);

        userModel = new UserModel();
        productModel = new ProductModel();

        fireAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();
                if (currentUser == null) {
                    //user not login
                    // TO DO - Pass intent to login screen
                    Log.d(TAG, "Value | No user logged in");
                }
            }
        };


        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               submitProductEntry();
            }
        });

    }

    private void submitProductEntry() {

        final String TAG = "submitProductEntry()";

        List<ProductModel> tempList = new ArrayList<>();

        // Two references
        userReference = FirebaseDatabase.getInstance().getReference().child("User");
        productReference = FirebaseDatabase.getInstance().getReference().child("Product");


        mUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = mUser.getUid();

        // TODO productReferenceToSpinner.child(userId).setValue();


        String productId = productReference.push().getKey();


        productModel.setCropProductID(productId);
        productModel.setCropName(cropName.getText().toString());
        productModel.setCropPrice(cropPrice.getText().toString());
        productModel.setCropQuantity(cropQuantity.getText().toString());
        productModel.setCropDescription(cropDescription.getText().toString());
        productModel.setUserKey(userReference.child(mUser.getUid()).getKey());

        userReference.child(userId).child("fullName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String username = dataSnapshot.getValue(String.class);
                Log.d(TAG, "username: " + username);

                productReference.child(productId).child("fullName").setValue(username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        productReference.child(productId).setValue(productModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AddProductActivity.this, "Added product successfully", Toast.LENGTH_SHORT).show();
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        // set User
        // set

    }



//    private void generateCategories() {
//        //Only call this method to regenerate category tables
//
//        productReferenceToSpinner = FirebaseDatabase.getInstance().getReference().child("Category");
//        Map<String, CategoryModel> categoryModelHashMap = new HashMap<>();
//
//        categoryModelHashMap.put("1", new CategoryModel("001" , "Livestock",
//                "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e8/20150728_xl_P1000804_Leck_mich_Zaertlichkeit_der_Rinder.JPG/220px-20150728_xl_P1000804_Leck_mich_Zaertlichkeit_der_Rinder.JPG\""));
//        categoryModelHashMap.put("2", new CategoryModel("002", "Vegetables",
//                "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e8/20150728_xl_P1000804_Leck_mich_Zaertlichkeit_der_Rinder.JPG/220px-20150728_xl_P1000804_Leck_mich_Zaertlichkeit_der_Rinder.JPG"));
//        categoryModelHashMap.put("3", new CategoryModel("003", "Rice",
//                "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e8/20150728_xl_P1000804_Leck_mich_Zaertlichkeit_der_Rinder.JPG/220px-20150728_xl_P1000804_Leck_mich_Zaertlichkeit_der_Rinder.JPG"));
//        categoryModelHashMap.put("4", new CategoryModel("004", "Fruit",
//                "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e8/20150728_xl_P1000804_Leck_mich_Zaertlichkeit_der_Rinder.JPG/220px-20150728_xl_P1000804_Leck_mich_Zaertlichkeit_der_Rinder.JPG"));
//        categoryModelHashMap.put("5", new CategoryModel("005", "Others",
//                "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e8/20150728_xl_P1000804_Leck_mich_Zaertlichkeit_der_Rinder.JPG/220px-20150728_xl_P1000804_Leck_mich_Zaertlichkeit_der_Rinder.JPG"));
//
//        productReferenceToSpinner.setValue(categoryModelHashMap);
//    }
}
