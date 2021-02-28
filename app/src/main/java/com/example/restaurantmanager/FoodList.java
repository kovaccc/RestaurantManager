package com.example.restaurantmanager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantmanager.Interface.ItemClickListener;
import com.example.restaurantmanager.Model.Food;
import com.example.restaurantmanager.Model.OrderItem;
import com.example.restaurantmanager.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FoodList extends AppCompatActivity {

    Button btnAddToOrder;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference foodList;

    String categoryId = "";

    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        btnAddToOrder = findViewById(R.id.btnAddToOrder);
        //Firebase
        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Food");

        recyclerView = findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        // get intent here. THIS IS FOR GETTING CATEGORY ID TO SHOW CERTAIN FOOD
        if (getIntent() != null)
            categoryId = getIntent().getStringExtra("CategoryId");
        if (!categoryId.isEmpty() && categoryId != null) {

            loadListFood(categoryId);
        }
    }


    private void loadListFood(String categoryId) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final String TABLE_ID = getIntent().getStringExtra("table_id");

        final DatabaseReference table_order = database.getReference("Tables/" + TABLE_ID + "/Order");

        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class, R.layout.food_item, FoodViewHolder.class, foodList.orderByChild("MenuId").equalTo(categoryId)) {
            @Override
            protected void populateViewHolder(FoodViewHolder foodViewHolder, final Food food, int i) {

                final Food local = food;
                //ADDING TO ORDER
                foodViewHolder.btnAddFood.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(FoodList.this, "ADDING TO ORDER: " + local.getName(), Toast.LENGTH_SHORT).show();

                        DatabaseReference dr = table_order.child("foods").push();

                        OrderItem o = new OrderItem();
                        o.setId(dr.getKey());
                        o.setName(food.getName());
                        o.setPrice(food.getPrice());

                        dr.setValue(o);


                        table_order.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Long total = dataSnapshot.child("total").getValue(Long.class);
                                table_order.child("total").setValue(total + food.getPrice());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });

                foodViewHolder.food_price.setText(getString(R.string.food_price, food.getPrice().toString()));
                foodViewHolder.food_name.setText(food.getName());
                Picasso.with(getBaseContext()).load(food.getImage()).into(foodViewHolder.food_image);


                  foodViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(FoodList.this, "" + local.getName(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };

        recyclerView.setAdapter(adapter);
    }

}

