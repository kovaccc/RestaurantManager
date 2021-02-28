package com.example.restaurantmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantmanager.Common.Common;
import com.example.restaurantmanager.Interface.ItemClickListener;
import com.example.restaurantmanager.Model.Tables;
import com.example.restaurantmanager.ViewHolder.TableViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference home_2_tables;

    TextView txtFullName;



    FirebaseRecyclerAdapter<Tables, TableViewHolder> adapter;


    DrawerLayout drawer;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle("Tables");


        //Init Firebase
        database = FirebaseDatabase.getInstance();
        home_2_tables = database.getReference("Tables");


        navigationView = findViewById(R.id.nav_view);


        drawer = findViewById(R.id.drawer_layout);


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_log_out)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        //set Name for user
        View headerView = navigationView.getHeaderView(0);
        txtFullName = headerView.findViewById(R.id.txtFullName);
        txtFullName.setText(Common.currentUser.getName());


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                Intent signIn = new Intent(Home.this, SignIn.class);
                signIn.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(signIn);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });


        //load tables

        recyclerView = findViewById(R.id.recycler_tables);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        loadTables();


    }


    private void loadTables() {

        adapter = new FirebaseRecyclerAdapter<Tables, TableViewHolder>(Tables.class, R.layout.table_item, TableViewHolder.class, home_2_tables) {
            @Override
            protected void populateViewHolder(final TableViewHolder tableViewHolder, final Tables tables, final int i) {

                adapter.getRef(i).child("Order").child("total").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        tableViewHolder.tvCharge.setText("Charge: " + (dataSnapshot.getValue(Long.class) == null ? 0 : dataSnapshot.getValue(Long.class)+"$"));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



                tableViewHolder.table_number.setText("Table: " + tables.getNumber());
                tableViewHolder.tvNumberOfPersons.setText("Persons: " + tables.getPersons());
                tableViewHolder.tvState.setText(tables.getState());


                tableViewHolder.btnCharge.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        adapter.getRef(i).child("Order").child("foods").removeValue();
                        adapter.getRef(i).child("Order").child("total").setValue(0);

                    }
                });

                tableViewHolder.btnReserved.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        adapter.getRef(i).child("State").setValue("Reserved");
                        tableViewHolder.tvState.setText(tables.getState());
                        //tableViewHolder.tvState.setText("RESERVED");

                    }
                });

                tableViewHolder.btnFree.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(!(tableViewHolder.tvCharge.getText().equals("Charge: 0$"))) {
                            Toast.makeText(Home.this, "You must charge first!" , Toast.LENGTH_SHORT).show();
                        }
                        else {
                            adapter.getRef(i).child("State").setValue("Free");
                            tableViewHolder.tvState.setText(tables.getState());
                            //tableViewHolder.tvState.setText("FREE");

                        }

                    }
                });


                tableViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        if(tableViewHolder.tvState.getText().equals("Free"))
                            return;

                        Intent home2Intent = new Intent(Home.this, Home_2.class);
                        home2Intent.putExtra("table_id", adapter.getRef(position).getKey());
                        startActivity(home2Intent);
                        finish();
                    }
                });
            }
        };

        recyclerView.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


        return false;
    }


    @Override
    public void onBackPressed() {
        finishAffinity();
        finish();
    }

}
