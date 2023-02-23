package com.example.yummyrecipes.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yummyrecipes.Model.RecipeGetMoreInfo;
import com.example.yummyrecipes.Model.User;
import com.example.yummyrecipes.Model.UserSession;
import com.example.yummyrecipes.R;
import com.example.yummyrecipes.Service.DownloadImageTask;
import com.example.yummyrecipes.Service.InstructionListAdapter;
import com.example.yummyrecipes.Service.YummlyApi;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String name=intent.getStringExtra("name");
        String description=intent.getStringExtra("description");
        String image=intent.getStringExtra("image");

        TextView nameTextView = (TextView) findViewById(R.id.name);
        TextView descriptionTextView = (TextView) findViewById(R.id.description);
        ImageView thumbnail_url = (ImageView) findViewById(R.id.image);
        ImageButton addToFavorites=findViewById(R.id.addToFavorites);

        nameTextView.setText(name);
        descriptionTextView.setText(description);

        new DownloadImageTask(thumbnail_url)
                .execute(image);
        try {
            URL newurl = new URL(image);
            Bitmap mIcon_val = null;
            mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
            thumbnail_url.setImageBitmap(mIcon_val);

        } catch (Exception e) {
            e.getMessage();
        }

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
                //L'ajout de l'adapter pour les instructions
                List<String> instructions = new ArrayList<>();
                int taille=infos.getInstructions().size();
                Log.i("taille instruct",taille+"");
                for (int i=0;i<taille;i++){
                    instructions.add(infos.getInstructions().get(i).getDisplay_text());
                }
                Log.i("test instructions",instructions.get(0));

                InstructionListAdapter adapter = new InstructionListAdapter(getApplicationContext(), instructions);
                ListView listView=findViewById(R.id.instructions);
                listView.setAdapter(adapter);
                setListViewHeight(listView);
            }


            @Override
            public void onFailure(Call<RecipeGetMoreInfo> call, Throwable t) {
                Log.e("FetchOrSuccess", t.getMessage());

            }
        });

        //Ajouter au favoris
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        addToFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Here : add to favorites","favorites");
                User connectedUser= UserSession.getInstance().getUser();
                DatabaseReference usersRef = myRef.child("favorites/"+connectedUser.getEmail()).child(String.valueOf(id)); //Write your child reference if any
                Map<String, Object> newFavorite = new HashMap<>();
                newFavorite.put("name", name);
                newFavorite.put("image", image);
                newFavorite.put("description", description);
                usersRef.updateChildren(newFavorite);
                Toast.makeText(RecipeDetails.this, "This recipe is successfully added to your favorites", Toast.LENGTH_SHORT).show();
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

    public static void setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

}