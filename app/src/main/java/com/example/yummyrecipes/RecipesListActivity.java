package com.example.yummyrecipes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.listeFavoris:
                Intent intentFavoris = new Intent(this, FavoritesActivity.class);
                startActivity(intentFavoris);
                return true;
            case R.id.listeRecipes:
                Intent intentrecipes = new Intent(this, RecipesMainListActivity.class);
                startActivity(intentrecipes);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}