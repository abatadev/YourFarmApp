package com.java.yourfarmapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.java.yourfarmapp.Model.CategoryModel;
import com.java.yourfarmapp.Model.ProductModel;
import com.java.yourfarmapp.R;
import com.java.yourfarmapp.ui.product.ProductFragment;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private CategoryModel categoryModel;

    private RecyclerView recyclerView;

    private DatabaseReference categoriesRef;
    private DatabaseReference categoryQuery;
    
    private RelativeLayout layoutTest;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(false);
        linearLayoutManager.setStackFromEnd(false);

        recyclerView = (RecyclerView) root.findViewById(R.id.category_list);
        recyclerView.setLayoutManager(linearLayoutManager);

        categoriesRef = FirebaseDatabase.getInstance().getReference().child("Category");

        displayCategories();

        return root;
    }

    private void displayCategories() {
        Query query = categoriesRef;

        FirebaseRecyclerOptions<CategoryModel> options = new FirebaseRecyclerOptions.Builder<CategoryModel>()
                .setQuery(query, CategoryModel.class)
                .build();

        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<CategoryModel, HomeViewHolder>(options) {
            @NonNull
            @Override
            public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_category, parent, false);
                return new HomeViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull HomeViewHolder homeViewHolder, int position, @NonNull CategoryModel categoryModel) {
                final String categoryId = getRef(position).getKey(); // Note
                Log.d("onBindViewHolder", "Category ID: " + categoryId);

                categoriesRef.child(categoryId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            final String categoryNameToString =  dataSnapshot.child("categoryName").getValue().toString();
                            final String categoryImageToString = dataSnapshot.child("categoryImage").getValue().toString();
                            final String categoryId = dataSnapshot.child("categoryID").getValue().toString();

                            homeViewHolder.setCategoryName(categoryNameToString);
                            homeViewHolder.setCategoryImage(categoryImageToString);
                            
                            homeViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(getContext(), ProductFragment.class);
                                    intent.putExtra("categoryId", categoryId);
                                    startActivity(intent);
                                }
                            });
                            
                        } else {
                            Toast.makeText(getContext(), "Unable to find categories.", Toast.LENGTH_SHORT).show();
                            Log.d("adapter", "Does not exist");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                
            }

        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    public static class HomeViewHolder extends RecyclerView.ViewHolder {
        View mView;
        ImageView categoryImage;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setCategoryName(String name) {
            TextView categoryName = mView.findViewById(R.id.category_name);
            categoryName.setText(name);
        }

        public void setCategoryImage(String image) {
            ImageView categoryImage = mView.findViewById(R.id.category_image);
            Picasso.get().load(image).fit().into(categoryImage);
        }
    }

}
