package com.possible.team11app.models.AdsStatusModels;

public class AdsStatus {
    String id,bannerAds, interstitialAds;

    public AdsStatus(String id, String bannerAds, String interstitialAds) {
        this.id = id;
        this.bannerAds = bannerAds;
        this.interstitialAds = interstitialAds;
    }

    public String getId() {
        return id;
    }

    public String getBannerAds() {
        return bannerAds;
    }

    public String getInterstitialAds() {
        return interstitialAds;
    }
}
