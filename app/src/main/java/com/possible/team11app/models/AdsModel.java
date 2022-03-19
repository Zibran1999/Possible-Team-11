package com.possible.team11app.models;

import com.google.gson.annotations.SerializedName;

public class AdsModel {
    String id, banner, interstitial, appOpen;
    @SerializedName("native")
    String nativeADs;

    public AdsModel(String id, String banner, String interstitial, String nativeADs, String appOpen) {
        this.id = id;
        this.banner = banner;
        this.interstitial = interstitial;
        this.appOpen = appOpen;
        this.nativeADs = nativeADs;
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

    public String getAppOpen() {
        return appOpen;
    }

    public String getNativeADs() {
        return nativeADs;
    }
}
