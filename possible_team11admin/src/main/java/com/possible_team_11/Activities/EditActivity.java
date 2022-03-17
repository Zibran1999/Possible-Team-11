package com.possible_team_11.Activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.possible_team_11.Models.MatchDetailModels.AddMatchModel;
import com.possible_team_11.Models.MatchDetailModels.MatchPreview;
import com.possible_team_11.Models.MatchDetailModels.MatchViewModel;
import com.possible_team_11.Models.MatchDetailModels.MatchViewModelFactory;
import com.possible_team_11.R;
import com.possible_team_11.TeamPlayerModels.TeamPlayerModel;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Url;

interface RetrofitWebserviceEdit {

    Retrofit retrofit = new Retrofit.Builder()
            //https://softwaresreviewguides.com/dreamteam11/APIs/
            //https://gedgetsworld.in/teamdream11.com/
            .baseUrl("https://softwaresreviewguides.com/dreamteam11/APIs/")
            .addConverterFactory(GsonConverterFactory.create()).build();

    @FormUrlEncoded
    @POST()
    Call<AddMatchModel> updateMatchData(@FieldMap Map<String, String> map, @Url String url);

    @FormUrlEncoded
    @POST()
    Call<AddMatchModel> updateNewsData(@FieldMap Map<String, String> map, @Url String url);

    @FormUrlEncoded
    @POST()
    Call<AddMatchModel> updateTeamPlayerImage(@FieldMap Map<String, String> map, @Url String url);

    @FormUrlEncoded
    @POST()
    Call<AddMatchModel> updateTeamPreview(@FieldMap Map<String, String> map, @Url String url);


}

public class EditActivity extends AppCompatActivity {
    Button editMatchDetailsBtn, editMatchPreviewBtn, editMatchTeamImageBtn;
    Button uploadMatchBtn;
    Button dismissBtn;
    ImageView bestTeamImage;
    Dialog addMatchDialog, addCricketPreviewDialog, loadingDialog, bestTeamDialog;
    ImageView image_1, image_2;
    EditText team1Name, team2Name, matchDesc;
    TextView matchDate, matchTime;
    Uri uri, uri2, uri3;
    Bitmap bitmap;
    String encodedImage, encodedImage2, encodedImage3;
    String name1, name2, mDate, mDesc, formattedTime;

    String id, img1, img2, team1, team2, date, time, desc, url, uploadImg1, uploadImg2;
    MatchViewModel matchViewModel;
    MaterialButtonToggleGroup materialButtonToggleGroup;
    Button english, hindi, uploadBtn, cancelEdtBtn;
    EditText previewText;
    RetrofitWebserviceEdit retrofitWebserviceEdit;
    String teamImages, teamId;
    String hindiPreviewId, hindiTeamPreviewId, hindiString;
    String englishPreviewId, englishTeamPreviewId, englishString;
    Button editGrandMatchTeamImageBtn;


    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editMatchDetailsBtn = findViewById(R.id.edit_matchDetails_btn);
        editMatchPreviewBtn = findViewById(R.id.edit_match_preview_btn);
        editMatchTeamImageBtn = findViewById(R.id.edit_match_team_image_btn);
        editGrandMatchTeamImageBtn = findViewById(R.id.edit_grand_match_team_image_btn);
        id = getIntent().getStringExtra("id");
        img1 = getIntent().getStringExtra("img1");
        img2 = getIntent().getStringExtra("img2");
        team1 = getIntent().getStringExtra("team1");
        team2 = getIntent().getStringExtra("team2");
        uploadImg1 = getIntent().getStringExtra("uploadImg1");
        uploadImg2 = getIntent().getStringExtra("uploadImg2");
        date = getIntent().getStringExtra("date");
        time = getIntent().getStringExtra("time");
        desc = getIntent().getStringExtra("desc");
        url = getIntent().getStringExtra("url");

        retrofitWebserviceEdit = RetrofitWebserviceEdit.retrofit.create(RetrofitWebserviceEdit.class);

        editMatchDetailsBtn.setOnClickListener(v -> showEditLayout());
        editMatchPreviewBtn.setOnClickListener(v -> showPreviewLayout());
        editMatchTeamImageBtn.setOnClickListener(v -> showBestTeam("Head"));
        editGrandMatchTeamImageBtn.setOnClickListener(v -> showBestTeam("Grand"));
        //****Loading Dialog****/
        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.item_bg));
        loadingDialog.setCancelable(false);
        //**Loading Dialog****/
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void showBestTeam(String s) {
        bestTeamDialog = new Dialog(this);
        bestTeamDialog.setContentView(R.layout.edt_best_team_layout);
        bestTeamDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        bestTeamDialog.getWindow().setBackgroundDrawable(getApplicationContext().getDrawable(R.drawable.back_bg));

        bestTeamDialog.setCancelable(false);
        bestTeamDialog.show();
        Button cancelBtn, uploadBtn;
        bestTeamImage = bestTeamDialog.findViewById(R.id.bestPlayersImage);
        cancelBtn = bestTeamDialog.findViewById(R.id.cancel_best_preview);
        uploadBtn = bestTeamDialog.findViewById(R.id.update_cricket_preview);
        matchViewModel = new ViewModelProvider(this, new MatchViewModelFactory(this.getApplication(), id)).get(MatchViewModel.class);
        if (s.equals("Head")) {
            matchViewModel.getTeamImages().observe(this, teamPlayerImageModel -> {
                List<TeamPlayerModel> teamPlayerModelList = teamPlayerImageModel.getData();
                if (!teamPlayerModelList.isEmpty()) {
                    for (TeamPlayerModel t : teamPlayerModelList) {
                        String id = t.getId();
                        teamId = t.getTeamId();
                        teamImages = t.getTeamImage();

                        Log.d("onList", id + " " + teamId + " " + teamImages);
                        Glide.with(this).load("https://softwaresreviewguides.com/dreamteam11/APIs/Team_Player_Images/" + teamImages).into(bestTeamImage);

                    }
                    encodedImage3 = teamImages;
                }
            });
        } else if (s.equals("Grand")){
            matchViewModel.getGrandTeamImages().observe(this, teamPlayerImageModel -> {
                List<TeamPlayerModel> teamPlayerModelList = teamPlayerImageModel.getData();
                if (!teamPlayerModelList.isEmpty()) {
                    for (TeamPlayerModel t : teamPlayerModelList) {
                        String id = t.getId();
                        teamId = t.getTeamId();
                        teamImages = t.getTeamImage();

                        Log.d("onList", id + " " + teamId + " " + teamImages);
                        Glide.with(this).load("https://softwaresreviewguides.com/dreamteam11/APIs/Grand_Team_Player_Images/" + teamImages).into(bestTeamImage);

                    }
                    encodedImage3 = teamImages;
                }
            });

        }

        bestTeamImage.setOnClickListener(v -> FileChooser(103));


        cancelBtn.setOnClickListener(v -> {
            encodedImage3 = "";
            bestTeamDialog.dismiss();
        });
        uploadBtn.setOnClickListener(v -> {
            loadingDialog.show();

            if (s.equals("Head")) {
                updateBestTeam(id,"https://softwaresreviewguides.com/dreamteam11/APIs/update_team_player_images_api.php");
            } else if (s.equals("Grand")){
                updateBestTeam(id,"https://softwaresreviewguides.com/dreamteam11/APIs/update_grand_team_player_images_api.php");

            }

        });

    }

    private void updateBestTeam(String teamId, String s) {

        Map<String, String> map = new HashMap<>();

        if (encodedImage3.length() <= 100) {
            map.put("id", teamId);
            map.put("img", encodedImage3);
            map.put("updateImg", teamImages);
            map.put("imgKey", "0");
        }
        if (encodedImage3.length() > 100) {
            map.put("id", teamId);
            map.put("img", encodedImage3);
            map.put("updateImg", teamImages);
            map.put("imgKey", "1");
        }
        Call<AddMatchModel> call = retrofitWebserviceEdit.updateTeamPlayerImage(map, s);
        call.enqueue(new Callback<AddMatchModel>() {
            @Override
            public void onResponse(@NonNull Call<AddMatchModel> call, @NonNull Response<AddMatchModel> response) {
                AddMatchModel matchModel = response.body();
                if (response.isSuccessful()) {
                    assert matchModel != null;
                    Toast.makeText(getApplicationContext(), matchModel.getMessage(), Toast.LENGTH_SHORT).show();
                    bestTeamDialog.dismiss();
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
                Log.d("error response", t.getMessage());
                loadingDialog.dismiss();
            }
        });

    }

    private void showPreviewLayout() {
        addCricketPreviewDialog = new Dialog(this);
        addCricketPreviewDialog.setContentView(R.layout.edit_preview_layout);
        addCricketPreviewDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        addCricketPreviewDialog.setCancelable(false);
        addCricketPreviewDialog.show();
        previewText = addCricketPreviewDialog.findViewById(R.id.preview);
        materialButtonToggleGroup = addCricketPreviewDialog.findViewById(R.id.materialButtonToggleGroup);
        materialButtonToggleGroup.setVisibility(View.GONE);

        hindi = addCricketPreviewDialog.findViewById(R.id.hindiPreview);
        english = addCricketPreviewDialog.findViewById(R.id.englishPreview);
        uploadBtn = addCricketPreviewDialog.findViewById(R.id.update_btn);
        cancelEdtBtn = addCricketPreviewDialog.findViewById(R.id.cancel_edt_btn);

        fetchPreview(id);
        cancelEdtBtn.setOnClickListener(v -> addCricketPreviewDialog.dismiss());


    }

    @SuppressLint("NonConstantResourceId")
    private void fetchPreview(String id) {

        previewText.setVisibility(View.GONE);
        List<MatchPreview> matchPreviews = new ArrayList<>();
        matchViewModel = new ViewModelProvider(this, new MatchViewModelFactory(this.getApplication(), id)).get(MatchViewModel.class);
        matchViewModel.getMatchPreview().observe(this, matchPreviewModel -> {
            matchPreviews.clear();
            List<MatchPreview> matchPreviewList = matchPreviewModel.getData();
            matchPreviews.addAll(matchPreviewList);

            if (!matchPreviews.isEmpty()) {

                for (MatchPreview m : matchPreviews) {
                    if (m.getCricketTeamId().equals(id)) {
                        english.setBackgroundColor(Color.RED);
                        english.setTextColor(Color.WHITE);

                        String replaceString = m.getCricketTeamDesc().replaceAll("\\<.*?\\>", "");
                        String removeNumeric = replaceString.replaceAll("[0-9]", "");
                        Log.d("both data", removeNumeric.trim());

                        for (char c : removeNumeric.trim().toCharArray()) {
                            if (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.DEVANAGARI) {
                                hindiPreviewId = m.getId();
                                hindiTeamPreviewId = m.getCricketTeamId();
                                hindiString = m.getCricketTeamDesc();
                                Log.d("hindi", hindiString);
                                materialButtonToggleGroup.setVisibility(View.VISIBLE);

                                break;
                            } else {

                                if (englishString == null) {
                                    englishPreviewId = m.getId();
                                    englishTeamPreviewId = m.getCricketTeamId();
                                    englishString = m.getCricketTeamDesc();
                                    Log.d("english", englishString);

                                }

                            }

                        }
                    }
                }
                String finalEnglishString = englishString;
                String finalHindiString = hindiString;

                previewText.setVisibility(View.VISIBLE);
                previewText.setText(finalEnglishString);
                
                uploadBtn.setOnClickListener(v -> updatePreview(englishPreviewId, englishTeamPreviewId, previewText.getText().toString()));

                materialButtonToggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {

                    switch (checkedId) {
                        case R.id.englishPreview:
                            english.setBackgroundColor(Color.RED);
                            english.setTextColor(Color.WHITE);
                            hindi.setBackgroundColor(0);
                            hindi.setTextColor(Color.RED);
                            previewText.setText(finalEnglishString);
                            previewText.setVisibility(View.VISIBLE);
                            uploadBtn.setOnClickListener(v -> updatePreview(englishPreviewId, englishTeamPreviewId, previewText.getText().toString().trim()));
                            break;
                        case R.id.hindiPreview:
                            english.setBackgroundColor(0);
                            english.setTextColor(Color.RED);
                            hindi.setBackgroundColor(Color.RED);
                            hindi.setTextColor(Color.WHITE);
                            previewText.setText(finalHindiString);
                            previewText.setVisibility(View.VISIBLE);
                            hindiString = previewText.getText().toString().trim();
                            uploadBtn.setOnClickListener(v -> updatePreview(hindiPreviewId, hindiTeamPreviewId, previewText.getText().toString().trim()));

                            break;
                        default:
                    }
                });

            }
        });

    }

    private void updatePreview(String id, String teamId, String teamDesc) {
        loadingDialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("teamId", teamId);
        map.put("desc", teamDesc);
        Log.d("mapKey", map.get("id") + " " + map.get("teamId") + " " + map.get("teamDesc"));

        Call<AddMatchModel> call = retrofitWebserviceEdit.updateTeamPreview(map, "https://softwaresreviewguides.com/dreamteam11/APIs/update_team_preview_api.php");
        call.enqueue(new Callback<AddMatchModel>() {
            @Override
            public void onResponse(@NonNull Call<AddMatchModel> call, @NonNull Response<AddMatchModel> response) {
                AddMatchModel matchModel = response.body();
                if (response.isSuccessful()) {
                    assert matchModel != null;
                    Toast.makeText(getApplicationContext(), matchModel.getMessage(), Toast.LENGTH_SHORT).show();
                    addCricketPreviewDialog.dismiss();
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

    public void showEditLayout() {
        addMatchDialog = new Dialog(this);
        addMatchDialog.setContentView(R.layout.match_item_layout);
        addMatchDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        addMatchDialog.setCancelable(false);
        addMatchDialog.show();
        encodedImage = uploadImg1;
        encodedImage2 = uploadImg2;

        image_1 = addMatchDialog.findViewById(R.id.imageView1);
        image_2 = addMatchDialog.findViewById(R.id.imageView2);

        team1Name = addMatchDialog.findViewById(R.id.team_1_name);
        team2Name = addMatchDialog.findViewById(R.id.team_2_name);
        matchDate = addMatchDialog.findViewById(R.id.edt_match_date);
        matchTime = addMatchDialog.findViewById(R.id.edit_match_time_picker);
        matchDesc = addMatchDialog.findViewById(R.id.edt_match_desc);
        dismissBtn = addMatchDialog.findViewById(R.id.dialog_dismiss_btn);

        Glide.with(getApplicationContext()).load(img1).into(image_1);
        Glide.with(getApplicationContext()).load(img2).into(image_2);
        team1Name.setText(team1);
        team2Name.setText(team2);
        matchDate.setText(date);
        matchTime.setText(time);
        matchDesc.setText(desc);

        dismissBtn.setOnClickListener(v -> {
            encodedImage = "";
            encodedImage2 = "";
            addMatchDialog.dismiss();
        });
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

                    SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
                    Date date = null;
                    try {
                        date = fmt.parse(time);
                    } catch (ParseException e) {

                        e.printStackTrace();
                    }

                    SimpleDateFormat fmtOut = new SimpleDateFormat("hh:mm aa");

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
            mDate = matchDate.getText().toString().trim();
            formattedTime = matchTime.getText().toString().trim();

            updateData(id);
        });


    }

    private void updateData(String id) {
        Map<String, String> map = new HashMap<>();
        if (encodedImage.length() <= 100 && encodedImage2.length() <= 100) {
            map.put("img1", encodedImage);
            map.put("img2", encodedImage2);
            map.put("UpdateImg1", uploadImg1);
            map.put("UpdateImg2", uploadImg2);
            map.put("team1", name1);
            map.put("team2", name2);
            map.put("mDate", mDate);
            map.put("mTime", formattedTime);
            map.put("mDesc", mDesc);
            map.put("mId", id);
            map.put("imgKey", String.valueOf(0));
        }
        if (encodedImage.length() > 100) {
            map.put("img1", encodedImage);
            map.put("img2", encodedImage2);
            map.put("UpdateImg1", uploadImg1);
            map.put("UpdateImg2", uploadImg2);
            map.put("team1", name1);
            map.put("team2", name2);
            map.put("mDate", mDate);
            map.put("mTime", formattedTime);
            map.put("mDesc", mDesc);
            map.put("mId", id);
            map.put("imgKey", String.valueOf(101));
        }
        if (encodedImage2.length() > 101) {
            map.put("img1", encodedImage);
            map.put("img2", encodedImage2);
            map.put("UpdateImg1", uploadImg1);
            map.put("UpdateImg2", uploadImg2);
            map.put("team1", name1);
            map.put("team2", name2);
            map.put("mDate", mDate);
            map.put("mTime", formattedTime);
            map.put("mDesc", mDesc);
            map.put("mId", id);
            map.put("imgKey", String.valueOf(102));
        }
        if (encodedImage2.length() > 100 && encodedImage.length() > 100) {
            map.put("img1", encodedImage);
            map.put("img2", encodedImage2);
            map.put("UpdateImg1", uploadImg1);
            map.put("UpdateImg2", uploadImg2);
            map.put("team1", name1);
            map.put("team2", name2);
            map.put("mDate", mDate);
            map.put("mTime", formattedTime);
            map.put("mDesc", mDesc);
            map.put("mId", id);
            map.put("imgKey", String.valueOf(1));
        }

        Log.d("checkImageSelection", map.get("imgKey") + " " + encodedImage + "     key  " + encodedImage2);

        Call<AddMatchModel> call = retrofitWebserviceEdit.updateMatchData(map, url);
        call.enqueue(new Callback<AddMatchModel>() {
            @Override
            public void onResponse(@NonNull Call<AddMatchModel> call, @NonNull Response<AddMatchModel> response) {
                AddMatchModel matchModel = response.body();
                if (response.isSuccessful()) {
                    assert matchModel != null;
                    Toast.makeText(getApplicationContext(), matchModel.getMessage(), Toast.LENGTH_SHORT).show();
                    addMatchDialog.dismiss();
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
        } else if (requestCode == 103 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri3 = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri3);
                bitmap = BitmapFactory.decodeStream(inputStream);
                bestTeamImage.setImageBitmap(bitmap);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}