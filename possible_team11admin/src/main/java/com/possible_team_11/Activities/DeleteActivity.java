package com.possible_team_11.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.possible_team_11.Adapters.CricketLiveScoreAdapter;
import com.possible_team_11.Adapters.FootBallAdapter;
import com.possible_team_11.Adapters.NewsAdapter;
import com.possible_team_11.Models.FootballDataModels.FootBallData;
import com.possible_team_11.Models.MatchDetailModels.AddMatchModel;
import com.possible_team_11.Models.MatchDetailModels.MatchDatum;
import com.possible_team_11.Models.MatchDetailModels.MatchViewModel;
import com.possible_team_11.Models.MatchNewsModels.NewsDatum;
import com.possible_team_11.R;
import com.possible_team_11.databinding.ActivityDeleteBinding;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeleteActivity extends AppCompatActivity {
    ActivityDeleteBinding binding;
    Button contestDeleteBtn, newsDeleteBtn, cricketMatchDeleteBtn, footballMatchDeleteBtn;
    MatchViewModel matchViewModel;
    Map<String, String> map = new HashMap<>();
    MaterialAlertDialogBuilder builder;
    List<MatchDatum> matchData = new ArrayList<>();
    List<FootBallData> footBallDataList = new ArrayList<>();
    List<NewsDatum> newsDatumList = new ArrayList<>();
    Dialog loadingDialog, editNewsDialog;

    Button uploadNewsBtn;
    ImageButton newsDismissBtn;
    ImageView newsImageView;
    EditText newsTitle, newsDesc;
    Uri uri3;
    Bitmap bitmap;
    String encodedImage3;
    String newsTitleString, newsDescString;
    RetrofitWebserviceEdit retrofitWebserviceEdit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeleteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        contestDeleteBtn = binding.deleteContestBtn;
        newsDeleteBtn = binding.deleteNews;
        cricketMatchDeleteBtn = binding.deleteCricketData;
        footballMatchDeleteBtn = binding.deleteFootballData;
        retrofitWebserviceEdit = RetrofitWebserviceEdit.retrofit.create(RetrofitWebserviceEdit.class);

        matchData.clear();
        footBallDataList.clear();
        newsDatumList.clear();
        matchViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(MatchViewModel.class);

        builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("DELETE")
                .setMessage("Do you want to delete?")
                .setNegativeButton("CANCEL", (dialog1, which) -> {

                });
        contestDeleteBtn.setOnClickListener(v -> {
            builder.setPositiveButton("DELETE", (dialog, which) -> deleteContest("https://minutenewsflash.com/admin/posible_team/APIs/delete_contest_api.php", null));
            builder.show();
            builder.setNeutralButton("", (dialog, which) -> {

            });

        });

        cricketMatchDeleteBtn.setOnClickListener(v -> showDeleteLayout(101, "https://minutenewsflash.com/admin/posible_team/APIs/delete_cricket_match_api.php"));
        footballMatchDeleteBtn.setOnClickListener(v -> showDeleteLayout(102, "https://minutenewsflash.com/admin/posible_team/APIs/delete_football_match_api.php"));
        newsDeleteBtn.setOnClickListener(v -> showDeleteLayout(103, "https://minutenewsflash.com/admin/posible_team/APIs/delete_news_api.php"));
        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);
    }


    @SuppressLint("NotifyDataSetChanged")
    private void showDeleteLayout(int code, String url) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.delete_layout);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(true);
        dialog.show();
        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        if (code == 101) {
            loadingDialog.show();
            matchViewModel.getMatchData().observe(this, matchDetailModel -> {
                matchData.clear();
                List<MatchDatum> matchDatumList = matchDetailModel.getData();
                if (matchDatumList != null) {
                    matchData.addAll(matchDatumList);
                    CricketLiveScoreAdapter cricketLiveScoreAdapter = new CricketLiveScoreAdapter(matchData, this, datum -> {

                        builder.setPositiveButton("DELETE", (dialog1, which) -> {
                            String id = datum.getId();
                            String img1 = datum.getImage1();
                            String img2 = datum.getImage2();
                            map.put("matchId", id);
                            map.put("path", "Cricket_Team_Images/" + img1);
                            map.put("path2", "Cricket_Team_Images/" + img2);
                            deleteContest(url, map);
                        });
                        builder.setNeutralButton("Edit", (dialog14, which) -> {
                            Intent intent = new Intent(DeleteActivity.this, EditActivity.class);
                            intent.putExtra("id", datum.getId());
                            intent.putExtra("img1", "https://minutenewsflash.com/admin/posible_team/APIs/Cricket_Team_Images/" + datum.getImage1());
                            intent.putExtra("img2", "https://minutenewsflash.com/admin/posible_team/APIs/Cricket_Team_Images/" + datum.getImage2());
                            intent.putExtra("uploadImg1", datum.getImage1());
                            intent.putExtra("uploadImg2", datum.getImage2());
                            intent.putExtra("team1", datum.getTeam1Name());
                            intent.putExtra("team2", datum.getTeam2Name());
                            intent.putExtra("date", datum.getMatchDate());
                            intent.putExtra("time", datum.getMatchTime());
                            intent.putExtra("desc", datum.getTeam2Name());
                            intent.putExtra("url", "https://minutenewsflash.com/admin/posible_team/APIs/update_cricket_match_data_api.php");
                            startActivity(intent);
                        });
                        builder.show();

                    });
                    recyclerView.setAdapter(cricketLiveScoreAdapter);
                    cricketLiveScoreAdapter.notifyDataSetChanged();
                    if (matchData.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "List is empty", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                    loadingDialog.dismiss();

                }
            });

        } else if (code == 102) {
            loadingDialog.show();
            matchViewModel.getFootballData().observe(this, footballDataModel -> {
                footBallDataList.clear();
                List<FootBallData> footBallData = footballDataModel.getData();
                if (footBallData != null) {
                    footBallDataList.addAll(footBallData);
                    FootBallAdapter footBallAdapter = new FootBallAdapter(footBallDataList, this, datum -> {

                        builder.setPositiveButton("DELETE", (dialog12, which) -> {
                            String id = datum.getId();
                            String img1 = datum.getImage1();
                            String img2 = datum.getImage2();
                            map.put("matchId", id);
                            map.put("path", "Football_Team_Images/" + img1);
                            map.put("path2", "Football_Team_Images/" + img2);
                            deleteContest(url, map);
                        });
                        builder.setNeutralButton("Edit", (dialog14, which) -> {
                            Intent intent = new Intent(DeleteActivity.this, EditActivity.class);
                            intent.putExtra("id", datum.getId());
                            intent.putExtra("img1", "https://minutenewsflash.com/admin/posible_team/APIs/Football_Team_Images/" + datum.getImage1());
                            intent.putExtra("img2", "https://minutenewsflash.com/admin/posible_team/APIs/Football_Team_Images/" + datum.getImage2());
                            intent.putExtra("uploadImg1", datum.getImage1());
                            intent.putExtra("uploadImg2", datum.getImage2());
                            intent.putExtra("team1", datum.getTeam1Name());
                            intent.putExtra("team2", datum.getTeam2Name());
                            intent.putExtra("date", datum.getMatchDate());
                            intent.putExtra("time", datum.getMatchTime());
                            intent.putExtra("desc", datum.getTeam2Name());
                            intent.putExtra("url", "https://softwaresreviewguides.com/dreamteam11/APIs/update_football_match_data_api.php");

                            startActivity(intent);
                        });
                        builder.show();
                    });
                    recyclerView.setAdapter(footBallAdapter);
                    footBallAdapter.notifyDataSetChanged();
                    if (footBallDataList.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "List is empty", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                    loadingDialog.dismiss();
                }
            });
        } else if (code == 103) {
            loadingDialog.show();

            matchViewModel.getMatchNewsData().observe(this, matchNewsModel -> {
                newsDatumList.clear();
                List<NewsDatum> newsData = matchNewsModel.getData();
                if (newsData != null) {
                    newsDatumList.addAll(newsData);
                    NewsAdapter newsAdapter = new NewsAdapter(newsDatumList, this, newsDatum -> {
                        builder.setPositiveButton("DELETE", (dialog13, which) -> {
                            String id = newsDatum.getId();
                            String img1 = newsDatum.getNewsImg();
                            map.put("id", id);
                            map.put("path", "Cricket_News_Images/" + img1);
                            deleteContest(url, map);
                        });
                        builder.setNeutralButton("Edit", (dialog14, which) -> {
                            String id = newsDatum.getId();
                            String newsImg = newsDatum.getNewsImg();
                            String newsTitle = newsDatum.getNewsTitle();
                            String newsDesc = newsDatum.getNewsDesc();
                            editNews(id, newsImg, newsTitle, newsDesc);
                        });

                        builder.show();
                    });
                    recyclerView.setAdapter(newsAdapter);
                    newsAdapter.notifyDataSetChanged();
                    if (newsDatumList.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "List is empty", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                    loadingDialog.dismiss();

                }
            });
        }


    }

    private void deleteContest(String url, Map<String, String> map) {
        loadingDialog.show();
        RetrofitWebservice retrofitWebservice = RetrofitWebservice.retrofit.create(RetrofitWebservice.class);
        if (map != null) {
            Call<AddMatchModel> call = retrofitWebservice.getDeleteDataRes(map, url);
            call.enqueue(new Callback<AddMatchModel>() {
                @Override
                public void onResponse(@NonNull Call<AddMatchModel> call, @NonNull Response<AddMatchModel> response) {
                    AddMatchModel matchModel = response.body();
                    assert matchModel != null;
                    if (response.isSuccessful()) {
                        Log.d("deleteResponse", matchModel.getMessage());
                        Toast.makeText(getApplicationContext(), matchModel.getMessage(), Toast.LENGTH_SHORT).show();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    } else {
                        Toast.makeText(getApplicationContext(), matchModel.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                    loadingDialog.dismiss();

                }

                @Override
                public void onFailure(@NonNull Call<AddMatchModel> call, @NonNull Throwable t) {
                    Log.d("deleteResponse", t.getMessage());
                    loadingDialog.dismiss();

                }
            });
        } else {

            Call<AddMatchModel> call = retrofitWebservice.getDeleteContestRes(url);
            call.enqueue(new Callback<AddMatchModel>() {
                @Override
                public void onResponse(@NonNull Call<AddMatchModel> call, @NonNull Response<AddMatchModel> response) {
                    AddMatchModel matchModel = response.body();
                    Log.d("body", String.valueOf(response.body()));
                    if (response.isSuccessful()) {

                        Toast.makeText(getApplicationContext(), Objects.requireNonNull(matchModel).getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(getApplicationContext(), Objects.requireNonNull(matchModel).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    loadingDialog.dismiss();
                }

                @Override
                public void onFailure(@NonNull Call<AddMatchModel> call, @NonNull Throwable t) {
                    Log.d("deleteResponseError", t.getMessage());
                    loadingDialog.dismiss();

                }
            });
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
        overridePendingTransition(0, 0);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void editNews(String idS, String newsImgS, String newsTitleS, String newsDescS) {
        editNewsDialog = new Dialog(this);
        editNewsDialog.setContentView(R.layout.news_layout);
        editNewsDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        editNewsDialog.getWindow().setBackgroundDrawable(getApplicationContext().getDrawable(R.drawable.back_bg));
        editNewsDialog.show();
        editNewsDialog.setCancelable(false);
        newsImageView = editNewsDialog.findViewById(R.id.news_imageView);
        newsDismissBtn = editNewsDialog.findViewById(R.id.newsDismissBtn);
        newsTitle = editNewsDialog.findViewById(R.id.news_title);
        newsDesc = editNewsDialog.findViewById(R.id.news_description);
        uploadNewsBtn = editNewsDialog.findViewById(R.id.upload_news_btn);
        newsTitle.setText(newsTitleS);
        newsDesc.setText(newsDescS);
        Glide.with(this).load("https://minutenewsflash.com/admin/posible_team/APIs/Cricket_News_Images/" + newsImgS).into(newsImageView);

        encodedImage3 = newsImgS;
        newsDismissBtn.setOnClickListener(v -> {
            encodedImage3 = "";
            editNewsDialog.dismiss();
        });

        newsImageView.setOnClickListener(v -> FileChooser(102));
        uploadNewsBtn.setOnClickListener(v -> {
            loadingDialog.show();
            newsTitleString = newsTitle.getText().toString().trim();
            newsDescString = newsDesc.getText().toString().trim();

            updateNews(idS, newsImgS);
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
        if (requestCode == 102 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri3 = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri3);
                bitmap = BitmapFactory.decodeStream(inputStream);
                newsImageView.setImageBitmap(bitmap);
                encodedImage3 = imageStore(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public String imageStore(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imageBytes = stream.toByteArray();
        return android.util.Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    private void updateNews(String idS, String newsImgS) {
        Map<String, String> map = new HashMap<>();
        if (encodedImage3.length() <= 100) {
            map.put("id", idS);
            map.put("img", encodedImage3);
            map.put("title", newsTitleString);
            map.put("desc", newsDescString);
            map.put("imgKey", "0");
            map.put("deleteImg", newsImgS);
        }
        if (encodedImage3.length() > 100) {
            map.put("id", idS);
            map.put("img", encodedImage3);
            map.put("title", newsTitleString);
            map.put("desc", newsDescString);
            map.put("imgKey", "1");
            map.put("deleteImg", newsImgS);
        }

        Log.d("mapKey",map.get("imgKey"));
        Call<AddMatchModel> call = retrofitWebserviceEdit.updateNewsData(map, "https://minutenewsflash.com/admin/posible_team/APIs/update_news_data_api.php");
        call.enqueue(new Callback<AddMatchModel>() {
            @Override
            public void onResponse(@NonNull Call<AddMatchModel> call, @NonNull Response<AddMatchModel> response) {
                AddMatchModel matchModel = response.body();
                if (response.isSuccessful()) {
                    assert matchModel != null;
                    Toast.makeText(getApplicationContext(), matchModel.getMessage(), Toast.LENGTH_SHORT).show();
                    editNewsDialog.dismiss();
                    Intent intent = new Intent(getApplicationContext(), DeleteActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    finish();
                    overridePendingTransition(0, 0);
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


}