package com.java.yourfarmapp.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.java.yourfarmapp.Common.Common;
import com.java.yourfarmapp.Model.UserModel;
import com.java.yourfarmapp.R;
import com.java.yourfarmapp.SignInActivity;
import com.java.yourfarmapp.ui.product.ProductViewModel;

public class ProfileFragment extends Fragment {

    private ProductViewModel productViewModel;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener fireAuthListener;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser mUser;
    private DatabaseReference ref;

    UserModel userModel;

    private static final String TAG = "ProfileFragment.java";

    private TextView fullName;
    private TextView user_address;
    private TextView profile_email_address;
    private TextView user_contact_number;

    View root = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class); // Call view model
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        //TextView textView = root.findViewById(R.id.text_gallery);
        fullName = root.findViewById(R.id.full_name);
        user_address = root.findViewById(R.id.user_address);
        profile_email_address = root.findViewById(R.id.user_email_address);
        user_contact_number = root.findViewById(R.id.user_contact_number);

        fireAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();

                if (currentUser == null) {
                    //user not login
                    Log.d(TAG, "Value | No user logged in");
                }
            }
        };

        firebaseHelper();
        readUserInformation();


        return root;
    }

    public void firebaseHelper() {

    }

    public void readUserInformation() {
        ref = FirebaseDatabase.getInstance().getReference(Common.USER_REF);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = mUser.getUid();
        //String currentUser = mUser.getUid().toString();

        //Change UID value

        String firebaseDBName = ref.getKey();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference(Common.USER_REF).child(currentUserId);
        DatabaseReference profileRef = FirebaseDatabase.getInstance().getReference(Common.USER_REF);

        profileRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                userModel = dataSnapshot.getValue(UserModel.class);

                Log.d(TAG, "Value for Reference DB: " + firebaseDBName);
                Log.d(TAG, "Value for database is : " + rootRef.toString());
                Log.d(TAG, "Value for USER ID: " + currentUserId);
                Log.d(TAG, "Value for Current user logged in is: " + mUser);
                Log.d(TAG, "Value for email: " + userModel.getEmail());

                //fullName.setText(userModel.getFullName());
                //user_address.setText(userModel.getAddress());
                profile_email_address.setText(userModel.getEmail());
                //user_contact_number.setText(userModel.getContactNumber());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load database.", Toast.LENGTH_SHORT).show();
            }
        });

        profileRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Log.d(TAG, "Value for email: " + userModel.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }
}

