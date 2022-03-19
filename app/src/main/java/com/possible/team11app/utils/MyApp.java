package com.possible.team11app.utils;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.MobileAds;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.sdk.InterstitialListener;
import com.onesignal.OSNotificationOpenedResult;
import com.onesignal.OneSignal;
import com.possible.team11app.activities.MainActivity;
import com.possible.team11app.models.AdsModel;
import com.possible.team11app.models.AdsModelList;
import com.possible.team11app.models.MatchDetailModels.ApiInterface;
import com.possible.team11app.models.MatchDetailModels.WebServices;

import java.util.Objects;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyApp extends Application {
    private static final String ONESIGNAL_APP_ID = "452632c3-18f4-4e3a-b63a-3f866ac82186";
     public static InterstitialAd interstitialAd;

    public static MyApp mInstance;
    ApiInterface apiInterface;


    public MyApp() {
        mInstance = this;
    }

    public static void showInterstitialAd(Activity context) {
        String id = Paper.book().read(Prevalent.openAppAds);
        IronSource.init(context, id);
        IronSource.loadInterstitial();
        IronSource.setMetaData("Facebook_IS_CacheFlag", "IMAGE");
        IronSource.showInterstitial();
        IronSource.setInterstitialListener(new InterstitialListener() {
            @Override
            public void onInterstitialAdReady() {
                IronSource.showInterstitial();

            }

            @Override
            public void onInterstitialAdLoadFailed(IronSourceError ironSourceError) {

            }

            @Override
            public void onInterstitialAdOpened() {
            }

            @Override
            public void onInterstitialAdClosed() {

            }

            @Override
            public void onInterstitialAdShowSucceeded() {

            }

            @Override
            public void onInterstitialAdShowFailed(IronSourceError ironSourceError) {

            }

            @Override
            public void onInterstitialAdClicked() {

            }
        });


    }

    public static void showBannerAd(Context context, RelativeLayout container) {
        String id = Paper.book().read(Prevalent.bannerAds);
        MobileAds.initialize(context);
        Log.d("admobAdBan", id);
        AdRequest adRequest = new AdRequest.Builder().build();
        AdView adView = new AdView(context);
        container.addView(adView);
        adView.setAdUnitId(id);
        adView.setAdSize(AdSize.BANNER);
        adView.loadAd(adRequest);
        container.setVisibility(View.VISIBLE);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        Paper.init(mInstance);
            fetchAds();
           IronSource.getAdvertiserId(this);
        //Network Connectivity Status
          IronSource.shouldTrackNetworkState(this, true);
          AudienceNetworkAds.initialize(mInstance);

        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setNotificationOpenedHandler(new ExampleNotificationOpenedHandler());
        OneSignal.setAppId(ONESIGNAL_APP_ID);
    }

    private void fetchAds() {
        apiInterface = WebServices.getInterface();
        Call<AdsModelList> call = apiInterface.fetchAds("My Expert Team");
        call.enqueue(new Callback<AdsModelList>() {
            @Override
            public void onResponse(@NonNull Call<AdsModelList> call, @NonNull Response<AdsModelList> response) {
                if (response.isSuccessful()) {
                    if (Objects.requireNonNull(response.body()).getData() != null) {
                        for (AdsModel ads : response.body().getData()) {
                            Log.d("admobAdId", ads.getBanner() + " " + ads.getInterstitial() + " " + ads.getNativeADs());
                            Paper.book().write(Prevalent.bannerAds, ads.getBanner().trim());
                            Paper.book().write(Prevalent.interstitialAds, ads.getInterstitial().trim());
                            Paper.book().write(Prevalent.nativeAds, ads.getNativeADs().trim());
                            Paper.book().write(Prevalent.openAppAds, ads.getAppOpen());
                        }
                    }
                } else {
                    Log.d("adsError", response.message());
                }

            }

            @Override
            public void onFailure(@NonNull Call<AdsModelList> call, @NonNull Throwable t) {
                Log.d("adsError", t.getMessage());
            }
        });
    }


    private class ExampleNotificationOpenedHandler implements OneSignal.OSNotificationOpenedHandler {
        @Override
        public void notificationOpened(OSNotificationOpenedResult result) {
            Intent intent = new Intent(MyApp.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}
