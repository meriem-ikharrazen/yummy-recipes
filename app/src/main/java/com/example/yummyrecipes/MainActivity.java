package com.example.yummyrecipes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {


    private List<Recipe> recipes;
    private ListView list;
    private MainActivity currentActivity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*Button btn=(Button)findViewById(R.id.button_click);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i=new Intent(getApplicationContext(),RecipesListActivity.class);
                startActivity(i);
            }
        });
*/

        // ******* ADD USER TO DATABASE ***********
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(); //Getting root reference
        User user = new User("meryam@meryem","password","meryam soussi" );
        DatabaseReference usersRef = myRef.child("users/"+user.getEmail()); //Write your child reference if any
        Log.i("firebase", myRef.toString());
        usersRef.setValue(user);


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
                Log.i("list", list.toString());


                RecipeListAdapter adapter = new RecipeListAdapter(MainActivity.this, recipes_list);
                Log.i("MAinA", MainActivity.this.toString());
                list.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<Recipes> call, Throwable t) {
                Log.e("FetchOrSuccess", t.getMessage());

            }
        });



    }
}