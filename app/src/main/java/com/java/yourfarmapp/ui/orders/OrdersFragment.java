package com.java.yourfarmapp.ui.orders;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.java.yourfarmapp.Model.OrderModel;

import com.java.yourfarmapp.R;


public class OrdersFragment extends Fragment {

    OrdersViewModel ordersViewModel;
    private RecyclerView recyclerView;

    FirebaseUser mUser;

    private DatabaseReference orderRef;

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

        orderRef = FirebaseDatabase.getInstance().getReference().child("Order");

        displayOrders();

        return root;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ordersViewModel = ViewModelProviders.of(this).get(OrdersViewModel.class);
    }

    private void displayOrders() {


        mUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUser = mUser.getUid();

        Query query = orderRef.child(currentUser);

        FirebaseRecyclerOptions<OrderModel> options = new FirebaseRecyclerOptions.Builder<OrderModel>()
                .setQuery(query, OrderModel.class)
                .build();

        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<OrderModel, OrdersViewHolder>(options) {
            @NonNull
            @Override
            public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_orders, parent, false);
                return new OrdersViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull OrdersViewHolder ordersViewHolder, int position, @NonNull OrderModel orderModel) {
                /**
                 * This is an Order history for FARMER
                 * hide views if account type is DEALER
                 */

                String orderProductId = getRef(position).getKey();

                Log.d("Path: ", orderRef.child(currentUser).child(orderProductId).toString());

                orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                //farmerName = dataSnapshot.child("farmerName").getValue().toString();
                                //final String orderDate = dataSnapshot.child("orderDate").getValue().toString();
                                //final String orderTime = dataSnapshot.child("orderTime").getValue().toString();
                                //final String orderName = dataSnapshot.child("productName").getValue().toString();
                                final String orderDescription = dataSnapshot.child("productDescription").toString();
                                final String orderTotalPrice = dataSnapshot.child("productTotalPrice").toString();
                                //final String isComplete = dataSnapshot.child("complete").toString();

                                //ordersViewHolder.setOrderDate(orderDate);
                                //ordersViewHolder.setOrderTime(orderTime);
                                //ordersViewHolder.setProductName(orderName);
                                ordersViewHolder.setProductDescription(orderDescription);
                                ordersViewHolder.setProductTotalPrice(orderTotalPrice);

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


    public static class OrdersViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public OrdersViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setProductName(String productName) {
            TextView orderProductName = (TextView) mView.findViewById(R.id.order_product_name);
            orderProductName.setText(productName);
        }

        public void setProductDescription(String productDescription) {
            TextView orderProductDescription = (TextView) mView.findViewById(R.id.order_product_description);
            orderProductDescription.setText(productDescription);
        }

        public void setProductTotalPrice(String productTotalPrice) {
            TextView orderProductPrice = (TextView) mView.findViewById(R.id.order_product_price);
            orderProductPrice.setText(productTotalPrice);
        }

        public void setOrderDate(String orderDate) {
            TextView orderProductDate = (TextView) mView.findViewById(R.id.order_product_date);
            orderProductDate.setText(orderDate);
        }

        public void setOrderTime(String orderTime) {
            TextView orderProductTime = (TextView) mView.findViewById(R.id.order_product_time);
            orderProductTime.setText(orderTime);
        }

    }
}
