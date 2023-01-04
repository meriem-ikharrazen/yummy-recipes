package com.example.yummyrecipes;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface YummlyApi {
    @Headers({
            "X-RapidAPI-Key: 951377563bmsh3415018cc725eecp1ba701jsna76ddd49ffc0",
            "X-RapidAPI-Host: tasty.p.rapidapi.com"
            })
    @GET("recipes/list?from=0&size=20")
    Call<Recipes> getRecipes();}
