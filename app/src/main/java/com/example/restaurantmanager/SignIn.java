package com.example.restaurantmanager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.restaurantmanager.Common.Common;
import com.example.restaurantmanager.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {
    EditText etPhone, etPassword;
    Button btnSignIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        etPassword = findViewById(R.id.etPassword);
        etPhone = findViewById(R.id.etPhone);
        btnSignIn = findViewById(R.id.btnSignIn);


        //init Firebase
        final FirebaseDatabase database= FirebaseDatabase.getInstance();
         final DatabaseReference table_user = database.getReference("User");
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                mDialog.setMessage("Please waiting...");
                mDialog.show();

                table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //check if user not exist in database

                      if(dataSnapshot.child(etPhone.getText().toString()).exists() && TextUtils.isEmpty(etPhone.getText())==false) {

                            //Get useer information


                            mDialog.dismiss();

                            User user = dataSnapshot.child(etPhone.getText().toString()).getValue(User.class);
                            if (user.getPassword().equals(etPassword.getText().toString())) {

                                Intent homeIntent = new Intent(SignIn.this,Home.class);                                   // OVDJE SI MIJENJO DA IDE NA HOME_2 KLASU
                                Common.currentUser = user;
                                startActivity(homeIntent);
                                finish();
                            } else {

                                Toast.makeText(SignIn.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                            }
                        }

                        else
                            {
                                mDialog.dismiss();
                                Toast.makeText(SignIn.this,"User not exist in Database", Toast.LENGTH_SHORT).show();
                            }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SignIn.this, MainActivity.class);
        startActivity(intent);
    }
}
