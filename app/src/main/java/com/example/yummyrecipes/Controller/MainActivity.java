package com.example.yummyrecipes.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.yummyrecipes.R;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Uncommit this to access create account
        Intent intent = new Intent(MainActivity.this, CreateAccount.class);
        MainActivity.this.startActivity(intent);

    }
}