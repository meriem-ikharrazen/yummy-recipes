package com.example.yummyrecipes.Service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.yummyrecipes.Model.Favoris;
import com.example.yummyrecipes.R;

import java.net.URL;
import java.util.List;

public class FavoriteListAdapter extends ArrayAdapter<Favoris>  {

    List<Favoris> favoris;
    Context context;

    public FavoriteListAdapter(Context context,List<Favoris> favoris) {
        super(context, 0, favoris);
        this.context = context;
        this.favoris = favoris;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.recipe_item, parent, false);
            TextView text = (TextView) convertView.findViewById(R.id.infoTextViewID);
            ImageView image = (ImageView) convertView.findViewById(R.id.image);

            text.setText(favoris.get(position).getName());
            new DownloadImageTask(image)
                    .execute(favoris.get(position).getImage());
            try {
                URL newurl = new URL(favoris.get(position).getImage());
                Bitmap mIcon_val = null;
                mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
                image.setImageBitmap(mIcon_val);

            } catch (Exception e) {
                e.getMessage();
            }

            //ne fonctionne pas
/*
            image.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Log.i("Test Show details","test");
                    Intent intent = new Intent(context,RecipeDetails.class);

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("id",favoris.get(position).getId());
                    intent.putExtra("description",favoris.get(position).getDescription());
                    intent.putExtra("name",favoris.get(position).getName());
                    intent.putExtra("image",favoris.get(position).getImage());
                    context.startActivity(intent);
                }
            });*/

        }

        return convertView;
    }
}
