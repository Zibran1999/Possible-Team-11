package com.possible.team11app.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.material.card.MaterialCardView;
import com.possible.team11app.R;
import com.possible.team11app.databinding.AdLayoutBinding;
import com.possible.team11app.models.MatchDetailModels.Datum;
import com.possible.team11app.utils.Prevalent;

import java.util.List;
import java.util.Objects;

import io.paperdb.Paper;

public class CricketLiveScoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Datum> cricketLiveScoreModelList;
    Boolean bannerAds;
    CricketRvAdapter listener;

    Activity context;
    private static final int ITEM_VIEW = 0;
    private static final int AD_VIEW = 1;
    private static final int ITEM_FEED_COUNT = 4;
    AdLoader adLoader;

    public CricketLiveScoreAdapter(List<Datum> cricketLiveScoreModelList, Activity context, Boolean bannerAds, CricketRvAdapter listener) {
        this.cricketLiveScoreModelList = cricketLiveScoreModelList;
        this.context = context;
        this.bannerAds = bannerAds;
        this.listener = listener;
    }

    public int getItemViewType(int position) {
        if ((position + 1) % ITEM_FEED_COUNT == 0) {
            return AD_VIEW;
        }
        return ITEM_VIEW;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ITEM_VIEW) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cricket_live_score_layout, parent, false);
            return new CricketLiveScoreViewHolder(view);

        } else if (viewType == AD_VIEW) {
            View view = LayoutInflater.from(context).inflate(R.layout.ad_layout, parent, false);
            return new AdViewHolder(view);
        } else return null;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {



        if (holder.getItemViewType() == ITEM_VIEW) {
            int pos = position - Math.round(position / ITEM_FEED_COUNT);

            Glide.with(context).load("https://softwaresreviewguides.com/dreamteam11/APIs/Cricket_Team_Images/" + cricketLiveScoreModelList.get(pos).getImage1()).into(((CricketLiveScoreViewHolder)holder).teamIcon1);
            Glide.with(context).load("https://softwaresreviewguides.com/dreamteam11/APIs/Cricket_Team_Images/" + cricketLiveScoreModelList.get(pos).getImage2()).into(((CricketLiveScoreViewHolder)holder).teamIcon2);
            ((CricketLiveScoreViewHolder)holder).teamName.setText(cricketLiveScoreModelList.get(pos).getTeam1Name() + "  Vs  " + cricketLiveScoreModelList.get(pos).getTeam2Name());
            ((CricketLiveScoreViewHolder)holder).date.setText(cricketLiveScoreModelList.get(pos).getMatchDate());
            ((CricketLiveScoreViewHolder)holder).time.setText(cricketLiveScoreModelList.get(pos).getMatchTime());
            ((CricketLiveScoreViewHolder)holder).desc.setText(cricketLiveScoreModelList.get(pos).getMatchDesc());
            ((CricketLiveScoreViewHolder)holder).itemView.setOnClickListener(v -> listener.onItemClicked(cricketLiveScoreModelList.get(pos), pos));


        } else if (holder.getItemViewType() == AD_VIEW) {
            ((AdViewHolder) holder).bindAdData();
        }
    }


    @Override
    public int getItemCount() {
        if (cricketLiveScoreModelList.size() > 0) {
            return cricketLiveScoreModelList.size() + Math.round(cricketLiveScoreModelList.size() / ITEM_FEED_COUNT);
        }
        return 0;
    }


    public static class CricketLiveScoreViewHolder extends RecyclerView.ViewHolder {

        ImageView teamIcon1, teamIcon2;
        TextView teamName, date, time, desc;
        MaterialCardView cardView;


        public CricketLiveScoreViewHolder(@NonNull View itemView) {
            super(itemView);
            teamIcon1 = itemView.findViewById(R.id.team1);
            teamIcon2 = itemView.findViewById(R.id.team2);
            teamName = itemView.findViewById(R.id.teamName);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.cricket_time);
            desc = itemView.findViewById(R.id.description);
            cardView = itemView.findViewById(R.id.container);


        }


    }

    public  class AdViewHolder extends RecyclerView.ViewHolder {
        AdLayoutBinding binding;

        public AdViewHolder(@NonNull View itemAdView2) {
            super(itemAdView2);
            binding = AdLayoutBinding.bind(itemAdView2);


        }

        private void bindAdData() {
            Log.d("admobAdNative", Paper.book().read(Prevalent.nativeAds));

            AdLoader.Builder builder = new AdLoader.Builder(context, Paper.book().read(Prevalent.nativeAds))
                    .forNativeAd(nativeAd -> {
                        NativeAdView nativeAdView = (NativeAdView) context.getLayoutInflater().inflate(R.layout.native_ad_layout, null);
                        populateNativeADView(nativeAd, nativeAdView);
                        binding.adLayout.removeAllViews();
                        binding.adLayout.setElevation(5);
                        binding.adLayout.setPadding(5,5,5,5);
                        binding.adLayout.addView(nativeAdView);
                    });

            adLoader= builder.withAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    adLoader.loadAd(new AdRequest.Builder().build());
                }
            }).build();

//            adLoader.loadAd(new AdRequest.Builder().build());

        }

        private void populateNativeADView(NativeAd nativeAd, NativeAdView adView) {
            // Set the media view.
//            adView.setMediaView(adView.findViewById(R.id.ad_media));

            // Set other ad assets.
            adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
            adView.setBodyView(adView.findViewById(R.id.ad_body));
            adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
            adView.setIconView(adView.findViewById(R.id.ad_app_icon));
            adView.setPriceView(adView.findViewById(R.id.ad_price));
            adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
            adView.setStoreView(adView.findViewById(R.id.ad_store));
            adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

            // The headline and mediaContent are guaranteed to be in every UnifiedNativeAd.
            ((TextView) Objects.requireNonNull(adView.getHeadlineView())).setText(nativeAd.getHeadline());
//            adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

            // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
            // check before trying to display them.
            if (nativeAd.getBody() == null) {
                Objects.requireNonNull(adView.getBodyView()).setVisibility(View.INVISIBLE);
            } else {
                Objects.requireNonNull(adView.getBodyView()).setVisibility(View.VISIBLE);
                ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
            }

            if (nativeAd.getCallToAction() == null) {
                Objects.requireNonNull(adView.getCallToActionView()).setVisibility(View.INVISIBLE);
            } else {
                Objects.requireNonNull(adView.getCallToActionView()).setVisibility(View.VISIBLE);
                ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
            }

            if (nativeAd.getIcon() == null) {
                Objects.requireNonNull(adView.getIconView()).setVisibility(View.GONE);
            } else {
                ((ImageView) Objects.requireNonNull(adView.getIconView())).setImageDrawable(
                        nativeAd.getIcon().getDrawable());
                adView.getIconView().setVisibility(View.VISIBLE);
            }

            if (nativeAd.getPrice() == null) {
                Objects.requireNonNull(adView.getPriceView()).setVisibility(View.INVISIBLE);
            } else {
                Objects.requireNonNull(adView.getPriceView()).setVisibility(View.VISIBLE);
                ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
            }

            if (nativeAd.getStore() == null) {
                Objects.requireNonNull(adView.getStoreView()).setVisibility(View.INVISIBLE);
            } else {
                Objects.requireNonNull(adView.getStoreView()).setVisibility(View.VISIBLE);
                ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
            }

            if (nativeAd.getStarRating() == null) {
                Objects.requireNonNull(adView.getStarRatingView()).setVisibility(View.INVISIBLE);
            } else {
                ((RatingBar) Objects.requireNonNull(adView.getStarRatingView())).setRating(nativeAd.getStarRating().floatValue());
                adView.getStarRatingView().setVisibility(View.VISIBLE);
            }

            if (nativeAd.getAdvertiser() == null) {
                Objects.requireNonNull(adView.getAdvertiserView()).setVisibility(View.INVISIBLE);
            } else {
                ((TextView) Objects.requireNonNull(adView.getAdvertiserView())).setText(nativeAd.getAdvertiser());
                adView.getAdvertiserView().setVisibility(View.VISIBLE);
            }

            // This method tells the Google Mobile Ads SDK that you have finished populating your
            // native ad view with this native ad.
            adView.setNativeAd(nativeAd);
        }
    }

}