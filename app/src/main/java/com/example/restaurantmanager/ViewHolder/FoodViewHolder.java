package com.example.restaurantmanager.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantmanager.Interface.ItemClickListener;
import com.example.restaurantmanager.R;

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView food_name;
    public ImageView food_image;
    public TextView food_price;
    public Button btnAddFood;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener= itemClickListener;
    }

    public FoodViewHolder(View itemView) {
        super(itemView);

        food_price = itemView.findViewById(R.id.food_price);
        food_name = itemView.findViewById(R.id.food_name);
        food_image = itemView.findViewById(R.id.food_image);
        btnAddFood = itemView.findViewById(R.id.btnAddToOrder);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        itemClickListener.onClick(v,getAdapterPosition(),false);
    }
}
