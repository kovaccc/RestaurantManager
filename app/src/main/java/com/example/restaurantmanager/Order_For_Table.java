package com.example.restaurantmanager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantmanager.Interface.ItemClickListener;
import com.example.restaurantmanager.Model.OrderItem;
import com.example.restaurantmanager.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Order_For_Table extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference category;
    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;
    Button btnGoCharge;
    String total;
    public TextView txtTotal;

    FirebaseRecyclerAdapter<OrderItem, OrderViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order__for__table);

        btnGoCharge = findViewById(R.id.btnGoCharge);
        btnGoCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Order_For_Table.this, Home.class);
                startActivity(intent);
            }
        });

        //init firebase
        database = FirebaseDatabase.getInstance();
        String TABLE_ID = getIntent().getStringExtra("table_id");
        category = database.getReference("Tables").child(TABLE_ID).child("Order").child("foods");

        recycler_menu = findViewById(R.id.listOrder);
        //recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);

        txtTotal = findViewById(R.id.tvTotal);
        loadOrder();

    }

    private void loadOrder() {

        category.getParent().child("total").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                txtTotal.setText(dataSnapshot.getValue(Long.class).toString() + "$");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        adapter = new FirebaseRecyclerAdapter<OrderItem, OrderViewHolder>(OrderItem.class, R.layout.order_item, OrderViewHolder.class, category) {
            @Override
            protected void populateViewHolder(final OrderViewHolder menuViewHolder, final OrderItem category, int i) {
                final OrderItem clickItem = category;
                menuViewHolder.txtName.setText(category.getName());
                menuViewHolder.txtPrice.setText(category.getPrice().toString());
                menuViewHolder.txtID.setText(category.getId());

                menuViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, final int position, boolean isLongClick) {

                        menuViewHolder.txtX.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                final DatabaseReference firebase_total = adapter.getRef(position).getParent().getParent().child("total");

                                firebase_total.addListenerForSingleValueEvent(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                        firebase_total.setValue(dataSnapshot.getValue(Long.class) - category.getPrice());
                                        adapter.getRef(position).removeValue();


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }
                        });



                    }
                });
            }
        };
        recycler_menu.setAdapter(adapter);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
