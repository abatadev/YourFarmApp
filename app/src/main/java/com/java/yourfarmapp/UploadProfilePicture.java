package com.java.yourfarmapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.java.yourfarmapp.Common.Common;
import com.java.yourfarmapp.Model.UserModel;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class UploadProfilePicture extends AppCompatActivity {
    private FirebaseAuth.AuthStateListener fireAuthListener;
    FirebaseUser currentUser;

    private static final int PICK_IMAGE_REQUEST = 1234;

    StorageReference profilePicImageRef;
    DatabaseReference userReference;
    FirebaseUser mUser;

    private Button uploadButton;
    private CircleImageView profilePicHolder;

    private Uri imageUri = null;

    UserModel userModel;

    private String downloadImageUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_profile_picture);

        uploadButton = findViewById(R.id.upload_profile_picture);
        profilePicHolder = findViewById(R.id.profile_picture_big);

        profilePicImageRef = FirebaseStorage.getInstance().getReference().child("Images");

        userModel = new UserModel();

        profilePicHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadPictureToFirebase();
            }
        });

        final String TAG = "firebaseAuth";
        fireAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();
                if (currentUser == null) {
                    Log.d(TAG, "Value | No user logged in");
                }
            }
        };

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            profilePicHolder.setImageURI(imageUri);
        }
    }

    private void openGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntent, "Select picture"), PICK_IMAGE_REQUEST);
    }

    private void uploadPictureToFirebase() {
        if(imageUri == null) {
            Toast.makeText(this, "Please upload an image.", Toast.LENGTH_SHORT).show();
        }

        String unique_name = UUID.randomUUID().toString();
        StorageReference filePath = profilePicImageRef.child(imageUri.getLastPathSegment() + unique_name);

        final UploadTask uploadTask = filePath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadProfilePicture.this, "Upload failed.", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(UploadProfilePicture.this, "Upload success.", Toast.LENGTH_SHORT).show();
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
                            userModel.setProfilePic(downloadImageUrl);
                            Toast.makeText(UploadProfilePicture.this, "I got the image", Toast.LENGTH_SHORT).show();
                            sendToFirebase();
                        } else {
                          Log.d("Error Debug", "TO DO");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UploadProfilePicture.this, "Failed", Toast.LENGTH_SHORT).show();
                        Log.d("onFailure", "Error: " + e);
                    }
                });
            }
        });
    }

    private void sendToFirebase() {
        final String TAG = "sendToFirebase()";
        userReference = FirebaseDatabase.getInstance().getReference().child("User");

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = mUser.getUid();

        userReference.child(userId).child("profilePic").setValue(downloadImageUrl)
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(UploadProfilePicture.this, "Image updated.", Toast.LENGTH_SHORT).show();
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadProfilePicture.this, "Image update failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }



}
