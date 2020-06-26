package com.java.yourfarmapp.ui.product;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.java.yourfarmapp.AddProductActivity;
import com.java.yourfarmapp.Model.ProductModel;
import com.java.yourfarmapp.R;
import com.java.yourfarmapp.ViewProductItem;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;

public class ProductFragment extends Fragment {

    private Button addProductButton;
    private Context context;

    private List<ProductModel> productModelList;


    private EditText productTitle;
    private EditText productDescription;
    ImageView productImage;

    private RecyclerView recyclerView;

    DatabaseReference dbProduct;
    DatabaseReference userRef;

    ProductViewModel productViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {



        productViewModel = ViewModelProviders.of(   this).get(ProductViewModel.class); // Call view model
        View root = inflater.inflate(R.layout.fragment_product, container, false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(false);
        linearLayoutManager.setStackFromEnd(false);

        productImage = root.findViewById(R.id.crop_image_mini);

        recyclerView = (RecyclerView) root.findViewById(R.id.list);
        recyclerView.setLayoutManager(linearLayoutManager);

        addProductButton = root.findViewById(R.id.add_product);
        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddProductActivity.class);
                startActivity(intent);
            }
        });

        dbProduct = FirebaseDatabase.getInstance().getReference("Product");
        userRef = FirebaseDatabase.getInstance().getReference("User");

        displayProducts();

        return root;
    }

    public void displayProducts() {
        final String TAG = "displayProducts";

        Query query = dbProduct;

        FirebaseRecyclerOptions<ProductModel> options = new FirebaseRecyclerOptions.Builder<ProductModel>().setQuery(query, ProductModel.class).build();

        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<ProductModel, ProductViewHolder>(options) {
            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_product, parent, false);
                return new ProductViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int position, @NonNull ProductModel productModel) {
                final String cropProductId = getRef(position).getKey(); // Note

                dbProduct.child(cropProductId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            final String productTitle = dataSnapshot.child("cropName").getValue().toString();
                            final String productDescription = dataSnapshot.child("cropDescription").getValue().toString();
                            final String productPrice = dataSnapshot.child("cropPrice").getValue().toString();
                            final String productImageString = dataSnapshot.child("cropImage").getValue().toString();

                            //View holder to set name
                            productViewHolder.setProductName(productTitle);
                            productViewHolder.setProductDescription("Description: " + productDescription);
                            productViewHolder.setProductPrice("Price: " + productPrice);
                            productViewHolder.setCropPicture(productImageString);

                            productViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Log.d(TAG, "Product ID: " + cropProductId);
                                    Toast.makeText(getContext(), "ON CLICK TESTING!", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(getContext(), ViewProductItem.class);
                                    intent.putExtra("cropProductId", productModel.getCropProductID());
                                    startActivity(intent);

                                }
                            });

//                            Glide.with(context).load(productModel.getCropImage()).into(productViewHolder.productImage);

                        } else {
                            Log.d(TAG, "DB Product: " + dbProduct.getRef().getKey().toString());
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

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        private Unbinder unbinder;
        @BindView(R.id.product_image)
        ImageView productImage;

        View mView;

        public ProductViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setProductName(String fullName){
            TextView myName = (TextView) mView.findViewById(R.id.product_name);
            myName.setText(fullName);
        }

        public void setProductDescription(String description){
            TextView productDescription = (TextView) mView.findViewById(R.id.product_description);
            productDescription.setText(description);
        }

        public void setProductPrice(String price) {
            TextView productPrice = (TextView) mView.findViewById(R.id.product_price);
            productPrice.setText(price);

        }

        public void setCropPicture(String picture) {
            ImageView productPicture = mView.findViewById(R.id.crop_image_mini);
            Picasso.get().load(picture).into(productPicture);
        }

    }

}
