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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.java.yourfarmapp.Model.CategoryModel;
import com.java.yourfarmapp.Model.ProductModel;
import com.java.yourfarmapp.Model.UserModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddProductActivity extends AppCompatActivity {

    private static final String TAG = "AddProduct.java";

    DatabaseReference productReference;
    DatabaseReference categoryReference;

    private ImageView cropImage;

    private TextView categoryTextView;

    private EditText cropName;
    private EditText cropPrice;
    private EditText cropQuantity;

    private Spinner categorySpinner;
    private Spinner quantitySpinner;

    private Button buttonSave;

    ProductModel productModel; // Model

    ArrayList<CategoryModel> categoryList = new ArrayList<>();

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

        productModel = new ProductModel();


        buttonSave = (Button) findViewById(R.id.save_product_button);

        UserModel userModel = new UserModel();

        //generateCategories();
        loadCategoryList();

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               submitProductEntry();
            }
        });

    }

    private void uploadProductImage() {

    }

    private void submitProductEntry() {
        final String TAG = "submitProductEntry()";

        productReference = FirebaseDatabase.getInstance().getReference().child("ProductCategory").child(String.valueOf(categorySpinner.getSelectedItemId() + 1)).child("Product");
        //productReference = FirebaseDatabase.getInstance().getReference().child("ProductCategory").getKey();

        Log.d(TAG, "" + productReference);

        productModel.setCropName(cropName.getText().toString());
        productModel.setCropPrice(cropPrice.getText().toString());
        productModel.setProductCategoryName(categorySpinner.getSelectedItem().toString());
        productModel.setCropQuantity(cropQuantity.getText().toString());
        int id = categorySpinner.getId();

        productReference.push().setValue(productModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        //productReference.push().setValue(categoryModel);

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

    private void loadCategoryList() {
        productReference = FirebaseDatabase.getInstance().getReference();

        productReference.child("ProductCategory").addValueEventListener(new ValueEventListener() {

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

        productReference = FirebaseDatabase.getInstance().getReference().child("ProductCategory");
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

        productReference.setValue(productCategoryMap);
    }

    private void loadDataInSpinner() {
//        categoryArrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, categoryList);
//        spinnerPostCategory.setAdapter(categoryArrayAdapter);

    }

}
