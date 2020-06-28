package com.java.yourfarmapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import com.java.yourfarmapp.Model.CategoryModel;
import com.java.yourfarmapp.Model.ProductModel;
import com.java.yourfarmapp.Model.UserModel;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AddProductActivity extends AppCompatActivity {
    private FirebaseAuth.AuthStateListener fireAuthListener;

    FirebaseUser currentUser;

    private static final String TAG = "AddProduct.java";
    private static final int PICK_IMAGE_REQUEST = 1234;

    DatabaseReference productReference;
    DatabaseReference userReference;
    DatabaseReference categoryReference;

    StorageReference productImagesRef;

    FirebaseUser mUser;

    private ImageView cropImage;
    private Uri imageUri = null;

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

    private String downloadImageUrl;

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
//
//        categorySpinner = findViewById(R.id.category_spinner);
//        quantitySpinner = findViewById(R.id.quantity_spinner);

        buttonSave = (Button) findViewById(R.id.save_product_button);

        productImagesRef  = FirebaseStorage.getInstance().getReference().child("Images");

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
               uploadPictureToFirebase();
            }
        });

        cropImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

    }

    private void openGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntent, "Select picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            cropImage.setImageURI(imageUri);
        }
    }

    private void uploadPictureToFirebase() {
        final String TAG = "uploadPictureToFirebase()";

        if(imageUri == null) {
            Toast.makeText(this, "Please upload an image.", Toast.LENGTH_SHORT).show();
        }

        String unique_name = UUID.randomUUID().toString();
        StorageReference filePath = productImagesRef.child(imageUri.getLastPathSegment() + unique_name);

        final UploadTask uploadTask = filePath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddProductActivity.this, "Upload failed.", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AddProductActivity.this, "Upload success.", Toast.LENGTH_SHORT).show();
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()) {
                            throw task.getException();
                        }
                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()) {
                            downloadImageUrl = task.getResult().toString();
                            productModel.setCropImage(downloadImageUrl);
                            Toast.makeText(AddProductActivity.this, "I got the image", Toast.LENGTH_SHORT).show();
                            sendProductToFirebase();
                        }

                        // Save to DB
                    }
                });
            }
        });
    }

    private void sendProductToFirebase() {
        categoryReference = FirebaseDatabase.getInstance().getReference().child("Category");
        userReference = FirebaseDatabase.getInstance().getReference().child("User");
        productReference = FirebaseDatabase.getInstance().getReference().child("Product");

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = mUser.getUid();

        String productId = productReference.push().getKey();
        String categoryId = categoryReference.getKey();

        productModel.setCropProductID(productId);
        productModel.setCropName(cropName.getText().toString());
        productModel.setCropPrice(cropPrice.getText().toString());
        productModel.setCropQuantity(cropQuantity.getText().toString());
        productModel.setCropDescription(cropDescription.getText().toString());
        productModel.setUserKey(userReference.child(mUser.getUid()).getKey());
        productModel.setCropImage(downloadImageUrl);

        Log.d(TAG, "IMAGE URL: " + downloadImageUrl);

        userReference.child(userId).child("fullName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String username = dataSnapshot.getValue(String.class);
                productReference.child(productId).child("fullName").setValue(username);
                productModel.setFullName(username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        }); //Send fullName to product

        userReference.child(userId).child("number").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String phoneNumber = dataSnapshot.getValue(String.class);
                productReference.child(productId).child("number").setValue(phoneNumber);
                productModel.setNumber(phoneNumber);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }); //Send phone number to product

        userReference.child(userId).child("dealer").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean dealer = dataSnapshot.getValue(boolean.class);
                productReference.child(productId).child("dealer").setValue(dealer);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        userReference.child(userId).child("farmer").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean farmer = dataSnapshot.getValue(boolean.class);
                productReference.child(productId).child("farmer").setValue(farmer);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        userReference.child(userId).child("profilePic").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String profilePicture = dataSnapshot.getValue(String.class);
                productReference.child(productId).child("farmerProfilePic").setValue(profilePicture);
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
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {}
        });
    }
}
