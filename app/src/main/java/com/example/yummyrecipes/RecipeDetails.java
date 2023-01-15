package com.example.yummyrecipes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        Intent intent=getIntent();
        int id= Integer.parseInt(intent.getStringExtra("id"));
        Log.i("Get Id",id+"");

        // Test fetching recipes
        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl("https://tasty.p.rapidapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        YummlyApi test = mRetrofit.create(YummlyApi.class);

        Call<RecipeGetMoreInfo> call = test.getMoreInfo(id);

        call.enqueue(new Callback<RecipeGetMoreInfo>() {
            @Override
            public void onResponse(Call<RecipeGetMoreInfo> call, Response<RecipeGetMoreInfo> response) {
                Log.i("FetchOrSuccess", String.valueOf(response.code()));
                RecipeGetMoreInfo infos = response.body();
                Log.i("GetInfo", infos.getOriginal_video_url());
                Log.i("Instructions", infos.getInstructions().get(0).getDisplay_text());

            }

            @Override
            public void onFailure(Call<RecipeGetMoreInfo> call, Throwable t) {
                Log.e("FetchOrSuccess", t.getMessage());

            }
        });

    }
}