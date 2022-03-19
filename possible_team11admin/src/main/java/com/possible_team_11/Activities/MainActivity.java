package com.possible_team_11.Activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.core.content.ContextCompat;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputLayout;
import com.possible_team_11.Models.AdsModel;
import com.possible_team_11.Models.AdsModelList;
import com.possible_team_11.Models.AdsStatusModels.AdsStatus;
import com.possible_team_11.Models.AdsStatusModels.AdsStatusModel;
import com.possible_team_11.Models.Datum;
import com.possible_team_11.Models.MatchDetailModels.AddMatchModel;
import com.possible_team_11.Models.MatchDetailModels.ApiInterface;
import com.possible_team_11.Models.MatchDetailModels.MatchDatum;
import com.possible_team_11.Models.MatchDetailModels.MatchDetailModel;
import com.possible_team_11.Models.MatchDetailModels.MessageModel;
import com.possible_team_11.Models.MatchDetailModels.WebServices;
import com.possible_team_11.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;


interface RetrofitWebservice {

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://minutenewsflash.com/admin/posible_team/APIs/")
            .addConverterFactory(GsonConverterFactory.create()).build();

    @FormUrlEncoded
    @POST()
    Call<AddMatchModel> getMatchData(@FieldMap Map<String, String> map, @Url String url);

    @FormUrlEncoded
    @POST()
    Call<AddMatchModel> getNewsData(@FieldMap Map<String, String> map, @Url String url);

    @FormUrlEncoded
    @POST()
    Call<AddMatchModel> getPreview(@FieldMap Map<String, String> map, @Url String url);

    @FormUrlEncoded
    @POST()
    Call<AddMatchModel> getDeleteDataRes(@FieldMap Map<String, String> map, @Url String url);

    @POST()
    Call<AddMatchModel> getDeleteContestRes(@Url String url);

    @GET()
    Call<MatchDetailModel> getMatchDetailData(@Url String url);

    @GET()
    Call<AdsStatusModel> getAdsStatus(@Url String url);

    @FormUrlEncoded
    @POST()
    Call<AddMatchModel> postAdsStatus(@Url String url, @FieldMap Map<String, String> map);
}

public class MainActivity extends AppCompatActivity {
    Button cricketBtn, uploadMatchBtn, footballBtn, newsBtn, uploadNewsBtn, cricketPreviewBtn,
            previewCancelBtn, uploadCricketPreviewBtn, uploadCricketTeamPlayers, selectCricket, selectFootball, deleteBtn;
    Button dismissBtn, adIdUploadBtn, adIdCancelBtn;
    ImageButton newsDismissBtn;
    Dialog addMatchDialog, addCricketPreviewDialog, addNewsDialog, loadingDialog, choiceDialog, adsUpdateDialog;
    ImageView image_1, image_2, newsImageView, teamPlayersImageView;
    EditText team1Name, team2Name, matchDesc, newsTitle, newsDesc, cricketPreviewText, bannerIdTxt, interstitialIdTxt, nativeIdTxt, appopenAd;
    TextView matchDate, matchTime, adIdTitleTxt;
    TextInputLayout textInputLayout;
    Uri uri, uri2, uri3;
    Bitmap bitmap;
    String encodedImage, encodedImage2, encodedImage3;
    String name1, name2, mDate, mDesc, newsTitleString, newsDescString, matchId, teamDataText, formattedTime;

    ///////////////
    AppCompatAutoCompleteTextView appCompatAutoCompleteTextView;
    List<Datum> arrayList;
    List<String> arrayListS;
    ArrayAdapter<String> arrayAdapter;
    String getMatchTeamName, adIdTitle;
    RetrofitWebservice retrofitWebservice;
    boolean bannAds, intersAds;
    Button uploadGrandTeamPlayerImages;
    ApiInterface apiInterface;
    AdsModel adsModel;
    Map<String, String> map = new HashMap<>();
    Button uploadSimpleTeamPlayerImages;


    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deleteBtn = findViewById(R.id.delete);
        cricketBtn = findViewById(R.id.cricketBtn);
        footballBtn = findViewById(R.id.football_button);
        newsBtn = findViewById(R.id.news_button);
        cricketPreviewBtn = findViewById(R.id.uploadCricketPreviewBtn);
        uploadCricketTeamPlayers = findViewById(R.id.upload_cricket_match_team);
        uploadGrandTeamPlayerImages = findViewById(R.id.upload_grand_cricket_match_team);
        uploadSimpleTeamPlayerImages = findViewById(R.id.upload_simple_cricket_match_team);

        retrofitWebservice = RetrofitWebservice.retrofit.create(RetrofitWebservice.class);

        arrayList = new ArrayList<>();
        arrayListS = new ArrayList<>();

        arrayAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, arrayListS);

        cricketBtn.setOnClickListener(v -> {

            addMatch("https://minutenewsflash.com/admin/posible_team/APIs/add_match_api.php");
            addMatchDialog.show();
        });
        footballBtn.setOnClickListener(v -> {
            addMatch("https://minutenewsflash.com/admin/posible_team/APIs/add_football_data_api.php");
            addMatchDialog.show();
        });
        newsBtn.setOnClickListener(v -> addNews());
        cricketPreviewBtn.setOnClickListener(v -> userChoice(999, "https://minutenewsflash.com/admin/posible_team/APIs/add_preview_api.php"));
        uploadCricketTeamPlayers.setOnClickListener(v -> userChoice(777, "https://minutenewsflash.com/admin/posible_team/APIs/add_team_player_image_api.php"));
        uploadGrandTeamPlayerImages.setOnClickListener(v -> userChoice(777, "https://minutenewsflash.com/admin/posible_team/APIs/add_grand_team_player_image_api.php"));
        uploadSimpleTeamPlayerImages.setOnClickListener(v -> userChoice(777, "https://minutenewsflash.com/admin/posible_team/APIs/add_simple_team_player_images.php"));


        //****Loading Dialog****/
        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.item_bg));
        loadingDialog.setCancelable(false);
        //**Loading Dialog****/

        deleteBtn.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), DeleteActivity.class)));
        fetchAdsStatus();

        Button updateAdBtn = findViewById(R.id.update_ad_id);
        Button updateExpertAdBtn = findViewById(R.id.update_ad_id_2);
        Button updateDreamTeamAdBtn = findViewById(R.id.update_ad_id_3);
        updateAdBtn.setVisibility(View.GONE);
        updateDreamTeamAdBtn.setVisibility(View.GONE);
        updateAdBtn.setOnClickListener(v -> showUpdateAdsDialog("Prime Team"));
        updateExpertAdBtn.setOnClickListener(v -> showUpdateAdsDialog("My Expert Team"));
        updateDreamTeamAdBtn.setOnClickListener(v -> showUpdateAdsDialog("Dream Team"));


    }

    private void showUpdateAdsDialog(String id) {
        adsUpdateDialog = new Dialog(this);
        adsUpdateDialog.setContentView(R.layout.ad_id_layout);
        adsUpdateDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        adsUpdateDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.item_bg));
        adsUpdateDialog.setCancelable(false);
        adsUpdateDialog.show();

        bannerIdTxt = adsUpdateDialog.findViewById(R.id.banner_id);
        interstitialIdTxt = adsUpdateDialog.findViewById(R.id.interstitial_id);
        nativeIdTxt = adsUpdateDialog.findViewById(R.id.native_id);
        appopenAd = adsUpdateDialog.findViewById(R.id.appOpen);
        adIdTitleTxt = adsUpdateDialog.findViewById(R.id.ad_id_title);
        adIdUploadBtn = adsUpdateDialog.findViewById(R.id.upload_ids);
        adIdCancelBtn = adsUpdateDialog.findViewById(R.id.cancel_id);

            nativeIdTxt.setVisibility(View.GONE);
            interstitialIdTxt.setVisibility(View.GONE);

        adIdCancelBtn.setOnClickListener(v -> {
            adsUpdateDialog.dismiss();
        });

        apiInterface = WebServices.getInterface();
        Call<AdsModelList> call = apiInterface.fetchAds(id);
        call.enqueue(new Callback<AdsModelList>() {
            @Override
            public void onResponse(@NonNull Call<AdsModelList> call, @NonNull Response<AdsModelList> response) {
                if (response.isSuccessful()) {
                    if (Objects.requireNonNull(response.body()).getData() != null) {
                        for (AdsModel ads : response.body().getData()) {
                            adsModel = ads;
                            adIdTitle = ads.getId();
                            adIdTitleTxt.setText(adIdTitle);
                            bannerIdTxt.setText(ads.getBanner());
                            interstitialIdTxt.setText(ads.getInterstitial());
                            nativeIdTxt.setText(ads.getNativeADs());
                            appopenAd.setText(ads.getAppOpen());
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


        adIdUploadBtn.setOnClickListener(v -> {
            String bnId = bannerIdTxt.getText().toString().trim();
            String interId = interstitialIdTxt.getText().toString().trim();
            String nativeId = nativeIdTxt.getText().toString().trim();
            String appOpenId = appopenAd.getText().toString().trim();
            if (bnId.equals(adsModel.getBanner())
                    && interId.equals(adsModel.getInterstitial())
                    && nativeId.equals(adsModel.getNativeADs())
                    && appOpenId.equals(adsModel.getAppOpen())) {

                Toast.makeText(this, "No changes made in Ids", Toast.LENGTH_SHORT).show();

            } else {
                loadingDialog.show();
                map.put("id", adIdTitle);
                map.put("banner_id", bnId);
                map.put("inter_id", interId);
                map.put("native_id", nativeId);
                map.put("appOpen", appOpenId);
                updateAdIds(map);
            }
        });
    }

    private void updateAdIds(Map<String, String> map) {
        Call<MessageModel> call = apiInterface.updateAdIds(map);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NonNull Call<MessageModel> call, @NonNull Response<MessageModel> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
                adsUpdateDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<MessageModel> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });
    }


    private void fetchAdsStatus() {
        loadingDialog.show();
        Call<AdsStatusModel> call = retrofitWebservice.getAdsStatus("https://minutenewsflash.com/admin/posible_team/APIs/fetch_ads_status_api.php");
        call.enqueue(new Callback<AdsStatusModel>() {
            @Override
            public void onResponse(Call<AdsStatusModel> call, Response<AdsStatusModel> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    List<AdsStatus> adsStatuses = response.body().getAdsStatuses();
                    for (AdsStatus ads : adsStatuses) {
                        bannAds = Boolean.parseBoolean(ads.getBannerAds());
                        intersAds = Boolean.parseBoolean(ads.getInterstitialAds());
                    }
                    loadingDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<AdsStatusModel> call, Throwable t) {

            }
        });

    }


    private void uploadAdsStatus(String adsName, boolean bannerAdsBool, boolean interstitialAdsBool) {
        Map<String, String> booleanMap = new HashMap<>();
        booleanMap.put("bannerAds", String.valueOf(bannerAdsBool));
        booleanMap.put("interstitialAds", String.valueOf(interstitialAdsBool));
        Call<AddMatchModel> call = retrofitWebservice.postAdsStatus("https://minutenewsflash.com/admin/posible_team/APIs/add_ads_status_api.php", booleanMap);
        call.enqueue(new Callback<AddMatchModel>() {
            @Override
            public void onResponse(@NonNull Call<AddMatchModel> call, @NonNull Response<AddMatchModel> response) {
                AddMatchModel matchModel = response.body();
                if (response.isSuccessful()) {
                    assert matchModel != null;
                    Log.d("Retrofit Response", matchModel.getMessage());
                    Toast.makeText(getApplicationContext(), adsName, Toast.LENGTH_SHORT).show();

                } else {
                    assert response.body() != null;
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    Log.d("Retrofit Response", response.body().getError());

                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<AddMatchModel> call, @NonNull Throwable t) {
                Log.d("error response", t.getMessage());
                loadingDialog.dismiss();
            }
        });

    }

    private void userChoice(int code, String mUrl) {
        choiceDialog = new Dialog(this);
        choiceDialog.setContentView(R.layout.choice_layout);
        choiceDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        choiceDialog.setCancelable(true);
        choiceDialog.show();

        selectCricket = choiceDialog.findViewById(R.id.buttonCricket);
        selectFootball = choiceDialog.findViewById(R.id.buttonFootball);

        selectCricket.setOnClickListener(v -> {
            addCricketPreview(code, mUrl, "cricket");
            choiceDialog.dismiss();
        });
        selectFootball.setOnClickListener(v -> {
            addCricketPreview(code, mUrl, "football");
            choiceDialog.dismiss();
        });

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void addCricketPreview(int i, String apiUrl, String cricket) {
        addCricketPreviewDialog = new Dialog(this);
        addCricketPreviewDialog.setContentView(R.layout.cricket_preview_layout);
        addCricketPreviewDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        addCricketPreviewDialog.getWindow().setBackgroundDrawable(getApplicationContext().getDrawable(R.drawable.back_bg));
        addCricketPreviewDialog.setCancelable(false);
        addCricketPreviewDialog.show();

        appCompatAutoCompleteTextView = addCricketPreviewDialog.findViewById(R.id.drop_down_text);
        textInputLayout = addCricketPreviewDialog.findViewById(R.id.textInputLayout3);
        cricketPreviewText = addCricketPreviewDialog.findViewById(R.id.cricket_preview_text);
        teamPlayersImageView = addCricketPreviewDialog.findViewById(R.id.teamPlayersImage);
        previewCancelBtn = addCricketPreviewDialog.findViewById(R.id.cancel_match_preview);
        uploadCricketPreviewBtn = addCricketPreviewDialog.findViewById(R.id.upload_cricket_preview);

        if (cricket.equals("cricket")) {
            arrayList.clear();
            arrayListS.clear();
            fetchMatchDetails("https://minutenewsflash.com/admin/posible_team/APIs/fetch_match_data_api.php");
        } else {
            arrayList.clear();
            arrayListS.clear();
            fetchMatchDetails("https://minutenewsflash.com/admin/posible_team/APIs/fetch_football_match_data_api.php");
        }
        if (i == 999) {
            textInputLayout.setVisibility(View.VISIBLE);
            cricketPreviewText.setVisibility(View.VISIBLE);
            teamPlayersImageView.setVisibility(View.GONE);
        }
        if (i == 777) {
            textInputLayout.setVisibility(View.GONE);
            cricketPreviewText.setVisibility(View.GONE);
            teamPlayersImageView.setVisibility(View.VISIBLE);
        }
        teamPlayersImageView.setOnClickListener(v -> FileChooser(103));
        previewCancelBtn.setOnClickListener(v -> addCricketPreviewDialog.dismiss());

        appCompatAutoCompleteTextView.setInputType(0);
        appCompatAutoCompleteTextView.setAdapter(arrayAdapter);
        appCompatAutoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            getMatchTeamName = arrayListS.get(position);
            Toast.makeText(MainActivity.this, getMatchTeamName, Toast.LENGTH_SHORT).show();
        });

        uploadCricketPreviewBtn.setOnClickListener(v -> {
            loadingDialog.show();
            if (i == 999) {
                teamDataText = cricketPreviewText.getText().toString().trim();
            }
            if (i == 777) {
                teamDataText = encodedImage3;
            }

            for (Datum d : arrayList) {
                if ((d.getTeam1Name() + " Vs " + d.getTeam2Name()).equals(getMatchTeamName)) {
                    matchId = d.getId();
                    break;
                }
            }
            if (TextUtils.isEmpty(teamDataText)) {
                cricketPreviewText.setError("Enter Your Preview");
                loadingDialog.dismiss();
            } else if (TextUtils.isEmpty(matchId)) {
                appCompatAutoCompleteTextView.setError("Please select your team");
                loadingDialog.dismiss();
            } else if (teamDataText != null) {
                uploadMatchPreview(apiUrl, teamDataText);
            }

        });


    }

    private void uploadMatchPreview(String apiUrl, String text) {

        Map<String, String> map = new HashMap<>();
        map.put("id", matchId);
        map.put("desc", text);

        Call<AddMatchModel> call = retrofitWebservice.getPreview(map, apiUrl);
        call.enqueue(new Callback<AddMatchModel>() {
            @Override
            public void onResponse(@NonNull Call<AddMatchModel> call, @NonNull Response<AddMatchModel> response) {
                AddMatchModel matchModel = response.body();
                Log.e("Team Player", String.valueOf(response.body()));
                if (response.isSuccessful()) {
                    Log.d("Retrofit Response", Objects.requireNonNull(matchModel).getMessage());
                    Toast.makeText(getApplicationContext(), matchModel.getMessage(), Toast.LENGTH_SHORT).show();
                    choiceDialog.dismiss();
                    addCricketPreviewDialog.dismiss();

                } else {
                    Toast.makeText(getApplicationContext(), Objects.requireNonNull(matchModel).getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("Retrofit Response", matchModel.getError());

                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<AddMatchModel> call, @NonNull Throwable t) {
                Log.d("error response", t.getMessage());
                loadingDialog.dismiss();
            }
        });

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void addNews() {
        addNewsDialog = new Dialog(this);
        addNewsDialog.setContentView(R.layout.news_layout);
        addNewsDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        addNewsDialog.getWindow().setBackgroundDrawable(getApplicationContext().getDrawable(R.drawable.back_bg));
        addNewsDialog.show();
        addNewsDialog.setCancelable(false);
        newsImageView = addNewsDialog.findViewById(R.id.news_imageView);
        newsDismissBtn = addNewsDialog.findViewById(R.id.newsDismissBtn);
        newsTitle = addNewsDialog.findViewById(R.id.news_title);
        newsDesc = addNewsDialog.findViewById(R.id.news_description);
        uploadNewsBtn = addNewsDialog.findViewById(R.id.upload_news_btn);

        newsDismissBtn.setOnClickListener(v -> addNewsDialog.dismiss());

        newsImageView.setOnClickListener(v -> FileChooser(102));
        uploadNewsBtn.setOnClickListener(v -> {
            loadingDialog.show();
            newsTitleString = newsTitle.getText().toString().trim();
            newsDescString = newsDesc.getText().toString().trim();

            if (uri3 == null) {
                Toast.makeText(MainActivity.this, "Please select Image", Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            } else if (TextUtils.isEmpty(newsTitleString)) {
                newsTitle.setError("Field Required");
                loadingDialog.dismiss();
            } else if (TextUtils.isEmpty(newsDescString)) {
                newsDesc.setError("Field Required");
                loadingDialog.dismiss();
            } else {
                uploadNews();
            }
        });

    }

    private void uploadNews() {

        Map<String, String> map = new HashMap<>();
        UUID uuid = UUID.randomUUID();
        map.put("img", encodedImage3);
        map.put("title", newsTitleString);
        map.put("desc", newsDescString);
        map.put("id", uuid.toString());
        Call<AddMatchModel> call = retrofitWebservice.getNewsData(map, "https://minutenewsflash.com/admin/posible_team/APIs/add_cricket_news_api.php");
        call.enqueue(new Callback<AddMatchModel>() {
            @Override
            public void onResponse(@NonNull Call<AddMatchModel> call, @NonNull Response<AddMatchModel> response) {
                AddMatchModel matchModel = response.body();
                if (response.isSuccessful()) {
                    assert matchModel != null;
                    Toast.makeText(getApplicationContext(), matchModel.getMessage(), Toast.LENGTH_SHORT).show();
                    addNewsDialog.dismiss();
                } else {
                    assert matchModel != null;
                    Toast.makeText(getApplicationContext(), matchModel.getMessage(), Toast.LENGTH_SHORT).show();

                    assert response.body() != null;
                    Log.d("responseError", response.body().getError());
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<AddMatchModel> call, @NonNull Throwable t) {
                Log.d("responseError", t.getMessage());
                loadingDialog.dismiss();


            }
        });


    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private void addMatch(String url) {
        addMatchDialog = new Dialog(this);
        addMatchDialog.setContentView(R.layout.match_item_layout);
        addMatchDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        addMatchDialog.setCancelable(false);

        image_1 = addMatchDialog.findViewById(R.id.imageView1);
        image_2 = addMatchDialog.findViewById(R.id.imageView2);

        team1Name = addMatchDialog.findViewById(R.id.team_1_name);
        team2Name = addMatchDialog.findViewById(R.id.team_2_name);
        matchDate = addMatchDialog.findViewById(R.id.edt_match_date);
        matchTime = addMatchDialog.findViewById(R.id.edit_match_time_picker);
        matchDesc = addMatchDialog.findViewById(R.id.edt_match_desc);
        dismissBtn = addMatchDialog.findViewById(R.id.dialog_dismiss_btn);

        dismissBtn.setOnClickListener(v -> addMatchDialog.dismiss());
        image_1.setOnClickListener(v -> FileChooser(100));
        image_2.setOnClickListener(v -> FileChooser(101));
        uploadMatchBtn = addMatchDialog.findViewById(R.id.upload_match_btn);


        matchDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog pickerDialog = new DatePickerDialog(this, (datePicker, year1, month1, dayOfMonth) -> {
                String dateStr = "" + dayOfMonth + "-" + (month1 + 1) + "-" + year1;
                matchDate.setText(dateStr);
                mDate = dateStr;
            }, year, month, day);
            pickerDialog.show();
        });
        matchTime.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String time = hourOfDay + ":" + minute;

                    @SuppressLint("SimpleDateFormat") SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
                    Date date = null;
                    try {
                        date = fmt.parse(time);
                    } catch (ParseException e) {

                        e.printStackTrace();
                    }

                    @SuppressLint("SimpleDateFormat") SimpleDateFormat fmtOut = new SimpleDateFormat("hh:mm aa");

                    formattedTime = fmtOut.format(date);
                    matchTime.setText(formattedTime);

                }
            }, hour, minute, false);
            timePickerDialog.show();
        });

        uploadMatchBtn.setOnClickListener(v -> {
            loadingDialog.show();
            name1 = team1Name.getText().toString().trim();
            name2 = team2Name.getText().toString().trim();
            mDesc = matchDesc.getText().toString().trim();

            if (uri == null && uri2 == null) {
                Toast.makeText(MainActivity.this, "Please Select Images", Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            } else if (TextUtils.isEmpty(name1)) {
                team1Name.setError("Field Required");
                loadingDialog.dismiss();
            } else if (TextUtils.isEmpty(name2)) {
                team2Name.setError("Field Required");
                loadingDialog.dismiss();
            } else if (TextUtils.isEmpty(mDate)) {
                matchDate.setError("Field Required");
                loadingDialog.dismiss();
            } else if (TextUtils.isEmpty(formattedTime)) {
                matchTime.setError("Field Required");
                loadingDialog.dismiss();
            } else if (TextUtils.isEmpty(mDesc)) {
                matchDesc.setError("Field Required");
            } else {
                uploadData(url);
            }
        });
    }

    private void FileChooser(int i) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        startActivityForResult(intent, i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                bitmap = BitmapFactory.decodeStream(inputStream);
                image_1.setImageBitmap(bitmap);
                encodedImage = imageStore(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (requestCode == 101 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri2 = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri2);
                bitmap = BitmapFactory.decodeStream(inputStream);
                image_2.setImageBitmap(bitmap);
                encodedImage2 = imageStore(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (requestCode == 102 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri3 = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri3);
                bitmap = BitmapFactory.decodeStream(inputStream);
                newsImageView.setImageBitmap(bitmap);
                encodedImage3 = imageStore(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (requestCode == 103 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri3 = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri3);
                bitmap = BitmapFactory.decodeStream(inputStream);
                teamPlayersImageView.setImageBitmap(bitmap);
                encodedImage3 = imageStore(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void uploadData(String apiUrl) {
        UUID uuid = UUID.randomUUID();
        Map<String, String> map = new HashMap<>();
        map.put("img1", encodedImage);
        map.put("img2", encodedImage2);
        map.put("team1", name1);
        map.put("team2", name2);
        map.put("mDate", mDate);
        map.put("mTime", formattedTime);
        map.put("mDesc", mDesc);
        map.put("mId", uuid.toString());
        Call<AddMatchModel> call = retrofitWebservice.getMatchData(map, apiUrl);
        call.enqueue(new Callback<AddMatchModel>() {
            @Override
            public void onResponse(@NonNull Call<AddMatchModel> call, @NonNull Response<AddMatchModel> response) {
                AddMatchModel matchModel = response.body();
                if (response.isSuccessful()) {
                    assert matchModel != null;
                    Log.d("Retrofit Response", matchModel.getMessage());
                    Toast.makeText(getApplicationContext(), matchModel.getMessage(), Toast.LENGTH_SHORT).show();
                    addMatchDialog.dismiss();

                } else {
                    assert response.body() != null;
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    Log.d("Retrofit Response", response.body().getError());

                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<AddMatchModel> call, @NonNull Throwable t) {
                Log.d("error response", t.getMessage());
                loadingDialog.dismiss();
            }
        });

    }

    public String imageStore(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imageBytes = stream.toByteArray();
        return android.util.Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    private void fetchMatchDetails(String url) {

        Call<MatchDetailModel> call = retrofitWebservice.getMatchDetailData(url);
        call.enqueue(new Callback<MatchDetailModel>() {
            @Override
            public void onResponse(@NonNull Call<MatchDetailModel> call, @NonNull Response<MatchDetailModel> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    List<MatchDatum> matchDatumList = response.body().getData();
                    if (!matchDatumList.isEmpty()) {
                        for (MatchDatum m : matchDatumList) {

                            String id = m.getId();
                            String team1Image = m.getImage1();
                            String team2Image = m.getImage2();
                            String team1 = m.getTeam1Name();
                            String team2 = m.getTeam2Name();
                            String mDate = m.getMatchDate();
                            String mDesc = m.getMatchDesc();
                            arrayList.add(new Datum(id, team1Image, team2Image, team1, team2, mDate, mDesc));
                            arrayListS.add(team1 + " Vs " + team2);
                            arrayAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MatchDetailModel> call, @NonNull Throwable t) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}