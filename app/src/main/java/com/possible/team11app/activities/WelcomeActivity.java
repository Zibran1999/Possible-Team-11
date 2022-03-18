package com.possible.team11app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.possible.team11app.BuildConfig;
import com.possible.team11app.R;
import com.possible.team11app.databinding.ActivityWelcomeBinding;
import com.possible.team11app.utils.MyApp;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import de.hdodenhof.circleimageview.CircleImageView;

public class WelcomeActivity extends AppCompatActivity {
    private final String TAG = WelcomeActivity.class.getSimpleName();
    ActivityWelcomeBinding binding;
    CircleImageView rateBtn, startBtn, shareBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        startBtn = binding.startBtn;
        rateBtn = binding.rateBtn;
        shareBtn = binding.shareBtn;

        MyApp.showBannerAd(this, binding.adView);
        MyApp.showBannerAd(this, binding.adView2);

        startBtn.setOnClickListener(v -> {
            binding.lottieBall.setVisibility(View.VISIBLE);
            MyApp.showInterstitialAd(this);
            new Handler().postDelayed(() -> {
                startActivity(new Intent(getApplicationContext(), MyHomeActivity.class));
                binding.lottieBall.setVisibility(View.GONE);
            }, 3000);

        });
        rateBtn.setOnClickListener(v -> {
            rateApp(WelcomeActivity.this);
        });
        shareBtn.setOnClickListener(v -> shareApp());
    }
    public static void rateApp(Context context) {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        myAppLinkToMarket.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, " unable to find market app", Toast.LENGTH_LONG).show();
        }
    }
    private void contactUs() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setPackage("com.google.android.gm");
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"myexpertteam11@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "Hello");
        i.putExtra(Intent.EXTRA_TEXT, "I need some help regarding ");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(WelcomeActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }

    }

    private void whatsApp() throws UnsupportedEncodingException {
        String contact = "+91 8979854946"; // use country code with your phone number
        String url = "https://api.whatsapp.com/send?phone=" + contact + "&text=" + URLEncoder.encode("Hello, I need some help regarding ", "UTF-8");
        try {
            PackageManager pm = this.getPackageManager();
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setData(Uri.parse(url));
            startActivity(i);

        } catch (PackageManager.NameNotFoundException e) {
            try {
                PackageManager pm = this.getPackageManager();
                pm.getPackageInfo("com.whatsapp.w4b", PackageManager.GET_ACTIVITIES);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setData(Uri.parse(url));
                startActivity(i);
            } catch (PackageManager.NameNotFoundException exception) {
                e.printStackTrace();
                Toast.makeText(this, "WhatsApp is not installed on this Device.", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void shareApp() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
            String shareMessage = "\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}