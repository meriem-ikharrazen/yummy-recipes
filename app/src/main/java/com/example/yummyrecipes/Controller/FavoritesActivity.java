package com.example.yummyrecipes.Controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.yummyrecipes.Service.FavoriteListAdapter;
import com.example.yummyrecipes.Model.Favoris;
import com.example.yummyrecipes.Model.User;
import com.example.yummyrecipes.Model.UserSession;
import com.example.yummyrecipes.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {
    private List<Favoris> listeFavoris;
    private ListView list;
    private MainActivity currentActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        //get user connecté
        User connectedUser= UserSession.getInstance().getUser();

        //Get id des recettes
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference favoriteRef = myRef.child("favorites/"+connectedUser.getEmail());
        listeFavoris=new ArrayList<>();

        favoriteRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnap : snapshot.getChildren()) {
                    String image = dataSnap.child("image").getValue(String.class);
                    String description = dataSnap.child("description").getValue(String.class);
                    String name = dataSnap.child("name").getValue(String.class);
                    String id = dataSnap.getKey();
                    listeFavoris.add(new Favoris(Integer.parseInt(id),image,name,description));
                }
                list=(ListView)findViewById(R.id.list);
                FavoriteListAdapter adapter = new FavoriteListAdapter(FavoritesActivity.this, listeFavoris);
                Log.i("FavoritesActivity", FavoritesActivity.this.toString());
                list.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
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