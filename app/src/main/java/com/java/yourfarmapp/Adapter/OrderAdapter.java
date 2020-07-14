package com.java.yourfarmapp.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.java.yourfarmapp.Model.OrderModel;
import com.java.yourfarmapp.R;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private List<OrderModel> orderModelList;

    public OrderAdapter(List<OrderModel> orderModelList) {
        this.orderModelList = orderModelList;
    }

    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_orders, parent, false);

        return new OrderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderModel list = orderModelList.get(position);
        //
        holder.orderProductTime.setText(orderModelList.get(position).getOrderTime());

        holder.orderProductName.setText(list.getProductName());
        holder.orderProductPrice.setText(list.getProductPrice());
        holder.orderProductDescription.setText(list.getProductDescription());
        holder.orderProductDate.setText(list.getOrderDate());
        holder.orderProductTime.setText(list.getOrderTime());

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView orderProductDate, orderProductTime, orderProductName,
                orderProductDescription, orderProductPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            orderProductDate = (TextView) itemView.findViewById(R.id.order_product_date);
            orderProductTime = (TextView) itemView.findViewById(R.id.order_product_time);
            orderProductName = (TextView) itemView.findViewById(R.id.order_product_name);
            orderProductDescription = (TextView) itemView.findViewById(R.id.order_product_description);
            orderProductPrice = (TextView) itemView.findViewById(R.id.order_product_price);

        }
    }
}
