package com.example.yummyrecipes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.yummyrecipes.Recipe;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class RecipeListAdapter extends ArrayAdapter<Recipe> {
    List<Recipe> recipes;
    Context context;

    public RecipeListAdapter(Context context,List<Recipe> recipes) {
        super(context, 0, recipes);
        this.context = context;
        this.recipes = recipes;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.recipe_item, parent, false);
            TextView text = (TextView) convertView.findViewById(R.id.infoTextViewID);
            ImageView image = (ImageView) convertView.findViewById(R.id.image);

            text.setText(recipes.get(position).getName());
            new DownloadImageTask(image)
                    .execute(recipes.get(position).getThumbnail_url());
            try {
                URL newurl = new URL(recipes.get(position).getThumbnail_url());
                Bitmap mIcon_val = null;
                mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
                image.setImageBitmap(mIcon_val);

            } catch (Exception e) {
                e.getMessage();
            }


            image.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Log.i("Test Show details","test");
                    Intent intent = new Intent(context,RecipeDetails.class);

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("id",recipes.get(position).getId());
                    context.startActivity(intent);
                }
            });

        }

        return convertView;
    }

}