package com.possible.team11app.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.possible.team11app.R;
import com.possible.team11app.databinding.ActivityMainBinding;
import com.possible.team11app.models.AdsStatusModels.AdsStatus;
import com.possible.team11app.models.MatchDetailModels.MatchViewModel;
import com.possible.team11app.utils.MyService;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    public static final String BroadcastStringForAction = "checkInternet";
    public static boolean bannerAds, intersAds;
    ActivityMainBinding binding;
    int count = 1;
    public BroadcastReceiver MyReceiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(BroadcastStringForAction)) {
                if (intent.getStringExtra("online_status").trim().equals("true")) {
                    Set_Visibility_ON();
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

                    count++;
                } else {
                    Set_Visibility_OFF();
                }
            }
        }
    };
    MatchViewModel matchViewModel;
    FirebaseAnalytics firebaseAnalytics;
    private IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        fetchAdsStatus();


        /* code inside to check quick response starts */
        intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastStringForAction);
        Intent serviceIntent = new Intent(this, MyService.class);
        startService(serviceIntent);
        binding.tvNotConnected.setVisibility(View.GONE);
        if (isOnline(getApplicationContext())) {
            Set_Visibility_ON();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


        } else {
            Set_Visibility_OFF();
        }
        /* code inside to check quick response starts */
    }

    /*code to check quick response of isOnline starts*/

    public boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();

    }


    @SuppressLint("UseCompatLoadingForDrawables")
    public void Set_Visibility_ON() {
        binding.tvNotConnected.setVisibility(View.GONE);
        binding.lottie.setVisibility(View.VISIBLE);
        binding.img.setVisibility(View.VISIBLE);
        // binding.imageView3.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.container.setBackgroundColor(getColor(R.color.red));
        }
        if (count == 4) {
            new Handler().postDelayed(() -> {
                startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));
                finish();
            }, 1000);
        }


    }

    public void Set_Visibility_OFF() {
        binding.tvNotConnected.setVisibility(View.VISIBLE);
        binding.lottie.setVisibility(View.GONE);
        binding.img.setVisibility(View.GONE);
        // binding.imageView3.setVisibility(View.GONE);
        binding.container.setBackgroundColor(Color.BLACK);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        registerReceiver(MyReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(MyReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(MyReceiver, intentFilter);
    }

    private void fetchAdsStatus() {
        matchViewModel = new ViewModelProvider(this).get(MatchViewModel.class);
        matchViewModel.getAdsStatus().observe(this, adsStatusModel -> {
            List<AdsStatus> adsStatusList = adsStatusModel.getAdsStatuses();
            for (AdsStatus ads : adsStatusList) {
                bannerAds = Boolean.parseBoolean(ads.getBannerAds());
                intersAds = Boolean.parseBoolean(ads.getInterstitialAds());
                Log.d("adsOnResponse", bannerAds + " " + intersAds);
            }
        });
    }
}