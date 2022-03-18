package com.possible.team11app.activities;

import static com.possible.team11app.activities.ui.main.MatchDetailsFragment.setImage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.tabs.TabLayout;
import com.possible.team11app.R;
import com.possible.team11app.activities.ui.main.MatchDetailPagerAdapter;
import com.possible.team11app.databinding.ActivityMatchDetailBinding;
import com.possible.team11app.utils.AdsViewModel;
import com.possible.team11app.utils.MyService;
import com.theartofdev.edmodo.cropper.CropImage;

public class MatchDetailActivity extends AppCompatActivity {
    public static final String BroadcastStringForAction = "checkInternet";
    private static final int CAMERA_REQUEST = 100;
    private static final int STORAGE_REQUEST = 200;
    public static String id = "";
    public static int adCount = 1;
    public static int pos = 0;
    MatchDetailPagerAdapter matchDetailPagerAdapter;
    ViewPager viewPager;
    ActivityMatchDetailBinding binding;
    LottieAnimationView lottieAnimationView;
    int count = 1;
    public BroadcastReceiver MyReceiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(BroadcastStringForAction)) {
                if (intent.getStringExtra("online_status").trim().equals("true")) {
                    Set_Visibility_ON();
                    count++;
                } else {
                    Set_Visibility_OFF();
                }
            }
        }
    };
    Uri uri;
    private IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMatchDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        id = getIntent().getStringExtra("id");
        matchDetailPagerAdapter = new MatchDetailPagerAdapter(this, getSupportFragmentManager());
        viewPager = binding.viewPagerMatch;
        lottieAnimationView = binding.lottieMatchDetail;
        binding.backIcon.setOnClickListener(v -> onBackPressed());
        pos = getIntent().getIntExtra("pos", 0);


        intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastStringForAction);
        Intent serviceIntent = new Intent(this, MyService.class);
        startService(serviceIntent);
        if (isOnline(getApplicationContext())) {
            Set_Visibility_ON();
        } else {
            Set_Visibility_OFF();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AdsViewModel.destroyBanner();
        overridePendingTransition(0, 0);
        finish();
        overridePendingTransition(0, 0);
        adCount = 1;

    }


    public boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void Set_Visibility_ON() {
        lottieAnimationView.setVisibility(View.GONE);
        binding.tabs.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.VISIBLE);
        if (count == 4) {
            AdsViewModel adsViewModel = new AdsViewModel(this,binding.adView);
            getLifecycle().addObserver(adsViewModel);
            viewPager.setAdapter(matchDetailPagerAdapter);
            TabLayout tabs = binding.tabs;
            tabs.setupWithViewPager(viewPager);

        }
    }

    public void Set_Visibility_OFF() {
        lottieAnimationView.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.GONE);
        binding.tabs.setVisibility(View.GONE);

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST: {
                if (grantResults.length > 0) {
                    boolean camera_accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageaccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (camera_accepted && writeStorageaccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "Please Enable Camera and Storage Permissions", Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST: {
                if (grantResults.length > 0) {
                    boolean writeStorageaccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageaccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "Please Enable Storage Permissions", Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                assert result != null;
                uri = result.getUri();
                setImage(uri, this);

            }
        }
    }

    private void pickFromGallery() {
        CropImage.activity().start(MatchDetailActivity.this);
    }

}