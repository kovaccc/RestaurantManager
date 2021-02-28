package com.example.restaurantmanager.ViewHolder;

import android.view.View;
import android.widget.TextView;

import com.example.restaurantmanager.Interface.ItemClickListener;
import com.example.restaurantmanager.R;

public class OrderViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtName;
    public TextView txtPrice;
    public TextView txtID;
    public TextView txtX;
    private ItemClickListener itemClickListener;

    public OrderViewHolder(View itemView) {
        super(itemView);
        txtID = itemView.findViewById(R.id.tvOrderItemID);
        txtName = itemView.findViewById(R.id.tvOrderItemName);
        txtPrice = itemView.findViewById(R.id.tvOrderItemPrice);
        txtX = itemView.findViewById(R.id.tvX);



        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {

        itemClickListener.onClick(v,getAdapterPosition(),false);
    }
}