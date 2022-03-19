package com.possible.team11app.activities.ui.main;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.possible.team11app.R;
import com.possible.team11app.activities.MatchDetailActivity;
import com.possible.team11app.activities.MyBestTeamActivity;
import com.possible.team11app.adapters.CodesAdapter;
import com.possible.team11app.databinding.FragmentMatchDetailsBinding;
import com.possible.team11app.models.ContestCodeModels.CodesModel;
import com.possible.team11app.models.MatchDetailModels.AddMatchModel;
import com.possible.team11app.models.MatchDetailModels.ApiInterface;
import com.possible.team11app.models.MatchDetailModels.MatchPreview;
import com.possible.team11app.models.MatchDetailModels.MatchViewModel;
import com.possible.team11app.models.MatchDetailModels.MatchViewModelFactory;
import com.possible.team11app.models.MatchDetailModels.WebServices;
import com.possible.team11app.utils.AdsViewModel;
import com.possible.team11app.utils.MyApp;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchDetailsFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final int STORAGE_REQUEST = 200;
    private static final int CAMERA_REQUEST = 100;
    public static CircleImageView profileImage;
    public static String encodedImage;
    @SuppressLint("StaticFieldLeak")
    static MatchDetailsFragment fragment;
    static Bitmap bitmap;
    int index = 1;
    Dialog loadingDialog, dialog;
    MatchViewModel matchViewModel;
    RecyclerView recyclerView;
    LottieAnimationView lottieAnimationView;
    View bestTeamView;
    Intent intent;
    String[] storagePermission;
    String[] cameraPermission;
    RelativeLayout previewAd, teamAd, codeView;
    Button headBtn, grandBtn, simpleBtn;
    List<CodesModel> codesModelList = new ArrayList<>();
    private FragmentMatchDetailsBinding binding;

    public static MatchDetailsFragment newInstance(int index) {

        fragment = new MatchDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static String imageStore(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, stream);
        byte[] imageBytes = stream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }


    public static void setImage(Uri uri, Context context) {

        if (uri != null) {
            try {
                InputStream inputStream = context.getContentResolver().openInputStream(uri);
                bitmap = BitmapFactory.decodeStream(inputStream);
                profileImage.setImageBitmap(bitmap);
                encodedImage = imageStore(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PageViewModel pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);

        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @SuppressLint({"NotifyDataSetChanged", "NonConstantResourceId", "UseCompatLoadingForDrawables"})
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentMatchDetailsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        recyclerView = binding.matchRecyclerView;
        lottieAnimationView = binding.matchLottieRV;
        codeView = binding.codeAdview;
        Button addContest = binding.addContest;
        LinearLayoutManager layoutManager = new LinearLayoutManager(root.getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);


        //****Loading Dialog****/
        loadingDialog = new Dialog(root.getContext());
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.getWindow().setBackgroundDrawable(root.getContext().getDrawable(R.drawable.item_bg));
        loadingDialog.setCancelable(false);
        //**Loading Dialog****/

        if (MatchDetailActivity.adCount == 1) {
            MyApp.showInterstitialAd(requireActivity());
            MatchDetailActivity.adCount++;
        }
        if (index == 1) {

            View view = root.findViewById(R.id.preview);
            WebView previewText = view.findViewById(R.id.preview_data);
            MaterialButtonToggleGroup materialButtonToggleGroup = view.findViewById(R.id.materialButtonToggleGroup);
            materialButtonToggleGroup.setVisibility(View.GONE);
            Button hindi, english;
            hindi = view.findViewById(R.id.hindiPreview);
            english = view.findViewById(R.id.englishPreview);
            previewAd = view.findViewById(R.id.previewAdview);
            MyApp.showBannerAd(requireActivity(), previewAd);


            previewText.setVisibility(View.GONE);
            List<MatchPreview> matchPreviews = new ArrayList<>();
            lottieAnimationView.setAnimation(R.raw.loding_dot);
            lottieAnimationView.playAnimation();
            matchViewModel = new ViewModelProvider(requireActivity(),
                    new MatchViewModelFactory(requireActivity().getApplication(), MatchDetailActivity.id.trim())).get(MatchViewModel.class);
            matchViewModel.getMatchPreview().observe(requireActivity(), matchPreviewModel -> {
                matchPreviews.clear();
                List<MatchPreview> matchPreviewList = matchPreviewModel.getData();
                matchPreviews.addAll(matchPreviewList);

                if (!matchPreviewList.isEmpty()) {
                    MyApp.showBannerAd(requireActivity(), previewAd);
                    codeView.setVisibility(View.GONE);
                    if (MatchDetailActivity.adCount == 1) {
                            new Handler().postDelayed(() -> MyApp.showInterstitialAd(requireActivity()), 2000);
                        MatchDetailActivity.adCount++;
                    }

                    String hindiString = null;
                    String englishString = null;
                    for (MatchPreview m : matchPreviews) {
                        if (m.getCricketTeamId().equals(MatchDetailActivity.id)) {
                            hindi.setBackgroundColor(Color.RED);
                            hindi.setTextColor(Color.WHITE);

                            String replaceString = m.getCricketTeamDesc().replaceAll("<.*?>", "");
                            String removeNumeric = replaceString.replaceAll("[0-9]", "");

                            for (char c : removeNumeric.trim().toCharArray()) {
                                if (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.DEVANAGARI) {
                                    hindiString = m.getCricketTeamDesc();
                                    break;
                                } else {

                                    if (englishString == null) {
                                        englishString = m.getCricketTeamDesc();
                                        materialButtonToggleGroup.setVisibility(View.VISIBLE);

                                    }

                                }

                            }

                            addContest.setVisibility(View.GONE);
                            lottieAnimationView.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.GONE);
                            view.setVisibility(View.VISIBLE);
                        }
                    }
                    String finalEnglishString = englishString;
                    String finalHindiString = hindiString;

                    previewText.setVisibility(View.VISIBLE);
                    previewText.loadData(finalHindiString, "text/html", "UTF-8");

                    english.setBackgroundColor(0);
                    english.setTextColor(Color.RED);
                    materialButtonToggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {

                        switch (checkedId) {

                            case R.id.hindiPreview:
                                english.setBackgroundColor(0);
                                english.setTextColor(Color.RED);
                                hindi.setBackgroundColor(Color.RED);
                                hindi.setTextColor(Color.WHITE);
                                MyApp.showBannerAd(requireActivity(), previewAd);
                                previewText.loadDataWithBaseURL(null, finalHindiString, "text/html", "UTF-8", null);
                                previewText.setVisibility(View.VISIBLE);
                                break;
                            case R.id.englishPreview:
                                english.setBackgroundColor(Color.RED);
                                english.setTextColor(Color.WHITE);
                                hindi.setBackgroundColor(0);
                                MyApp.showBannerAd(requireActivity(), previewAd);
                                hindi.setTextColor(Color.RED);
                                previewText.loadDataWithBaseURL(null, finalEnglishString, "text/html", "UTF-8", null);
                                previewText.setVisibility(View.VISIBLE);
                                break;
                            default:
                        }
                    });

                } else {
                    lottieAnimationView.setAnimation(R.raw.empty);
                    lottieAnimationView.playAnimation();

                }
            });


        } else if (index == 2) {

            addContest.setOnClickListener(v -> addContestDialog(root.getContext()));
            lottieAnimationView.setAnimation(R.raw.loding_dot);
            lottieAnimationView.playAnimation();
            addContest.setVisibility(View.VISIBLE);
            showContestCode();


        } else if (index == 3) {
            addContest.setVisibility(View.GONE);
            bestTeamView = root.findViewById(R.id.best_team);
            bestTeamView.setVisibility(View.VISIBLE);
            lottieAnimationView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            binding.nestedScrollView.setVisibility(View.GONE);
            headBtn = bestTeamView.findViewById(R.id.head_to_head_btn);
            grandBtn = bestTeamView.findViewById(R.id.grand_league_team_btn);
            simpleBtn = bestTeamView.findViewById(R.id.simple_team_btn);
            teamAd = bestTeamView.findViewById(R.id.teamMaxAdview);
            MyApp.showBannerAd(requireActivity(), teamAd);

            headBtn.setOnClickListener(view -> {

                AdsViewModel.destroyBanner();
                loadingDialog.show();
                // appOpenManager = new AppOpenManager(MyApp.mInstance, Paper.book().read(Prevalent.openAppAds), requireActivity());


                intent = new Intent(root.getContext(), MatchDetailActivity.class);
                intent.putExtra("teamName", "Head");
                startActivity(intent);

                FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity());
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Head To Head");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Head team Click");
                mFirebaseAnalytics.logEvent("Selected_head_team_item", bundle);

//                if (MyApp.mInterstitialAd != null) {
//                    MyApp.mInterstitialAd.show(requireActivity());
//                    MyApp.mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
//                        @Override
//                        public void onAdDismissedFullScreenContent() {
//                            // Called when fullscreen content is dismissed.
//                            intent = new Intent(root.getContext(), MyBestTeam.class);
//                            intent.putExtra("teamName", "Head");
//                            startActivity(intent);
//
//                            FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity());
//                            Bundle bundle = new Bundle();
//                            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Head To Head");
//                            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Head team Click");
//                            mFirebaseAnalytics.logEvent("Selected_head_team_item", bundle);
//                        }
//
//                        @Override
//                        public void onAdFailedToShowFullScreenContent(AdError adError) {
//                            // Called when fullscreen content failed to show.
//                            Log.d("TAG", "The ad failed to show.");
//                        }
//
//                        @Override
//                        public void onAdShowedFullScreenContent() {
//                            // Called when fullscreen content is shown.
//                            // Make sure to set your reference to null so you don't
//                            // show it a second time.
//                            MyApp.mInterstitialAd = null;
//                            Log.d("TAG", "The ad was shown.");
//                        }
//                    });
//                } else {
//                    MyApp.showInterstitialAd(requireActivity());
//                    intent = new Intent(root.getContext(), MyBestTeam.class);
//                    intent.putExtra("teamName", "Head");
//                    startActivity(intent);
//                    FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity());
//                    Bundle bundle = new Bundle();
//                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Head To Head");
//                    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Head team Click");
//                    mFirebaseAnalytics.logEvent("Selected_head_team_item", bundle);
//                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
//
//                }

            });
            grandBtn.setOnClickListener(view -> {
                loadingDialog.show();
                //appOpenManager = new AppOpenManager(MyApp.mInstance, Paper.book().read(Prevalent.openAppAds), requireActivity());

                AdsViewModel.destroyBanner();


                intent = new Intent(root.getContext(), MyBestTeamActivity.class);
                intent.putExtra("teamName", "Grand");
                startActivity(intent);

                FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity());
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Grand Team");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Grand team Click");
                mFirebaseAnalytics.logEvent("Selected_grand_team_item", bundle);

//                if (MyApp.mInterstitialAd != null) {
//                    MyApp.mInterstitialAd.show(requireActivity());
//                    MyApp.mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
//                        @Override
//                        public void onAdDismissedFullScreenContent() {
//                            // Called when fullscreen content is dismissed.
//                            intent = new Intent(root.getContext(), MyBestTeam.class);
//                            intent.putExtra("teamName", "Grand");
//                            startActivity(intent);
//
//                            FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity());
//                            Bundle bundle = new Bundle();
//                            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Grand Team");
//                            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Grand team Click");
//                            mFirebaseAnalytics.logEvent("Selected_grand_team_item", bundle);
//
//                        }
//
//                        @Override
//                        public void onAdFailedToShowFullScreenContent(AdError adError) {
//                            // Called when fullscreen content failed to show.
//                            Log.d("TAG", "The ad failed to show.");
//                        }
//
//                        @Override
//                        public void onAdShowedFullScreenContent() {
//                            // Called when fullscreen content is shown.
//                            // Make sure to set your reference to null so you don't
//                            // show it a second time.
//                            MyApp.mInterstitialAd = null;
//                            Log.d("TAG", "The ad was shown.");
//                        }
//                    });
//                } else {
//                    MyApp.showInterstitialAd(requireActivity());
//                    intent = new Intent(root.getContext(), MyBestTeam.class);
//                    intent.putExtra("teamName", "Grand");
//                    startActivity(intent);
//
//                    FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity());
//                    Bundle bundle = new Bundle();
//                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Grand Team");
//                    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Grand team Click");
//                    mFirebaseAnalytics.logEvent("Selected_grand_team_item", bundle);
//
//
//                }

            });
            simpleBtn.setOnClickListener(v -> {
                loadingDialog.show();
                // appOpenManager = new AppOpenManager(MyApp.mInstance, Paper.book().read(Prevalent.openAppAds), requireActivity());
                AdsViewModel.destroyBanner();


                intent = new Intent(root.getContext(), MyBestTeamActivity.class);
                intent.putExtra("teamName", "Simple");
                startActivity(intent);

                FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity());
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Simple Team");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Simple team Click");
                mFirebaseAnalytics.logEvent("Selected_simple_team_item", bundle);

//                if (MyApp.mInterstitialAd != null) {
//                    MyApp.mInterstitialAd.show(requireActivity());
//                    MyApp.mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
//                        @Override
//                        public void onAdDismissedFullScreenContent() {
//                            // Called when fullscreen content is dismissed.
//                            intent = new Intent(root.getContext(), MyBestTeam.class);
//                            intent.putExtra("teamName", "Simple");
//                            startActivity(intent);
//
//                            FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity());
//                            Bundle bundle = new Bundle();
//                            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Simple Team");
//                            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Simple team Click");
//                            mFirebaseAnalytics.logEvent("Selected_simple_team_item", bundle);
//                        }
//
//                        @Override
//                        public void onAdFailedToShowFullScreenContent(AdError adError) {
//                            // Called when fullscreen content failed to show.
//                            Log.d("TAG", "The ad failed to show.");
//                        }
//
//                        @Override
//                        public void onAdShowedFullScreenContent() {
//                            // Called when fullscreen content is shown.
//                            // Make sure to set your reference to null so you don't
//                            // show it a second time.
//                            MyApp.mInterstitialAd = null;
//                            Log.d("TAG", "The ad was shown.");
//                        }
//                    });
//                } else {
//                    MyApp.showInterstitialAd(requireActivity());
//                    intent = new Intent(root.getContext(), MyBestTeam.class);
//                    intent.putExtra("teamName", "Simple");
//                    startActivity(intent);
//
//                    FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity());
//                    Bundle bundle = new Bundle();
//                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Simple Team");
//                    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Simple team Click");
//                    mFirebaseAnalytics.logEvent("Selected_simple_team_item", bundle);
//                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
//
//                }

            });


        }
        return root;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showContestCode() {
        matchViewModel = new ViewModelProvider(requireActivity()).get(MatchViewModel.class);
        matchViewModel.getContestData().observe(requireActivity(), contestCodeModel -> {

            if (!contestCodeModel.getData().isEmpty()) {
                codesModelList.clear();
                codesModelList.addAll(contestCodeModel.getData());
                Collections.reverse(codesModelList);
                MyApp.showBannerAd(requireActivity(), codeView);
                CodesAdapter codesAdapter = new CodesAdapter(getContext());
                recyclerView.setAdapter(codesAdapter);
                codesAdapter.updateCodeModelList(codesModelList);
                lottieAnimationView.setVisibility(View.GONE);


            } else {
                lottieAnimationView.setAnimation(R.raw.empty);
                lottieAnimationView.playAnimation();
            }
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void addContestDialog(Context context) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.add_contest_layout);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(requireContext().getDrawable(R.drawable.back_bg));
        dialog.setCancelable(false);
        dialog.show();

        EditText userNameEDT, winningAmountEDT, entryFeesEDT, totalWinnerEDT, totalTeamEDt, contestCodeEDt;
        Button cancelBtn, addContestBtn;
        userNameEDT = dialog.findViewById(R.id.user_nameEDT);
        winningAmountEDT = dialog.findViewById(R.id.winning_amountEDT);
        entryFeesEDT = dialog.findViewById(R.id.entry_feesEDT);
        totalWinnerEDT = dialog.findViewById(R.id.total_winnerEDT);
        totalTeamEDt = dialog.findViewById(R.id.total_teamEDT);
        contestCodeEDt = dialog.findViewById(R.id.contest_codeEDT);
        cancelBtn = dialog.findViewById(R.id.cancel_contestBtn);
        addContestBtn = dialog.findViewById(R.id.add_contestBtn);
        profileImage = dialog.findViewById(R.id.profileImg);
        storagePermission = new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

        profileImage.setOnClickListener(v -> showImagePicDialog());


        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        addContestBtn.setOnClickListener(v -> {

            String userName, winningAmount, entryFees, totalWinner, totalTeam, contestCode;
            userName = userNameEDT.getText().toString().trim();
            winningAmount = winningAmountEDT.getText().toString().trim();
            entryFees = entryFeesEDT.getText().toString().trim();
            totalWinner = totalWinnerEDT.getText().toString().trim();
            totalTeam = totalTeamEDt.getText().toString().trim();
            contestCode = contestCodeEDt.getText().toString().trim();

            if (encodedImage == null) {
                Toast.makeText(requireActivity(), "Please select an image.", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(userName)) {
                userNameEDT.setError("Field are required");
            } else if (TextUtils.isEmpty(winningAmount)) {
                winningAmountEDT.setError("Field are required");
            } else if (TextUtils.isEmpty(entryFees)) {
                entryFeesEDT.setError("Field are required");
            } else if (TextUtils.isEmpty(totalWinner)) {
                totalWinnerEDT.setError("Field are required");
            } else if (TextUtils.isEmpty(totalTeam)) {
                totalTeamEDt.setError("Field are required");
            } else if (TextUtils.isEmpty(contestCode)) {
                contestCodeEDt.setError("Field are required");
            } else {
                uploadData(encodedImage, userName, winningAmount, entryFees, totalWinner, totalTeam, contestCode, context);
            }
        });


    }

    private Boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
    }

    // Requesting  gallery permission
    private void requestStoragePermission() {
        requestPermissions(storagePermission, STORAGE_REQUEST);
    }

    private void uploadData(String encodedImage, String userName, String winningAmount, String entryFees, String totalWinner, String totalTeam, String contestCode, Context context) {
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put("userName", userName);
        stringMap.put("winnerAmount", winningAmount);
        stringMap.put("entryFees", entryFees);
        stringMap.put("totalWinner", totalWinner);
        stringMap.put("totalTeam", totalTeam);
        stringMap.put("contestCode", contestCode);
        stringMap.put("img", encodedImage);
        loadingDialog.show();

        ApiInterface apiInterface = WebServices.getInterface();
        Call<AddMatchModel> call = apiInterface.getContestRes(stringMap, "https://softwaresreviewguides.com/dreamteam11/APIs/add_contest_data_api.php");
        call.enqueue(new Callback<AddMatchModel>() {
            @Override
            public void onResponse(@NonNull Call<AddMatchModel> call, @NonNull Response<AddMatchModel> response) {
                if (response.isSuccessful()) {
                    showContestCode();
                    assert response.body() != null;
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    assert response.body() != null;
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();

            }

            @Override
            public void onFailure(@NonNull Call<AddMatchModel> call, @NonNull Throwable t) {

            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }

    }

    @Override
    public void onResume() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
        super.onResume();
    }

    @Override
    public void onDestroy() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
        super.onDestroy();
    }

    private void showImagePicDialog() {
        String[] options = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Pick Image From");
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                if (!checkCameraPermission()) {
                    requestCameraPermission();
                } else {
                    pickFromGallery();
                }
            } else if (which == 1) {
                if (!checkStoragePermission()) {
                    requestStoragePermission();
                } else {
                    pickFromGallery();
                }
            }
        });
        builder.create().show();
    }

    private Boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    // Requesting camera permission
    private void requestCameraPermission() {
        requestPermissions(cameraPermission, CAMERA_REQUEST);
    }

    private void pickFromGallery() {
        CropImage.activity().start(requireActivity());
    }
}

