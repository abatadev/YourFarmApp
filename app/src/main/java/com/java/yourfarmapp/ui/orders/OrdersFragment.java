package com.java.yourfarmapp.ui.orders;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.java.yourfarmapp.Adapter.OrderAdapter;
import com.java.yourfarmapp.Model.MessagesModel;
import com.java.yourfarmapp.Model.OrderModel;

import com.java.yourfarmapp.R;

import java.util.ArrayList;
import java.util.List;


public class OrdersFragment extends Fragment {

    OrdersViewModel ordersViewModel;
    private RecyclerView recyclerView;

    FirebaseUser mUser;
    FirebaseAuth mAuth;

    private DatabaseReference orderRef;
    private List<OrderModel> orderModelList;

    private Context mContext;

    private OrderAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ordersViewModel =
                ViewModelProviders.of(this).get(OrdersViewModel.class);
        View root = inflater.inflate(R.layout.fragment_orders, container, false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(false);
        linearLayoutManager.setStackFromEnd(false);

        recyclerView = (RecyclerView) root.findViewById(R.id.order_list);
        recyclerView.setLayoutManager(linearLayoutManager);

        orderModelList = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();
        orderRef = FirebaseDatabase.getInstance().getReference().child("Order").child(userId);

        Log.d("OrdersFragment", "OrdersFragment Path: " + orderRef);
        Log.d("OrdersFragment", "Current User ID: " + userId);

        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        OrderModel orderModel = new OrderModel();

                        orderModel.setProductName(dataSnapshot.child("productName").toString());

                        orderModelList.add(orderModel);
                    }

                    adapter = new OrderAdapter(orderModelList);
                    recyclerView.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        recyclerView.setAdapter(adapter);

        return root;
    }
}



