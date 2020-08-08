package com.java.yourfarmapp.ui.product;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.java.yourfarmapp.Model.ProductModel;
import com.java.yourfarmapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductFragment extends Fragment {

    private LinearLayout productControlView;
    private Button addProductButton, editProductButton, deleteProductButton;
    private Context context;
    private List<ProductModel> productModelList;
    private EditText productTitle, productDescription;
    private ImageView productImage;
    private RecyclerView recyclerView;
    private DatabaseReference dbProduct, userRef, productRef;
    private FirebaseUser mUser;
    private String userId, productId;
    private boolean accountIsFarmer;

    private FirebaseRecyclerOptions<ProductModel> options;
    private FirebaseRecyclerAdapter<ProductModel, ProductViewHolder> adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_product, container, false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        recyclerView = (RecyclerView) root.findViewById(R.id.list);
        recyclerView.setLayoutManager(linearLayoutManager);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = mUser.getUid();

        initializeContentView(root);
        initializeDatabase();
        checkUserAccountType(userId);
        displayProducts();

        return root;
    }

    private void checkUserAccountType(String userId) {
        final String TAG ="checkUserAccountType";

        Log.d(TAG, "User ID: " + userId);
        userRef.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                accountIsFarmer = dataSnapshot.child("farmer").getValue(Boolean.class);
                if(accountIsFarmer == true) {
                    // Show button
                    accountIsFarmer = true;
                    addProductButton.setVisibility(View.VISIBLE);

                    addProductButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getContext(), AddProductActivity.class);
                            startActivity(intent);
                        }
                    });
                } else if (accountIsFarmer == false){
                    // Hide button
                    accountIsFarmer = false;
                    addProductButton.setVisibility(View.GONE);
                } else {
                    // For good measure
                    accountIsFarmer = false;
                    addProductButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initializeContentView(View root) {
        productImage = root.findViewById(R.id.crop_image_mini);
        editProductButton = root.findViewById(R.id.edit_product_button);
        deleteProductButton = root.findViewById(R.id.delete_product_button);
        productControlView = root.findViewById(R.id.product_control_layout);
        addProductButton = root.findViewById(R.id.add_product);
    }

    private void initializeDatabase() {
        dbProduct = FirebaseDatabase.getInstance().getReference("Product");
        userRef = FirebaseDatabase.getInstance().getReference("User");
        productRef = FirebaseDatabase.getInstance().getReference("Product");
    }


    private void displayProducts() {
        final String TAG = "displayProducts";
        //Query query = productRef.orderByChild("maidName").startAt(data).endAt(data + "\uf8ff");

        if(accountIsFarmer == true) { // Load own products if farmer
            options = new FirebaseRecyclerOptions.Builder<ProductModel>().setQuery(FirebaseDatabase.getInstance().getReference("Product").child(userId), ProductModel.class).build();
        } else { // Load all products if dealer
            options = new FirebaseRecyclerOptions.Builder<ProductModel>().setQuery(FirebaseDatabase.getInstance().getReference("Product"), ProductModel.class).build();
        }

        adapter = new FirebaseRecyclerAdapter<ProductModel, ProductViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull final ProductViewHolder productViewHolder, int i, @NonNull ProductModel productModel) {
                productId = productModel.getCropProductID();

                productViewHolder.productNameTextView.setText(productModel.getCropName());
                productViewHolder.productDescriptionTextView.setText(productModel.getCropDescription());
                productViewHolder.productPriceTextView.setText(productModel.getCropPrice());

                Picasso.get().load(productModel.getCropImage()).fit().into(productViewHolder.productImage);

                Log.d(TAG, "Farmer: " + accountIsFarmer);

                if(accountIsFarmer == true) {
                    productViewHolder.editProductButton.setVisibility(View.VISIBLE);
                    productViewHolder.deleteProductButton.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "Welcome back farmer!", Toast.LENGTH_SHORT).show();

                    productViewHolder.editProductButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getContext(), EditProductActivity.class);
                            intent.putExtra("cropProductId", getRef(i).getKey());
                            startActivity(intent);
                        }
                    });

                    productViewHolder.deleteProductButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String deleteProductKey = getRef(i).getKey();
                            deleteProductEntry(deleteProductKey);
                        }
                    });

                } else {
                    productViewHolder.editProductButton.setVisibility(View.GONE);
                    productViewHolder.deleteProductButton.setVisibility(View.GONE);
                }


                productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String cropProductId = getRef(i).getKey();

                        Intent intent = new Intent(getContext(), ViewProductItemActivity.class);

                        intent.putExtra("cropProductId", cropProductId);
                        startActivity(intent);
                    }
                });
            }


            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_list_product, parent, false);
                return new ProductViewHolder(view);
            }

        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    private void deleteProductEntry(String deleteProductKey) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Delete this Maid entry?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                productRef.child(deleteProductKey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Product has been deleted from the database.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Delete failed.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.create();
        builder.show();

    }

}
