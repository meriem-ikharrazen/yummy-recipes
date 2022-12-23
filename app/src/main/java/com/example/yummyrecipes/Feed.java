package com.example.yummyrecipes;

import java.util.List;

public class Feed {

    private List<Recipe> feed;
    private Object seo;

    public List<Recipe> getFeed() {
        return feed;
    }

    public void setFeed(List<Recipe> feed) {
        this.feed = feed;
    }
}
