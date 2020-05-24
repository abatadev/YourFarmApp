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
    private static final String TAG = "AddProduct.java";

    DatabaseReference productReferenceToSpinner;
    DatabaseReference categoryReference;
    DatabaseReference userReference;

    FirebaseUser mUser;
    DataSnapshot dataSnapshot;

    private ImageView cropImage;
    private TextView categoryTextView;

    private EditText cropName;
    private EditText cropPrice;
    private EditText cropQuantity;

    private Spinner categorySpinner;
    private Spinner quantitySpinner;

    private Button buttonSave;

    UserModel userModel;
    ProductModel productModel; // Model

    // https://stackoverflow.com/questions/38232140/how-to-get-the-key-from-the-value-in-firebase
    // https://stackoverflow.com/questions/37094631/get-the-pushed-id-for-specific-value-in-firebase-android
    // https://stackoverflow.com/questions/48893199/firebase-database-query-to-fetch-particular-data
    // https://stackoverflow.com/questions/57371314/how-to-get-data-from-another-table-according-to-a-retrieved-data-in-firebase

    @Override
    protected  void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        cropImage = (ImageView) findViewById(R.id.crop_image);
        categoryTextView = findViewById(R.id.category_text_view);
        cropName = (EditText) findViewById(R.id.crop_name);
        cropPrice = (EditText) findViewById(R.id.crop_price);
        cropQuantity = findViewById(R.id.crop_quantity);

        categorySpinner = findViewById(R.id.category_spinner);
        quantitySpinner = findViewById(R.id.quantity_spinner);

        buttonSave = (Button) findViewById(R.id.save_product_button);

        userModel = new UserModel();
        productModel = new ProductModel();

        loadCategoryListToSpinner();

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
        categoryReference = FirebaseDatabase.getInstance().getReference().child("ProductCategory");
        userReference = FirebaseDatabase.getInstance().getReference().child("User");

        productReferenceToSpinner = FirebaseDatabase.getInstance().getReference().child("ProductCategory")
                .child(String.valueOf(categorySpinner.getSelectedItemId() + 1)).child("Product"); // Load category list into spinner

//        categoryReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
//                    ProductModel productModel = new ProductModel();
//                    productModel.setProductCategoryID(categorySnapshot.getKey());
//                    tempList.add(productModel);
//                    Log.d(TAG, "List: " + Arrays.toString(tempList.toArray()));
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = mUser.getUid();

        userReference.orderByChild("uid").equalTo(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                    String key = snapshot1.getValue().toString();
                    Log.d(key, "key: " + key);
                    Toast.makeText(AddProductActivity.this, key, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "xxx" + categoryReference.equalTo(String.valueOf(categorySpinner.getSelectedItemId())).toString());

                    String test = (String.valueOf(categorySpinner.getSelectedItemId()));

                    categoryReference.equalTo(String.valueOf(categorySpinner.getSelectedItemId())).
                            orderByChild(snapshot1.getKey())
                            .equalTo("thisProductKey")
                            .addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        String dealerName = userModel.getEmail();
        String dealerId = userModel.getUID();

        String test = productReferenceToSpinner.getKey();
        Log.d(TAG, "" + productReferenceToSpinner + " test: " + test);


        productModel.setCropName(cropName.getText().toString());
        productModel.setCropPrice(cropPrice.getText().toString());
        productModel.setProductCategoryName(categorySpinner.getSelectedItem().toString());
        //productModel.setProductDescription
        productModel.setCropQuantity(cropQuantity.getText().toString());



        productReferenceToSpinner.push().setValue(productModel)
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
    }

    private void loadCategoryListToSpinner() {
        productReferenceToSpinner = FirebaseDatabase.getInstance().getReference();
        productReferenceToSpinner.child("ProductCategory").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<String> categoryList = new ArrayList<String>();

                for(DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    String productCategoryName = categorySnapshot.child("productCategoryName").getValue(String.class);
                    categoryList.add(productCategoryName);
                }

                Spinner spinner = (Spinner) findViewById(R.id.category_spinner);

                ArrayAdapter<String> categoryListAdapter = new ArrayAdapter<String>
                        (AddProductActivity.this,
                                android.R.layout.simple_spinner_dropdown_item,
                                categoryList);

                categoryListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner.setAdapter(categoryListAdapter);

                categoryTextView.setText(spinner.getSelectedItem().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void generateCategories() {
        //Only call this method to regenerate category tables

        productReferenceToSpinner = FirebaseDatabase.getInstance().getReference().child("ProductCategory");
        Map<String, ProductModel> productCategoryMap = new HashMap<>();

        productCategoryMap.put("1", new ProductModel("001" , "Livestock",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e8/20150728_xl_P1000804_Leck_mich_Zaertlichkeit_der_Rinder.JPG/220px-20150728_xl_P1000804_Leck_mich_Zaertlichkeit_der_Rinder.JPG\""));
        productCategoryMap.put("2", new ProductModel("002", "Vegetables",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e8/20150728_xl_P1000804_Leck_mich_Zaertlichkeit_der_Rinder.JPG/220px-20150728_xl_P1000804_Leck_mich_Zaertlichkeit_der_Rinder.JPG"));
        productCategoryMap.put("3", new ProductModel("003", "Rice",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e8/20150728_xl_P1000804_Leck_mich_Zaertlichkeit_der_Rinder.JPG/220px-20150728_xl_P1000804_Leck_mich_Zaertlichkeit_der_Rinder.JPG"));
        productCategoryMap.put("4", new ProductModel("004", "Fruit",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e8/20150728_xl_P1000804_Leck_mich_Zaertlichkeit_der_Rinder.JPG/220px-20150728_xl_P1000804_Leck_mich_Zaertlichkeit_der_Rinder.JPG"));
        productCategoryMap.put("5", new ProductModel("005", "Others",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e8/20150728_xl_P1000804_Leck_mich_Zaertlichkeit_der_Rinder.JPG/220px-20150728_xl_P1000804_Leck_mich_Zaertlichkeit_der_Rinder.JPG"));

        productReferenceToSpinner.setValue(productCategoryMap);
    }
}
