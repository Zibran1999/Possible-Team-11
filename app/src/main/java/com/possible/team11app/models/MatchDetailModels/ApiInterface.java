package com.possible.team11app.models.MatchDetailModels;
import com.possible.team11app.models.AdsModelList;
import com.possible.team11app.models.AdsStatusModels.AdsStatusModel;
import com.possible.team11app.models.ContestCodeModels.ContestCodeModel;
import com.possible.team11app.models.FootballDataModels.FootballDataModel;
import com.possible.team11app.models.MatchNewsModels.MatchNewsModel;
import com.possible.team11app.models.TeamPlayerModels.TeamPlayerImageModel;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiInterface {
    @GET("fetch_match_data_api.php")
    Call<MatchDetailModel> getMatchDetailData();

    @GET("fetch_news_data_api.php")
    Call<MatchNewsModel> getMatchNewsData();

    @GET("fetch_football_match_data_api.php")
    Call<FootballDataModel> getFootBallData();

    @GET("fetch_contest_code_data_api.php")
    Call<ContestCodeModel> getContestData();

    @GET("fetch_preview_data_api.php")
    Call<MatchPreviewModel> getMatchPreview(@Query("id") String id);

    @GET("fetch_team_player_images_api.php")
    Call<TeamPlayerImageModel> getTeamPlayerImage(@Query("id") String id);

    @GET("fetch_grand_team_player_images_api.php")
    Call<TeamPlayerImageModel> getGrandTeamPlayerImage(@Query("id") String id);

    @GET("fetch_simple_team_player_images.php")
    Call<TeamPlayerImageModel> getSimpleTeamPlayerImage(@Query("id") String id);

    @FormUrlEncoded
    @POST()
    Call<AddMatchModel> getContestRes(@FieldMap Map<String, String> map, @Url String url);

    @GET("fetch_ads_status_api.php")
    Call<AdsStatusModel> getAdsStatus();
    @FormUrlEncoded
    @POST("ads_id_fetch.php")
    Call<AdsModelList> fetchAds(@Field("id") String id);
}
