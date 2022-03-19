package com.possible.team11app.activities.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.ads.AudienceNetworkAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.possible.team11app.R;
import com.possible.team11app.activities.MatchDetailActivity;
import com.possible.team11app.activities.NewsActivity;
import com.possible.team11app.adapters.CricketLiveScoreAdapter;
import com.possible.team11app.adapters.FootBallAdapter;
import com.possible.team11app.adapters.NewsAdapter;
import com.possible.team11app.databinding.FragmentMyHomeBinding;
import com.possible.team11app.models.FootballDataModels.FootBallData;
import com.possible.team11app.models.MatchDetailModels.Datum;
import com.possible.team11app.models.MatchDetailModels.MatchViewModel;
import com.possible.team11app.models.MatchNewsModels.NewsDatum;
import com.possible.team11app.utils.AdsViewModel;
import com.possible.team11app.utils.MyApp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    public static boolean bannerAds, intersAds;
    // Interstitial Add
    private final String TAG = PlaceholderFragment.class.getSimpleName();
    int index = 1;
    List<Datum> cricketLiveScoreModelList = new ArrayList<>();
    List<NewsDatum> newsModelList = new ArrayList<>();
    List<FootBallData> footBallModelList = new ArrayList<>();
    MatchViewModel matchViewModel;
    SwipeRefreshLayout swipeRefreshLayout;
    LottieAnimationView lottieAnimationView;
    RecyclerView recyclerView;
    RelativeLayout adView;
    private FragmentMyHomeBinding binding;

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  MobileAds.initialize(requireActivity());
        AudienceNetworkAds.initialize(requireActivity());
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentMyHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        recyclerView = binding.homeRecyclerView;
        lottieAnimationView = binding.lottieRV;
        swipeRefreshLayout = binding.swipeRefresh;
        adView = binding.adView;

        LinearLayoutManager layoutManager = new LinearLayoutManager(root.getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);


        matchViewModel = new ViewModelProvider(requireActivity()).get(MatchViewModel.class);
        if (index == 1) {
            MyApp.showBannerAd(requireActivity(),binding.adView);

            lottieAnimationView.setAnimation(R.raw.loding_dot);
            lottieAnimationView.playAnimation();
            swipeRefreshLayout.setOnRefreshListener(() -> {
                setMatchData(root.getContext());
                swipeRefreshLayout.setRefreshing(false);
                MyApp.showBannerAd(requireActivity(),binding.adView);
            });
            setMatchData(root.getContext());

        } else if (index == 2) {
            MyApp.showBannerAd(requireActivity(),binding.adView);

            lottieAnimationView.setAnimation(R.raw.loding_dot);
            lottieAnimationView.playAnimation();
            swipeRefreshLayout.setOnRefreshListener(() -> {
                setNewsData(root.getContext());
                MyApp.showBannerAd(requireActivity(),binding.adView);
                swipeRefreshLayout.setRefreshing(false);
            });
            setNewsData(root.getContext());

        } else if (index == 3) {
            lottieAnimationView.setAnimation(R.raw.loding_dot);
            lottieAnimationView.playAnimation();
            MyApp.showBannerAd(requireActivity(),binding.adView);

            swipeRefreshLayout.setOnRefreshListener(() -> {
                MyApp.showBannerAd(requireActivity(),binding.adView);
                setFootBallData(root.getContext());
                swipeRefreshLayout.setRefreshing(false);
            });
            setFootBallData(root.getContext());

        }

        return root;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setMatchData(Context context) {
        matchViewModel.getMatchData().observe(requireActivity(), matchDetailModel -> {
            List<Datum> matchDetailModelList = matchDetailModel.getData();
            Log.d("Added", String.valueOf(matchDetailModel.getData()));
            cricketLiveScoreModelList.clear();
            if (!matchDetailModelList.isEmpty()) {
                for (Datum datum : matchDetailModelList) {
                    String id = datum.getId();


                    String img1 = datum.getImage1();
                    String img2 = datum.getImage2();
                    String team1 = datum.getTeam1Name();
                    String team2 = datum.getTeam2Name();
                    String date = datum.getMatchDate();
                    String time = datum.getMatchTime();
                    String desc = datum.getMatchDesc();
                    cricketLiveScoreModelList.add(new Datum(id, img1, img2, team1, team2, date, desc, time));

                }
                CricketLiveScoreAdapter cricketLiveScoreAdapter = new CricketLiveScoreAdapter(cricketLiveScoreModelList, requireActivity(), bannerAds, (datum, position) -> {

//                    if (position % 2 == 0) {

                    // appOpenManager = new AppOpenManager(MyApp.mInstance, Paper.book().read(Prevalent.openAppAds), context);
                    AdsViewModel.destroyBanner();


                    Intent intent = new Intent(context, MatchDetailActivity.class);
                    intent.putExtra("matchPos", position);
                    intent.putExtra("id", datum.getId());
                    intent.putExtra("pos", position);
                    startActivity(intent);

                    FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity());
                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, datum.getTeam1Name() + " VS " + datum.getTeam2Name());
                    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "team Click");
                    mFirebaseAnalytics.logEvent("Selected_match_team_item", bundle);


//                            if (MyApp.mInterstitialAd != null) {
//                                MyApp.mInterstitialAd.show(requireActivity());
//                                MyApp.mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
//                                    @Override
//                                    public void onAdDismissedFullScreenContent() {
//                                        // Called when fullscreen content is dismissed.
//                                        Intent intent = new Intent(context, MatchDetails.class);
//                                        intent.putExtra("matchPos", position);
//                                        intent.putExtra("id", datum.getId());
//                                        intent.putExtra("pos", position);
//                                        startActivity(intent);
//
//                                        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity());
//                                        Bundle bundle = new Bundle();
//                                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, datum.getTeam1Name() + " VS " + datum.getTeam2Name());
//                                        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "team Click");
//                                        mFirebaseAnalytics.logEvent("Selected_match_team_item", bundle);
//                                    }
//
//                                    @Override
//                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
//                                        // Called when fullscreen content failed to show.
//                                        Log.d("TAG", "The ad failed to show.");
//                                    }
//
//                                    @Override
//                                    public void onAdShowedFullScreenContent() {
//                                        // Called when fullscreen content is shown.
//                                        // Make sure to set your reference to null so you don't
//                                        // show it a second time.
//                                        MyApp.mInterstitialAd = null;
//                                        Log.d("TAG", "The ad was shown.");
//                                    }
//                                });
//                            } else {
//                                MyApp.showInterstitialAd(requireActivity());
//                                Intent intent = new Intent(context, MatchDetails.class);
//                                intent.putExtra("matchPos", position);
//                                intent.putExtra("id", datum.getId());
//                                intent.putExtra("pos", position);
//                                startActivity(intent);
//
//                                FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity());
//                                Bundle bundle = new Bundle();
//                                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, datum.getTeam1Name() + " VS " + datum.getTeam2Name());
//                                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "team Click");
//                                mFirebaseAnalytics.logEvent("Selected_match_team_item", bundle);
//                                Log.d("TAG", "The interstitial ad wasn't ready yet.");
//
//                            }


//                    }else {
//                        Intent intent = new Intent(context, MatchDetails.class);
//                        intent.putExtra("matchPos", position);
//                        intent.putExtra("id", datum.getId());
//                        intent.putExtra("pos", position);
//                        startActivity(intent);
//
//                        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity());
//                        Bundle bundle = new Bundle();
//                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, datum.getTeam1Name() + " VS " + datum.getTeam2Name());
//                        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "team Click");
//                        mFirebaseAnalytics.logEvent("Selected_match_team_item", bundle);
//                    }
                });
                recyclerView.setAdapter(cricketLiveScoreAdapter);
                Collections.reverse(cricketLiveScoreModelList);
                cricketLiveScoreAdapter.notifyDataSetChanged();
                lottieAnimationView.setVisibility(View.GONE);


            } else {
                lottieAnimationView.setAnimation(R.raw.empty);
                lottieAnimationView.playAnimation();
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setNewsData(Context context) {
        matchViewModel.getMatchNewsData().observe(requireActivity(), matchNewsModel -> {
            List<NewsDatum> newsDatumList = matchNewsModel.getData();
            newsModelList.clear();
            if (!newsDatumList.isEmpty()) {
                for (NewsDatum news : newsDatumList) {
                    String id = news.getId();
                    String img = news.getNewsImg();
                    String title = news.getNewsTitle();
                    String desc = news.getNewsDesc();
                    newsModelList.add(new NewsDatum(id, img, title, desc));

                }
                NewsAdapter newsAdapter = new NewsAdapter(newsModelList, requireActivity(), bannerAds, (newsDatum, position) -> {
//                    if (position % 2 == 0) {
                    // appOpenManager = new AppOpenManager(MyApp.mInstance, Paper.book().read(Prevalent.openAppAds), context);
                    AdsViewModel.destroyBanner();

                    Intent intent = new Intent(context, NewsActivity.class);
                    intent.putExtra("newsPos", position);
                    intent.putExtra("img", newsDatum.getNewsImg());
                    intent.putExtra("desc", newsDatum.getNewsDesc());
                    intent.putExtra("title", newsDatum.getNewsTitle());
                    startActivity(intent);

                    FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity());
                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, newsDatum.getNewsTitle());
                    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "News Click");
                    mFirebaseAnalytics.logEvent("Selected_news_item", bundle);

//                    if (MyApp.mInterstitialAd != null) {
//                        MyApp.mInterstitialAd.show(requireActivity());
//                        MyApp.mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
//                            @Override
//                            public void onAdDismissedFullScreenContent() {
//                                // Called when fullscreen content is dismissed.
//                                Intent intent = new Intent(context, NewsActivity.class);
//                                intent.putExtra("newsPos", position);
//                                intent.putExtra("img", newsDatum.getNewsImg());
//                                intent.putExtra("desc", newsDatum.getNewsDesc());
//                                intent.putExtra("title", newsDatum.getNewsTitle());
//                                startActivity(intent);
//
//                                FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity());
//                                Bundle bundle = new Bundle();
//                                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, newsDatum.getNewsTitle());
//                                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "News Click");
//                                mFirebaseAnalytics.logEvent("Selected_news_item", bundle);
//
//                            }
//
//                            @Override
//                            public void onAdFailedToShowFullScreenContent(AdError adError) {
//                                // Called when fullscreen content failed to show.
//                                Log.d("TAG", "The ad failed to show.");
//                            }
//
//                            @Override
//                            public void onAdShowedFullScreenContent() {
//                                // Called when fullscreen content is shown.
//                                // Make sure to set your reference to null so you don't
//                                // show it a second time.
//                                MyApp.mInterstitialAd = null;
//                                Log.d("TAG", "The ad was shown.");
//                            }
//                        });
//                    } else {
//                        MyApp.showInterstitialAd(requireActivity());
//                        Intent intent = new Intent(context, NewsActivity.class);
//                        intent.putExtra("newsPos", position);
//                        intent.putExtra("img", newsDatum.getNewsImg());
//                        intent.putExtra("desc", newsDatum.getNewsDesc());
//                        intent.putExtra("title", newsDatum.getNewsTitle());
//                        startActivity(intent);
//
//                        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity());
//                        Bundle bundle = new Bundle();
//                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, newsDatum.getNewsTitle());
//                        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "News Click");
//                        mFirebaseAnalytics.logEvent("Selected_news_item", bundle);
//
//                        Log.d("TAG", "The interstitial ad wasn't ready yet.");
//
//                    }

//                    }else {
//                        Intent intent = new Intent(context, NewsActivity.class);
//                        intent.putExtra("newsPos", position);
//                        intent.putExtra("img", newsDatum.getNewsImg());
//                        intent.putExtra("desc", newsDatum.getNewsDesc());
//                        intent.putExtra("title", newsDatum.getNewsTitle());
//                        startActivity(intent);
//
//                        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity());
//                        Bundle bundle = new Bundle();
//                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, newsDatum.getNewsTitle());
//                        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "News Click");
//                        mFirebaseAnalytics.logEvent("Selected_news_item", bundle);
//                    }

                });

                recyclerView.setAdapter(newsAdapter);
                Collections.reverse(newsModelList);
                newsAdapter.notifyDataSetChanged();
                lottieAnimationView.setVisibility(View.GONE);

            } else {
                lottieAnimationView.setAnimation(R.raw.empty);
                lottieAnimationView.playAnimation();
            }
        });

    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFootBallData(Context context) {
        matchViewModel.getFootballData().observe(requireActivity(), footballDataModel -> {
            List<FootBallData> footBallData = footballDataModel.getData();
            footBallModelList.clear();

            if (!footBallData.isEmpty()) {
                for (FootBallData f : footBallData) {

                    String id = f.getId();
                    String img1 = f.getImage1();
                    String img2 = f.getImage2();
                    String team1 = f.getTeam1Name();
                    String team2 = f.getTeam2Name();
                    String date = f.getMatchDate();
                    String time = f.getMatchTime();
                    String desc = f.getMatchDesc();
                    footBallModelList.add(new FootBallData(id, img1, img2, team1, team2, date, desc, time));
                }
                FootBallAdapter footBallAdapter = new FootBallAdapter(footBallModelList, requireActivity(), bannerAds, (datum, position) -> {
                    AdsViewModel.destroyBanner();

//                    if (position % 2 == 0) {
                    // appOpenManager = new AppOpenManager(MyApp.mInstance, Paper.book().read(Prevalent.openAppAds), context);


                    Intent intent = new Intent(context, MatchDetailActivity.class);
                    intent.putExtra("id", datum.getId());
                    intent.putExtra("pos", position);
                    startActivity(intent);

                    FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity());
                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, datum.getTeam1Name() + " VS " + datum.getTeam2Name());
                    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Football Click");
                    mFirebaseAnalytics.logEvent("Selected_Football_item", bundle);

//                    if (MyApp.mInterstitialAd != null) {
//                        MyApp.mInterstitialAd.show(requireActivity());
//                        MyApp.mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
//                            @Override
//                            public void onAdDismissedFullScreenContent() {
//                                // Called when fullscreen content is dismissed.
//                                Intent intent = new Intent(context, MatchDetails.class);
//                                intent.putExtra("id", datum.getId());
//                                intent.putExtra("pos", position);
//                                startActivity(intent);
//
//                                FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity());
//                                Bundle bundle = new Bundle();
//                                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, datum.getTeam1Name() + " VS " + datum.getTeam2Name());
//                                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Football Click");
//                                mFirebaseAnalytics.logEvent("Selected_Football_item", bundle);
//                            }
//
//                            @Override
//                            public void onAdFailedToShowFullScreenContent(AdError adError) {
//                                // Called when fullscreen content failed to show.
//                                Log.d("TAG", "The ad failed to show.");
//                            }
//
//                            @Override
//                            public void onAdShowedFullScreenContent() {
//                                // Called when fullscreen content is shown.
//                                // Make sure to set your reference to null so you don't
//                                // show it a second time.
//                                MyApp.mInterstitialAd = null;
//                                Log.d("TAG", "The ad was shown.");
//                            }
//                        });
//                    } else {
//                        MyApp.showInterstitialAd(requireActivity());
//                        Intent intent = new Intent(context, MatchDetails.class);
//                        intent.putExtra("id", datum.getId());
//                        intent.putExtra("pos", position);
//                        startActivity(intent);
//
//                        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity());
//                        Bundle bundle = new Bundle();
//                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, datum.getTeam1Name() + " VS " + datum.getTeam2Name());
//                        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Football Click");
//                        mFirebaseAnalytics.logEvent("Selected_Football_item", bundle);
//
//                        Log.d("TAG", "The interstitial ad wasn't ready yet.");
//
//                    }

//                    } else {
//                        Intent intent = new Intent(context, MatchDetails.class);
//                        intent.putExtra("id", datum.getId());
//                        intent.putExtra("pos", position);
//                        startActivity(intent);
//
//                        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity());
//                        Bundle bundle = new Bundle();
//                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, datum.getTeam1Name() + " VS " + datum.getTeam2Name());
//                        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Football Click");
//                        mFirebaseAnalytics.logEvent("Selected_Football_item", bundle);
//
//                    }

                });
                recyclerView.setAdapter(footBallAdapter);
                Collections.reverse(footBallModelList);
                footBallAdapter.notifyDataSetChanged();
                lottieAnimationView.setVisibility(View.GONE);

            } else {
                lottieAnimationView.setAnimation(R.raw.empty);
                lottieAnimationView.playAnimation();
            }
        });


    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        binding = null;
    }


}