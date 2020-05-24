package com.java.yourfarmapp.ui.product;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.java.yourfarmapp.AddProductActivity;
import com.java.yourfarmapp.R;

public class ProductFragment extends Fragment {

    /**
     * To do
     *  1. Populate categories
     *  2.
     */

    private Button addProduct;

    private ProductViewModel productViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class); // Call view model
        View root = inflater.inflate(R.layout.fragment_product, container, false);

        Button button;

        button = root.findViewById(R.id.add_product_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddProductActivity.class);
                startActivity(intent);
            }
        });



        return root;
    }
}
