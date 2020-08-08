package com.java.yourfarmapp.ui.product;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.java.yourfarmapp.Model.ProductModel;
import com.java.yourfarmapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EditProductActivity extends AppCompatActivity {

    private EditText editCropName, editCropDescription, editCropPrice, editCropQuantity;
    private ImageView editCropImage;
    private Spinner editCategorySpinner;
    private Button editProductButton;

    private FirebaseDatabase mDatabase;
    private DatabaseReference userRef, productRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private StorageReference productImagesRef;

    private Uri imageUri = null;
    private static final int PICK_IMAGE_REQUEST = 1234;

    private String currentUserId, cropProductId, cropName, cropDescription, cropPrice, cropQuantity,
            downloadImageUrl;

    private ProductModel productModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        initializeView();
        initializeDatabase();
        retrieveProductData();

        editCropImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                openGallery();
            }
        });
    }

    private void initializeView() {
        editCropName = findViewById(R.id.edit_crop_name);
        editCropDescription = findViewById(R.id.edit_crop_description);
        editCropPrice = findViewById(R.id.edit_crop_price);
        editCropQuantity = findViewById(R.id.edit_crop_quantity);
        editCropDescription = findViewById(R.id.edit_crop_description);

        editCropImage = findViewById(R.id.edit_crop_image);
        editCategorySpinner = findViewById(R.id.edit_category_spinner);
        editProductButton = findViewById(R.id.edit_product_button);
    }

    private void initializeDatabase() {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance();
        userRef = mDatabase.getReference("User");
        productRef = mDatabase.getReference("Product");
        productImagesRef  = FirebaseStorage.getInstance().getReference().child("Images");

        currentUserId = mUser.getUid().toString();
    }

    private void retrieveProductData() {
        final String TAG = "retrieveProductData";

        cropProductId = getIntent().getStringExtra("cropProductId");

        Log.d(TAG, "ID: " + cropProductId);

        try {
            productRef.child(cropProductId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    cropName = dataSnapshot.child("cropName").getValue().toString();
                    cropDescription = dataSnapshot.child("cropDescription").getValue().toString();
                    cropPrice = dataSnapshot.child("cropPrice").getValue().toString();
                    cropQuantity = dataSnapshot.child("cropQuantity").getValue().toString();

                    editCropName.setText(cropName);
                    editCropDescription.setText(cropDescription);
                    editCropPrice.setText(cropPrice);
                    editCropQuantity.setText(cropQuantity);

                    editProductButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            submitToFirebase(cropProductId);
                        }
                    });

                    populateCategorySpinner();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } catch (NullPointerException e) {
            editCropName.setText("");
            editCropDescription.setText("");
            editCropPrice.setText("");
            editCropQuantity.setText("");

            populateCategorySpinner();
        }

    }

    private void submitToFirebase(String cropProductId) {
        ProductModel productModel = new ProductModel();

        int categorySelectedId = (int) editCategorySpinner.getSelectedItemId();

        String cropNameToString, cropPrice, cropQuantity, cropDescription;
        cropNameToString = editCropName.getText().toString();
        cropPrice = editCropPrice.getText().toString();
        cropQuantity = editCropQuantity.getText().toString();
        cropDescription = editCropDescription.getText().toString();

        productModel.setCropProductID(cropProductId);
        productModel.setCropName(cropNameToString);
        productModel.setCropPrice(cropPrice);
        productModel.setCropQuantity(cropQuantity);
        productModel.setCropDescription(cropDescription);
        productModel.setUserKey(userRef.child(mUser.getUid()).getKey());
//        productModel.setCropImage(downloadImageUrl);
        productModel.setProductCategoryName(editCategorySpinner.getSelectedItem().toString());
        productModel.setListId(categorySelectedId);

        userRef.child(currentUserId).child("fullName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String username = dataSnapshot.getValue(String.class);
                productRef.child(cropProductId).child("fullName").setValue(username);
                productModel.setFullName(username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        }); //Send fullName to product

        userRef.child(currentUserId).child("number").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String phoneNumber = dataSnapshot.getValue(String.class);
                productRef.child(cropProductId).child("number").setValue(phoneNumber);
                productModel.setNumber(phoneNumber);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        userRef.child(currentUserId).child("profilePic").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String profilePicture = dataSnapshot.getValue(String.class);
                productRef.child(cropProductId).child("farmerProfilePic").setValue(profilePicture);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        productRef.child(cropProductId).setValue(productModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(EditProductActivity.this, "Edited the product successfully", Toast.LENGTH_SHORT).show();
//                uploadPictureToFirebase();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {}
        });
    }

//    private void uploadPictureToFirebase() {
//        final String TAG = "uploadPictureToFirebase()";
//
//        if(imageUri == null) {
//            Toast.makeText(this, "Please upload an image.", Toast.LENGTH_SHORT).show();
//        }
//
//        String unique_name = UUID.randomUUID().toString();
//
//        try {
//
//            StorageReference filePath = productImagesRef.child(imageUri.getLastPathSegment() + unique_name);
//
//            final UploadTask uploadTask = filePath.putFile(imageUri);
//
//            uploadTask.addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(EditProductActivity.this, "Upload failed.", Toast.LENGTH_SHORT).show();
//                }
//            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(EditProductActivity.this, "Upload success.", Toast.LENGTH_SHORT).show();
//                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                        @Override
//                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                            if (!task.isSuccessful()) {
//                                throw task.getException();
//                            }
//                            downloadImageUrl = filePath.getDownloadUrl().toString();
//                            return filePath.getDownloadUrl();
//                        }
//                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Uri> task) {
//                            if (task.isSuccessful()) {
//                                downloadImageUrl = task.getResult().toString();
//                                productModel.setCropImage(downloadImageUrl);
//                                Toast.makeText(EditProductActivity.this, "I got the image", Toast.LENGTH_SHORT).show();
//                            }
//
//                            // Save to DB
//                        }
//                    });
//                }
//            });
//
//        } catch (NullPointerException e) {
//            Toast.makeText(this, "Please add an image.", Toast.LENGTH_SHORT).show();
//        }
//    }

    private void populateCategorySpinner() {
        List<String> category = new ArrayList<>();
        category.add(0, "Choose Category");
        category.add("Livestock");
        category.add("Vegetables");
        category.add("Rice");
        category.add("Fruits");
        category.add("Others");

        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,category);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        editCategorySpinner.setAdapter(dataAdapter);
    }

//    private void openGallery() {
//        Intent galleryIntent = new Intent();
//        galleryIntent.setType("image/*");
//        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(galleryIntent, "Select picture"), PICK_IMAGE_REQUEST);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
//            imageUri = data.getData();
//        }
//    }

}
