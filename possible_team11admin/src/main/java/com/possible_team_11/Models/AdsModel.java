package com.possible_team_11.Models;

import com.google.gson.annotations.SerializedName;

public class AdsModel {
    String id, banner, interstitial;
    @SerializedName("native")
    String nativeADs;
    String appOpen;

    public AdsModel(String id, String banner, String interstitial, String nativeADs, String appOpen) {
        this.id = id;
        this.banner = banner;
        this.interstitial = interstitial;
        this.nativeADs = nativeADs;
        this.appOpen = appOpen;
    }

    public String getId() {
        return id;
    }

    public String getBanner() {
        return banner;
    }

    public String getInterstitial() {
        return interstitial;
    }

    public String getNativeADs() {
        return nativeADs;
    }

    public String getAppOpen() {
        return appOpen;
    }
}
