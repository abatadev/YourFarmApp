package com.java.yourfarmapp.ui.orders;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.java.yourfarmapp.Model.OrderModel;

import com.java.yourfarmapp.R;

import java.util.List;


public class OrdersFragment extends Fragment {

    private RecyclerView recyclerView;

    FirebaseUser mUser;
    FirebaseAuth mAuth;

    private DatabaseReference orderRef;
    private List<OrderModel> orderModelList;

    private OrderAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_orders, container, false);

        initializeView(root);
        loadOrders();

        recyclerView.setAdapter(adapter);
        return root;
    }

    private void loadOrders() {
        final String TAG = "loadOrders";

        String currentUserId = mUser.getUid();
        Log.d(TAG, "User ID: " + currentUserId);

        FirebaseRecyclerOptions<OrderModel> options =
                new FirebaseRecyclerOptions.Builder<OrderModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference("Order")
                .child(currentUserId),
                        OrderModel.class)
                .build();

        adapter = new OrderAdapter(options);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void initializeView(View root) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(false);
        linearLayoutManager.setStackFromEnd(false);

        recyclerView = (RecyclerView) root.findViewById(R.id.order_list);
        recyclerView.setLayoutManager(linearLayoutManager);

        mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();
        orderRef = FirebaseDatabase.getInstance().getReference().child("Order").child(userId);

        Log.d("OrdersFragment", "OrdersFragment Path: " + orderRef);
        Log.d("OrdersFragment", "Current User ID: " + userId);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
    }
}



