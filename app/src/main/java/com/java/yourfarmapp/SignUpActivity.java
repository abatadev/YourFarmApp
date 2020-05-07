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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.java.yourfarmapp.Model.User;

public class SignUpActivity extends AppCompatActivity {

    private EditText username, full_name, phone_number, email_address, physical_address, edit_text_password, confirm_password;
    private Button button_sign_up;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    private RadioButton rb_farmer, rb_dealer;

    private boolean isDealer;
    private boolean isFarmer;

    private static final String TAG = "SignUpActivity.java";

    FirebaseDatabase database;
    DatabaseReference databaseReference;

    User userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        full_name = (EditText) findViewById(R.id.full_name);
        phone_number = (EditText) findViewById(R.id.phone_number);
        email_address = (EditText) findViewById(R.id.email_address);
        physical_address = (EditText) findViewById(R.id.physical_address);
        edit_text_password = (EditText) findViewById(R.id.password);
        confirm_password = (EditText) findViewById(R.id.confirm_password);

        button_sign_up = (Button) findViewById(R.id.button_sign_up);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        rb_farmer = findViewById(R.id.radio_button_farmer);
        rb_dealer = findViewById(R.id.radio_button_dealer);

        userModel = new User();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("User");

        auth = FirebaseAuth.getInstance();

        button_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = email_address.getText().toString().trim();
                String password = edit_text_password.getText().toString().trim();
                String confirmInputPassword = confirm_password.getText().toString().trim();

                //Add confirmation before proceeding
                if(TextUtils.isEmpty(email)) {
                    Toast.makeText(SignUpActivity.this, "Enter a valid email address.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(password)) {
                    Toast.makeText(SignUpActivity.this, "Enter your password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(SignUpActivity.this, "Password must be greater than 6 characters.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirmInputPassword)) {
                    Toast.makeText(SignUpActivity.this, "Your passwords should match.", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                String full_name_input = full_name.getText().toString();
                String phone_number_input = phone_number.getText().toString();
                String address = physical_address.getText().toString();
                userModel = new User(email, password, phone_number_input, full_name_input, address, isFarmer, isDealer);

                registerUser(email, password);
            }
        });

    }

    public void registerUser(String email, String password) {
        // Initialize create user
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SignUpActivity.this, "Register okay!", Toast.LENGTH_SHORT).show();

                        if(task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // Push
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed." + task.getException(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void updateUI(FirebaseUser currentUser) {
        String keyId = databaseReference.push().getKey();
        databaseReference.child(keyId).setValue(userModel);
        Intent loginIntent = new Intent(this, MainActivity.class);
        startActivity(loginIntent);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton)view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {

            case R.id.radio_button_dealer:
                if (checked) {
                    isDealer = true;
                    isFarmer = false;
                    break;
                }

            case R.id.radio_button_farmer:
                if (checked) {
                    isFarmer = true;
                    isDealer = false;
                    break;
                }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //progressBar.setVisibility(View.GONE);
    }
}
