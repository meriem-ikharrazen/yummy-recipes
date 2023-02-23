package com.example.yummyrecipes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
                User connectedUser=UserSession.getInstance().getUser();
                DatabaseReference usersRef = myRef.child("favorites/"+connectedUser.getEmail()); //Write your child reference if any
                Map<String, Object> newFavorite = new HashMap<>();
                newFavorite.put(random(), String.valueOf(id));
                usersRef.updateChildren(newFavorite);
                Toast.makeText(RecipeDetails.this, "this recipe is successfully added to your favorites", Toast.LENGTH_SHORT).show();
            }
        });

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

    public String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(5);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

}