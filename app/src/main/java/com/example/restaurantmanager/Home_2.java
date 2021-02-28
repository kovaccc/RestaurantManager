package com.example.restaurantmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantmanager.Interface.ItemClickListener;
import com.example.restaurantmanager.Model.Category;
import com.example.restaurantmanager.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Home_2 extends AppCompatActivity {


    FirebaseDatabase database;
    DatabaseReference category;

    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Category, MenuViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_2);


        FloatingActionButton floatingActionButtonGoToOrder;
        floatingActionButtonGoToOrder = findViewById(R.id.floatingBtnGoToOrder);
        floatingActionButtonGoToOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent IntentToOrder = new Intent(Home_2.this, Order_For_Table.class);
                IntentToOrder.putExtra("table_id", getIntent().getStringExtra("table_id"));
                startActivity(IntentToOrder);

            }
        });

        //Firebase


        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");


        recycler_menu = findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);

        loadMenu();


    }

    private void loadMenu() {
        adapter = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(Category.class, R.layout.menu_item, MenuViewHolder.class, category) {
            @Override
            protected void populateViewHolder(MenuViewHolder menuViewHolder, Category category, int i) {
                menuViewHolder.txtMenuName.setText(category.getName());
                Picasso.with(getBaseContext()).load(category.getImage()).into(menuViewHolder.imageView);
                final Category clickItem = category;
                menuViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Get CategoryID and send to new Activity
                        Intent foodList = new Intent(Home_2.this, FoodList.class);
                        //Because CategoryId is key, so get key of this item
                        foodList.putExtra("CategoryId", adapter.getRef(position).getKey());
                        foodList.putExtra("table_id", getIntent().getStringExtra("table_id"));
                        startActivity(foodList);
                    }
                });
            }
        };
        recycler_menu.setAdapter(adapter);
    }


    // OVO SI DODAO DA TI NA BACK ODE NA STOLOVE A NE NA SIGN IN
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Home_2.this, Home.class);
        startActivity(intent);
    }

}