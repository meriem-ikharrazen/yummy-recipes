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
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;
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
        String name=intent.getStringExtra("name");
        String description=intent.getStringExtra("description");
        String image=intent.getStringExtra("image");

        TextView nameTextView = (TextView) findViewById(R.id.name);
        TextView descriptionTextView = (TextView) findViewById(R.id.description);
        TextView instructionTextView = (TextView) findViewById(R.id.instruction);
        ImageView thumbnail_url = (ImageView) findViewById(R.id.image);

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