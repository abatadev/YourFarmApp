package com.java.yourfarmapp.ui.product;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.java.yourfarmapp.R;

public class ProductViewHolder extends RecyclerView.ViewHolder {
    ImageView productImage;
    TextView productNameTextView, productDescriptionTextView, productPriceTextView;
    Button editProductButton, deleteProductButton;

    public ProductViewHolder(@NonNull View itemView) {

        super(itemView);

        productImage = itemView.findViewById(R.id.crop_image_mini);
        productNameTextView = itemView.findViewById(R.id.product_name);
        productDescriptionTextView = itemView.findViewById(R.id.product_description);
        productPriceTextView = itemView.findViewById(R.id.product_price);
        editProductButton = itemView.findViewById(R.id.edit_product_button);
        deleteProductButton = itemView.findViewById(R.id.delete_product_button);

    }
}
