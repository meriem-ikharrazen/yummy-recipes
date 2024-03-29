package com.example.yummyrecipes.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yummyrecipes.Model.User;
import com.example.yummyrecipes.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(); //Getting root reference


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        Button btn = findViewById(R.id.buttonCreateAccount);
        Button goToLogInActivityButton = findViewById(R.id.goToLogInActivityButton);


        if(btn != null){
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("Create account", "onClick: ");
                    EditText email = findViewById(R.id.emaiText);
                    EditText fullName = findViewById(R.id.fullNameText);
                    EditText password = findViewById(R.id.passwordText);
                    try{
                        // ******* ADD USER TO DATABASE ***********
                        User user = new User(email.getText().toString(),password.getText().toString(),fullName.getText().toString() );
                        DatabaseReference usersRef = myRef.child("users/"+user.getEmail()); //Write your child reference if any
                        Log.i("firebase", myRef.toString());
                        usersRef.setValue(user);
                        Toast.makeText(CreateAccount.this, "Login successful", Toast.LENGTH_SHORT).show();

                        // Navigate to recipes login
                        Intent intent = new Intent(CreateAccount.this, LogInActiviy.class);
                        startActivity(intent);
                    } catch (Exception e){
                        email.setText("");
                        fullName.setText("");
                        password.setText("");
                        Toast.makeText(CreateAccount.this, "Login failed", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }

        goToLogInActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAccount.this, LogInActiviy.class);
                startActivity(intent);
            }
        });
    }
}