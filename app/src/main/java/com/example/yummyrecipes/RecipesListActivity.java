package com.example.yummyrecipes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipesListActivity extends AppCompatActivity {

    private List<Recipe> recipes;
    private ListView list;
    private RecipesListActivity currentActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        currentActivity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_list);


        // Test fetching recipes
        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl("https://tasty.p.rapidapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        YummlyApi test = mRetrofit.create(YummlyApi.class);

        Call<Recipes> call = test.getRecipes();

        call.enqueue(new Callback<Recipes>() {
            @Override
            public void onResponse(Call<Recipes> call, Response<Recipes> response) {
                Log.i("FetchOrSuccess", String.valueOf(response.code()));
                Recipes recipes = response.body();
                Log.i("enter1", recipes.toString());

                List<Recipe> recipes_list = recipes.getResults();
                Log.i("enter2", recipes_list.toString());

                for(Recipe recipe : recipes_list){
                    Log.i("nameDebug", recipe.getName());
                }


                list = (ListView)findViewById(R.id.list);
                RecipeListAdapter adapter = new RecipeListAdapter(currentActivity, recipes_list);
                list.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<Recipes> call, Throwable t) {
                Log.e("FetchOrSuccess", t.getMessage());

            }
        });





    }

}