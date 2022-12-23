package com.example.yummyrecipes;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface YummlyApi {
    @Headers({
            "X-RapidAPI-Key: 951377563bmsh3415018cc725eecp1ba701jsna76ddd49ffc0",
            "X-RapidAPI-Host: yummly2.p.rapidapi.com"
            })
    @GET("feeds/list?limit=24&start=0")
    Call<Feed> getFeeds();}
