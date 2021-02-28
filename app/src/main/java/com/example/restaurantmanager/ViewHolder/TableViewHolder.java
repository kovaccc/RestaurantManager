package com.example.restaurantmanager.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantmanager.Interface.ItemClickListener;
import com.example.restaurantmanager.R;

public class TableViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView table_number;
    public TextView tvNumberOfPersons;
    public TextView tvCharge;
    public Button btnCharge;
    public TextView tvState;
    public Button btnReserved;


    public Button btnFree;
    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener= itemClickListener;
    }

    public TableViewHolder(View itemView) {
        super(itemView);

        tvNumberOfPersons = itemView.findViewById(R.id.tvNumberOfPersons);
        table_number = itemView.findViewById(R.id.tvTableNumber);
        tvCharge = itemView.findViewById(R.id.tvCharge);
        btnCharge = itemView.findViewById(R.id.btnCharge);
        tvState = itemView.findViewById(R.id.tvState);
        btnReserved = itemView.findViewById(R.id.btnReserve);
        btnFree = itemView.findViewById(R.id.btnFree);




        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        itemClickListener.onClick(v,getAdapterPosition(),false);
    }



}
