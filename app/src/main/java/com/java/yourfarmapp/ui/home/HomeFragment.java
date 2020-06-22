package com.java.yourfarmapp.ui.home;

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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.java.yourfarmapp.Model.CategoryModel;
import com.java.yourfarmapp.R;

import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    DatabaseReference categoriesRef;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        generateCategories();
        return root;
    }

    private void generateCategories() {
        //Only call this method to regenerate category tables

        categoriesRef = FirebaseDatabase.getInstance().getReference().child("Category");
        Map<String, CategoryModel> categoryModelHashMap = new HashMap<>();

        categoryModelHashMap.put("1", new CategoryModel("001" , "Livestock",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e8/20150728_xl_P1000804_Leck_mich_Zaertlichkeit_der_Rinder.JPG/220px-20150728_xl_P1000804_Leck_mich_Zaertlichkeit_der_Rinder.JPG\""));
        categoryModelHashMap.put("2", new CategoryModel("002", "Vegetables",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e8/20150728_xl_P1000804_Leck_mich_Zaertlichkeit_der_Rinder.JPG/220px-20150728_xl_P1000804_Leck_mich_Zaertlichkeit_der_Rinder.JPG"));
        categoryModelHashMap.put("3", new CategoryModel("003", "Rice",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e8/20150728_xl_P1000804_Leck_mich_Zaertlichkeit_der_Rinder.JPG/220px-20150728_xl_P1000804_Leck_mich_Zaertlichkeit_der_Rinder.JPG"));
        categoryModelHashMap.put("4", new CategoryModel("004", "Fruit",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e8/20150728_xl_P1000804_Leck_mich_Zaertlichkeit_der_Rinder.JPG/220px-20150728_xl_P1000804_Leck_mich_Zaertlichkeit_der_Rinder.JPG"));
        categoryModelHashMap.put("5", new CategoryModel("005", "Others",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e8/20150728_xl_P1000804_Leck_mich_Zaertlichkeit_der_Rinder.JPG/220px-20150728_xl_P1000804_Leck_mich_Zaertlichkeit_der_Rinder.JPG"));

        categoriesRef.setValue(categoryModelHashMap);
    }
}
