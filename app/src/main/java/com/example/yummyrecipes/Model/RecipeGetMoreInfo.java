package com.example.yummyrecipes.Model;


import java.util.List;

public class RecipeGetMoreInfo {
    private List<Instruction> instructions;
    private String original_video_url;
    private String thumbnail_url;

    public List<Instruction> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<Instruction> instructions) {
        this.instructions = instructions;
    }

    public String getOriginal_video_url() {
        return original_video_url;
    }

    public void setOriginal_video_url(String original_video_url) {
        this.original_video_url = original_video_url;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }
}
