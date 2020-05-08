package com.java.yourfarmapp.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.java.yourfarmapp.R;
import com.java.yourfarmapp.ui.product.ProductViewModel;

public class ProfileFragment extends Fragment {

    private ProductViewModel productViewModel;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class); // Call view model
        View root = inflater.inflate(R.layout.fragment_product, container, false);

        final TextView textView = root.findViewById(R.id.text_gallery);
        final TextView fullName = root.findViewById(R.id.full_name);
        final TextView user_address = root.findViewById(R.id.user_address);
        final TextView user_email_address = root.findViewById(R.id.user_email_address);
        final TextView user_contact_number = root.findViewById(R.id.user_contact_number);

        //final TextView account_type;
        productViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                fullName.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }
}

