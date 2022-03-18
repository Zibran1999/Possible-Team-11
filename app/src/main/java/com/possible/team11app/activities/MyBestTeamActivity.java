package com.possible.team11app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.possible.team11app.R;
import com.possible.team11app.databinding.ActivityMyBestTeamBinding;
import com.possible.team11app.models.MatchDetailModels.MatchViewModel;
import com.possible.team11app.models.MatchDetailModels.MatchViewModelFactory;
import com.possible.team11app.models.TeamPlayerModels.TeamPlayerModel;
import com.possible.team11app.utils.AdsViewModel;
import com.possible.team11app.utils.MyApp;

import java.util.List;

public class MyBestTeamActivity extends AppCompatActivity {
    LottieAnimationView lottieAnimationView;
    MatchViewModel matchViewModel;
    String teamName;
    ImageView backIcon;
    ActivityMyBestTeamBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyBestTeamBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MyApp.showInterstitialAd(MyBestTeamActivity.this);
        AdsViewModel adsViewModel = new AdsViewModel(this,binding.adView);
        getLifecycle().addObserver(adsViewModel);

        teamName = getIntent().getStringExtra("teamName");
        showBestTeamImage(this, teamName);
        backIcon = findViewById(R.id.back_icon);
        backIcon.setOnClickListener(view -> onBackPressed());
    }

    private void showBestTeamImage(Context context, String teamName) {
        ImageView teamImage = findViewById(R.id.team_img);
        lottieAnimationView = findViewById(R.id.match_lottieRV);

        lottieAnimationView.setAnimation(R.raw.loding_dot);
        lottieAnimationView.setVisibility(View.VISIBLE);
        lottieAnimationView.playAnimation();

        matchViewModel = new ViewModelProvider(this, new MatchViewModelFactory(this.getApplication(), MatchDetailActivity.id.trim())).get(MatchViewModel.class);
        switch (teamName) {
            case "Head":
                matchViewModel.getTeamImages().observe(this, teamPlayerImageModel -> {
                    List<TeamPlayerModel> teamPlayerModelList = teamPlayerImageModel.getData();
                    if (!teamPlayerImageModel.getData().isEmpty()) {

                        for (TeamPlayerModel t : teamPlayerModelList) {
                            String id = t.getId();
                            String teamId = t.getTeamId();
                            String teamImages = t.getTeamImage();
                            Log.d("onList", id + " " + teamId + " " + teamImages);
                            if (MatchDetailActivity.id.equals(teamId)) {
                                Glide.with(context).load("https://softwaresreviewguides.com/dreamteam11/APIs/Team_Player_Images/" + teamImages).into(teamImage);
                                lottieAnimationView.setVisibility(View.GONE);
                                new Handler().postDelayed(() -> MyApp.showInterstitialAd(MyBestTeamActivity.this), 2000);
                                // MyApp.showBannerAd(this,binding.adView);
                            }
                        }

//                        MyApp.showInterstitialAd(this);


                    } else {
                        binding.textView.setVisibility(View.GONE);
                        lottieAnimationView.setAnimation(R.raw.empty);
                        lottieAnimationView.playAnimation();
                    }
                });

                break;
            case "Grand":
                matchViewModel.getGrandTeamImages().observe(this, teamPlayerImageModel -> {
                    List<TeamPlayerModel> teamPlayerModelList = teamPlayerImageModel.getData();
                    if (!teamPlayerImageModel.getData().isEmpty()) {

                        for (TeamPlayerModel t : teamPlayerModelList) {
                            String id = t.getId();
                            String teamId = t.getTeamId();
                            String teamImages = t.getTeamImage();
                            Log.d("onList", id + " " + teamId + " " + teamImages);
                            if (MatchDetailActivity.id.equals(teamId)) {
                                Glide.with(context).load("https://softwaresreviewguides.com/dreamteam11/APIs/Grand_Team_Player_Images/" + teamImages).into(teamImage);
                                lottieAnimationView.setVisibility(View.GONE);
                                new Handler().postDelayed(() -> MyApp.showInterstitialAd(MyBestTeamActivity.this), 2000);

                                // MyApp.showBannerAd(this,binding.adView);
                            }
                        }

//                        MyApp.showInterstitialAd(this);
                    } else {
                        binding.textView.setVisibility(View.GONE);

                        lottieAnimationView.setAnimation(R.raw.empty);
                        lottieAnimationView.playAnimation();
                    }
                });

                break;
            case "Simple":
                matchViewModel.getSimpleTeamImages().observe(this, teamPlayerImageModel -> {
                    List<TeamPlayerModel> teamPlayerModelList = teamPlayerImageModel.getData();
                    if (!teamPlayerImageModel.getData().isEmpty()) {
                        binding.textView.setVisibility(View.VISIBLE);

                        for (TeamPlayerModel t : teamPlayerModelList) {
                            String id = t.getId();
                            String teamId = t.getTeamId();
                            String teamImages = t.getTeamImage();
                            Log.d("onList", id + " " + teamId + " " + teamImages);
                            if (MatchDetailActivity.id.equals(teamId)) {
                                Glide.with(context).load("https://softwaresreviewguides.com/dreamteam11/APIs/Simple_Team_Player_Images/" + teamImages).into(teamImage);
                                lottieAnimationView.setVisibility(View.GONE);
                                Log.d("teamImage", t.getTeamImage());
                                new Handler().postDelayed(() -> MyApp.showInterstitialAd(MyBestTeamActivity.this), 2000);
                                //  MyApp.showBannerAd(this,binding.adView);
                            }
                        }


//                        MyApp.showInterstitialAd(this);

                    } else {
                        lottieAnimationView.setAnimation(R.raw.empty);
                        lottieAnimationView.playAnimation();
                        binding.textView.setVisibility(View.GONE);
                    }
                });

                break;
        }

    }


    @Override
    public void onBackPressed() {
        AdsViewModel.destroyBanner();
        super.onBackPressed();

    }
}