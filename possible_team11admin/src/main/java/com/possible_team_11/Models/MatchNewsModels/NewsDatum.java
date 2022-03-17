package com.possible_team_11.Models.MatchNewsModels;

public class NewsDatum {

    private String id;
    private String newsImg;
    private String newsTitle;
    private String newsDesc;

    public NewsDatum(String id, String newsImg, String newsTitle, String newsDesc) {
        this.id = id;
        this.newsImg = newsImg;
        this.newsTitle = newsTitle;
        this.newsDesc = newsDesc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNewsImg() {
        return newsImg;
    }

    public void setNewsImg(String newsImg) {
        this.newsImg = newsImg;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsDesc() {
        return newsDesc;
    }

    public void setNewsDesc(String newsDesc) {
        this.newsDesc = newsDesc;
    }

}
