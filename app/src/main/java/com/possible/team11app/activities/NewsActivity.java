package com.possible.team11app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.possible.team11app.R;
import com.possible.team11app.utils.AdsViewModel;
import com.possible.team11app.utils.MyApp;


public class NewsActivity extends AppCompatActivity {

    int pos = -1;
    ImageView newsImg, backIcon;
    TextView newsTitle, newsDesc;
    String img, desc, title;
    RelativeLayout adView,adview2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        newsImg = findViewById(R.id.newsImg);
        newsTitle = findViewById(R.id.newsTitle);
        newsDesc = findViewById(R.id.newsDesc);
        backIcon = findViewById(R.id.back_icon);
        adView = findViewById(R.id.adView);
        adview2 = findViewById(R.id.adView2);

        img = getIntent().getStringExtra("img");
        desc = getIntent().getStringExtra("desc");
        title = getIntent().getStringExtra("title");
        pos = getIntent().getIntExtra("newsPos", 0);

        new Handler().postDelayed(() -> {
            MyApp.showInterstitialAd(this);
        }, 2000);


        Glide.with(this).load("https://softwaresreviewguides.com/dreamteam11/APIs/Cricket_News_Images/" + img).into(newsImg);
        newsTitle.setText(title);
        newsDesc.setText(desc);
        MyApp.showInterstitialAd(this);
        AdsViewModel adsViewModel = new AdsViewModel(this,adView);
        getLifecycle().addObserver(adsViewModel);

        MyApp.showBannerAd(this,adview2);
        backIcon.setOnClickListener(v -> onBackPressed());


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AdsViewModel.destroyBanner();
        overridePendingTransition(0, 0);
        finish();
        overridePendingTransition(0, 0);
    }

}