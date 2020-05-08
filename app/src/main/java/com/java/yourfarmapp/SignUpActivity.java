package com.java.yourfarmapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.java.yourfarmapp.Common.Common;
import com.java.yourfarmapp.Model.UserModel;

public class SignUpActivity extends AppCompatActivity {

    private EditText inputEmail;
    private EditText inputPassword;
    private EditText inputConfirmPassword;

    private Button sign_up;

    private FirebaseAuth auth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        inputEmail = (EditText) findViewById(R.id.email_address);
        inputPassword = (EditText) findViewById(R.id.password);
        inputConfirmPassword = (EditText) findViewById(R.id.confirm_password);

        auth = FirebaseAuth.getInstance();

        sign_up = findViewById(R.id.button_sign_up);

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("User");
                //

                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)) {
                    Toast.makeText(SignUpActivity.this, "Please enter an email address.", Toast.LENGTH_SHORT).show();
                }
                if(TextUtils.isEmpty(password)) {
                    Toast.makeText(SignUpActivity.this, "Please enter a password", Toast.LENGTH_SHORT).show();
                }


                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                mUser = FirebaseAuth.getInstance().getCurrentUser();
                                String userId = mUser.getUid();

                                //String userId = mDatabase.push().getKey();

                                UserModel userModel = new UserModel(userId, email, password);
                                mDatabase.child(userId).setValue(userModel);

                                finish();

                                if(!task.isSuccessful()) {
                                    Toast.makeText(SignUpActivity.this, "Register failed.."  + task.getException(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SignUpActivity.this, "Register okay..", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
