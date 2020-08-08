package com.java.yourfarmapp.ui.orders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.java.yourfarmapp.Model.OrderModel;
import com.java.yourfarmapp.R;

public class OrderAdapter extends FirebaseRecyclerAdapter<OrderModel, OrderAdapter.ViewHolder> {

    public OrderAdapter(@NonNull FirebaseRecyclerOptions<OrderModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int i, @NonNull OrderModel orderModel) {
//        holder.orderId.setText("Product ID: " + orderModel.getProductId());
        holder.orderProductName.setText("Product Name: " + orderModel.getProductName());
        holder.orderProductPrice.setText("Price: " + orderModel.getProductPrice());
        holder.orderProductDescription.setText("Description: " + orderModel.getProductDescription());
        holder.orderProductTime.setText("Time: " + orderModel.getOrderTime());
        holder.orderProductDate.setText("Date: " + orderModel.getOrderDate());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_list_orders, parent, false);
        return new OrderAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderId, orderProductName, orderProductPrice, orderProductDescription, orderProductDate, orderProductTime;

        public ViewHolder(View view) {
            super(view);
            orderId = view.findViewById(R.id.order_product_id);
            orderProductName = view.findViewById(R.id.order_product_name);
            orderProductPrice = view.findViewById(R.id.order_product_price);
            orderProductDescription = view.findViewById(R.id.order_product_description);
            orderProductDate = view.findViewById(R.id.order_product_date);
            orderProductTime = view.findViewById(R.id.order_product_time);

        }
    }
}
