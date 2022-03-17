package com.possible_team_11.Models.MatchDetailModels;


import com.possible_team_11.Models.AdsModelList;
import com.possible_team_11.Models.FootballDataModels.FootballDataModel;
import com.possible_team_11.Models.MatchNewsModels.MatchNewsModel;
import com.possible_team_11.TeamPlayerModels.TeamPlayerImageModel;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("fetch_match_data_api.php")
    Call<MatchDetailModel> getMatchDetailData();

    @GET("fetch_news_data_api.php")
    Call<MatchNewsModel> getMatchNewsData();

    @GET("fetch_football_match_data_api.php")
    Call<FootballDataModel> getFootBallData();

    @GET("fetch_preview_data_api.php")
    Call<MatchPreviewModel> getMatchPreview(@Query("id") String id);

    @GET("fetch_team_player_images_api.php")
    Call<TeamPlayerImageModel> getTeamPlayerImage(@Query("id") String id);

    @GET("fetch_grand_team_player_images_api.php")
    Call<TeamPlayerImageModel> getGrandTeamPlayerImage(@Query("id") String id);

    @FormUrlEncoded
    @POST("ads_id_fetch.php")
    Call<AdsModelList> fetchAds(@Field("id") String id);

    @FormUrlEncoded
    @POST("ads_id_update.php")
    Call<MessageModel> updateAdIds(@FieldMap Map<String, String> map);


}
