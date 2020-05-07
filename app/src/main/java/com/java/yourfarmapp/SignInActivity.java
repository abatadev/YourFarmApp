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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    private EditText emailAddressInput, passwordInput;
    private Button signInButton;
    private Button signUpButton;
    private ProgressBar progressBar;

    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get Firebase Authentication Instance
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) { // Redirect if there is an existing user.
            startActivity(new Intent(SignInActivity.this, HomeActivity.class));
            finish();
        }

        setContentView(R.layout.activity_sign_in);

        emailAddressInput = (EditText) findViewById(R.id.email_address_input);
        passwordInput = (EditText) findViewById(R.id.password_input);

        signInButton = (Button) findViewById(R.id.button_login);
        signUpButton = (Button) findViewById(R.id.button_sign_up);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        // Get Firebase Authentication Instance
        auth = FirebaseAuth.getInstance();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailAddressInput.getText().toString();
                final String password = passwordInput.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(SignInActivity.this, "Email address cannot be empty.", Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(SignInActivity.this, "Password field cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                // Authenticate the user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    // Error
                                    if (password.length() < 6) {
                                        passwordInput.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(SignInActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });

            }
        });

    }
}