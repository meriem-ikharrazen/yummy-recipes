package com.example.yummyrecipes.Controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yummyrecipes.Model.User;
import com.example.yummyrecipes.Model.UserSession;
import com.example.yummyrecipes.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogInActiviy extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_activiy);
        Button goToCreateAccountActivityButton = findViewById(R.id.buttonCreateAccount);
        Button logIn=findViewById(R.id.buttonLogIn);

        logIn.setOnClickListener(new View.OnClickListener() {
            //Getting root reference
            @Override
            public void onClick(View view) {
                Log.i("Log In", "onClick: ");
                EditText email = findViewById(R.id.emaiText);
                EditText password = findViewById(R.id.passwordText);

                // *******  LOG IN ***********
                DatabaseReference usersRef = myRef.child("users/"+email.getText().toString());

                usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot!=null && snapshot.exists()) {
                        // User found in database
                        String fullname = snapshot.child("fullname").getValue(String.class);
                        String userEmail = snapshot.child("email").getValue(String.class);
                        String userPassword = snapshot.child("password").getValue(String.class);
                        if (userPassword.equals(password.getText().toString())) {
                            Log.i("Success Connection", "Log In");
                            User connectedUser=new User(userEmail,userPassword,fullname);

                            //Mettre l'utilisateur connecté dans une session
                            UserSession.getInstance().setUser(connectedUser);

                            // Navigate to recipes list main to show all recipes
                            Intent intent = new Intent(LogInActiviy.this, RecipesMainListActivity.class);
                            startActivity(intent);

                        } else {
                            // Password does not match, login failed
                            Log.i("Password ne matche pas", "Log In");
                            Toast.makeText(LogInActiviy.this, "Login failed: incorrect password", Toast.LENGTH_SHORT).show();
                        }
                        } else {
                            // User not found in database
                            Log.i("User not found in db", "Log In");
                            Toast.makeText(LogInActiviy.this, "Login failed: user not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Database error occurred
                        Log.i("Database error occurred","Log In");
                        Toast.makeText(LogInActiviy.this, "Login failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                });

                goToCreateAccountActivityButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(LogInActiviy.this, CreateAccount.class);
                        startActivity(intent);
                    }
                });
            }

        });
    }
}