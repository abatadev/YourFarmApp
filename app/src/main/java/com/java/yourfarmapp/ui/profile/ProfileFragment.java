package com.java.yourfarmapp.ui.profile;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
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

    private static final String SOURCE = "readUserInformation()";

    private TextView fullName;
    private TextView user_address;
    private TextView profile_email_address;
    private TextView contact_number;

    private ImageView image_name;
    private ImageView image_address;
    private ImageView image_email;
    private ImageView image_number;

    private RadioButton dealer;
    private RadioButton farmer;

    private Button saveAccountType;

    private String currentUserId;
    private String firebaseDBName;

    View root = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class); // Call view model
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        //TextView textView = root.findViewById(R.id.text_gallery);
        fullName = root.findViewById(R.id.full_name);
        user_address = root.findViewById(R.id.user_address);
        profile_email_address = root.findViewById(R.id.user_email_address);
        contact_number = root.findViewById(R.id.user_contact_number);

        //Image
        image_name = root.findViewById(R.id.image_name_icon);
        image_address = root.findViewById(R.id.image_address_icon);
        image_email = root.findViewById(R.id.image_email_icon);
        image_number = root.findViewById(R.id.image_number_icon);

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

        readUserInformation();

        image_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Alert Dialog
                updateNameInformation();
            }
        });

        image_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAddressInformation();
            }
        });

        image_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateEmailInformation();
            }
        });

        image_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateNumberInformation();
            }
        });

        updateAccountType();

        return root;
    }

    public void retrieveFirebaseInformation() {
        ref = FirebaseDatabase.getInstance().getReference(Common.USER_REF);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUserId = mUser.getUid();

        firebaseDBName = ref.getKey();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference(Common.USER_REF).child(currentUserId);
        DatabaseReference profileRef = FirebaseDatabase.getInstance().getReference(Common.USER_REF);
    }

    private void readUserInformation() {
        final String TAG = "readUserInformation()";

        ref = FirebaseDatabase.getInstance().getReference(Common.USER_REF);
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        String currentUserId = mUser.getUid();
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

                fullName.setText(userModel.getFullName());
                user_address.setText(userModel.getAddress());
                profile_email_address.setText(userModel.getEmail());
                contact_number.setText(userModel.getNumber());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load database.", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void updateAccountType() {
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_profile, null);

        ref = FirebaseDatabase.getInstance().getReference(Common.USER_REF);
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        String currentUserId = mUser.getUid();
        String firebaseDBName = ref.getKey();

        dealer = itemView.findViewById(R.id.is_dealer);
        farmer = itemView.findViewById(R.id.is_farmer);
        Button saveAccountType = itemView.findViewById(R.id.button_type);
        RadioGroup radioGroup = itemView.findViewById(R.id.radio_group);

        final String TAG = "updateAccountType()";

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference(Common.USER_REF).child(currentUserId);
        DatabaseReference profileRef = FirebaseDatabase.getInstance().getReference(Common.USER_REF);

        Log.d(TAG, "Starting " + TAG);


        radioGroup.clearCheck();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedID) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedID);
            }
        });

//        isDealer = true;
//        isFarmer = false;
//
//        profileRef.child(currentUserId).child("dealer").setValue(isDealer);
//
//        userModel.setFarmer(isFarmer);
//        userModel.setDealer(isDealer);


        saveAccountType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Submitting...", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onClick" + TAG);

                int selectedId = radioGroup.getCheckedRadioButtonId();

                if(selectedId == -1) {
                    Toast.makeText(getContext(), "No account type selected", Toast.LENGTH_SHORT).show();
                } else {
                    RadioButton radioButton = radioGroup.findViewById(selectedId);
                    if(selectedId == 1) {
                        Toast.makeText(getContext(), "Option 1: Dealer", Toast.LENGTH_SHORT).show();
                    } else if(selectedId == 2) {
                        Toast.makeText(getContext(), "Option 2: Farmer", Toast.LENGTH_SHORT).show();
                    }
                }

                Toast.makeText(getContext(), "Submitted...", Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void updateNameInformation() {
        ref = FirebaseDatabase.getInstance().getReference(Common.USER_REF);
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        String currentUserId = mUser.getUid();
        String firebaseDBName = ref.getKey();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference(Common.USER_REF).child(currentUserId);
        DatabaseReference profileRef = FirebaseDatabase.getInstance().getReference(Common.USER_REF);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Change name");
        builder.setMessage("Please complete the following information");

        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.layout_update_name, null);
        EditText edit_name =itemView.findViewById(R.id.dialog_edit_name);

        builder.setNegativeButton("Cancel", ((dialogInterface, i) -> dialogInterface.dismiss()));

        builder.setPositiveButton("Update", ((dialogInterface, i) -> {
            String fullName = edit_name.getText().toString();
            profileRef.child(currentUserId).child("fullName").setValue(fullName);

            // Add testing 
        }));

        builder.setView(itemView);
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void updateAddressInformation() {
        ref = FirebaseDatabase.getInstance().getReference(Common.USER_REF);
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        String currentUserId = mUser.getUid();
        String firebaseDBName = ref.getKey();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference(Common.USER_REF).child(currentUserId);
        DatabaseReference profileRef = FirebaseDatabase.getInstance().getReference(Common.USER_REF);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Change Address");
        builder.setMessage("Please complete the following information");

        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.layout_update_address, null); //Change
        EditText editAddress =itemView.findViewById(R.id.dialog_edit_address);

        builder.setNegativeButton("Cancel", ((dialogInterface, i) -> dialogInterface.dismiss()));

        builder.setPositiveButton("Update", ((dialogInterface, i) -> {
            String address = editAddress.getText().toString();
            profileRef.child(currentUserId).child("address").setValue(address);

            // Add testing
        }));

        builder.setView(itemView);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateEmailInformation() {
        ref = FirebaseDatabase.getInstance().getReference(Common.USER_REF);
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String currentUserId = mUser.getUid();
        String firebaseDBName = ref.getKey();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference(Common.USER_REF).child(currentUserId);
        DatabaseReference profileRef = FirebaseDatabase.getInstance().getReference(Common.USER_REF);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Change Email");
        builder.setMessage("Please complete the following information");

        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.layout_update_email, null); //Change
        EditText editEmail =itemView.findViewById(R.id.dialog_edit_email);

        builder.setNegativeButton("Cancel", ((dialogInterface, i) -> dialogInterface.dismiss()));

        builder.setPositiveButton("Update", ((dialogInterface, i) -> {
            String emailAddress = editEmail.getText().toString();
            editEmail.setText(emailAddress);

            user.updateEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getContext(), "Update email success.", Toast.LENGTH_SHORT).show();
                        profileRef.child(currentUserId).child("email").setValue(emailAddress);
                        editEmail.setText(emailAddress);
                        Log.d(TAG, "Email update is successful.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Failed to update email address. " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "E: " + e.getMessage());
                    }
                });
        }));

        builder.setView(itemView);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateNumberInformation() {
        ref = FirebaseDatabase.getInstance().getReference(Common.USER_REF);
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        String currentUserId = mUser.getUid();
        String firebaseDBName = ref.getKey();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference(Common.USER_REF).child(currentUserId);
        DatabaseReference profileRef = FirebaseDatabase.getInstance().getReference(Common.USER_REF);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Change Contact Number");
        builder.setMessage("Please complete the following information");

        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.layout_update_number, null); //Change
        EditText editNumber = itemView.findViewById(R.id.dialog_edit_number);

        builder.setNegativeButton("Cancel", ((dialogInterface, i) -> dialogInterface.dismiss()));

        builder.setPositiveButton("Update", ((dialogInterface, i) -> {
            String number = editNumber.getText().toString();
            profileRef.child(currentUserId).child("number").setValue(number);

            // Add testing
        }));

        builder.setView(itemView);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}


