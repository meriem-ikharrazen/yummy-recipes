package com.example.yummyrecipes.Service;

import com.example.yummyrecipes.Model.RecipeGetMoreInfo;
import com.example.yummyrecipes.Model.Recipes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface YummlyApi {
    @Headers({
            "X-RapidAPI-Key: f1f401036cmshe91c481bfafcd79p1bb762jsn45a71a5cf4ab",
            "X-RapidAPI-Host: tasty.p.rapidapi.com"
            })
    @GET("recipes/list?from=0&size=20")
    Call<Recipes> getRecipes();

    @Headers({
            "X-RapidAPI-Key: f1f401036cmshe91c481bfafcd79p1bb762jsn45a71a5cf4ab",
            "X-RapidAPI-Host: tasty.p.rapidapi.com"
    })
    @GET("recipes/get-more-info")
    Call<RecipeGetMoreInfo> getMoreInfo(@Query("id") int id);
}
