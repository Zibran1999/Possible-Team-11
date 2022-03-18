package com.possible.team11app.models.MatchDetailModels;


import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.possible.team11app.models.AdsStatusModels.AdsStatusModel;
import com.possible.team11app.models.ContestCodeModels.ContestCodeModel;
import com.possible.team11app.models.FootballDataModels.FootballDataModel;
import com.possible.team11app.models.MatchNewsModels.MatchNewsModel;
import com.possible.team11app.models.TeamPlayerModels.TeamPlayerImageModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchDetailRepository {

    private static ApiInterface apiInterface;
    private static MatchDetailRepository matchDetailRepository;
    private final MutableLiveData<MatchDetailModel> matchDetailModelMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<MatchNewsModel> matchNewsModelLiveData = new MutableLiveData<>();
    private final MutableLiveData<FootballDataModel> footballDataLiveData = new MutableLiveData<>();
    private final MutableLiveData<ContestCodeModel> codeModelMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<MatchPreviewModel> matchPreviewModelMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<TeamPlayerImageModel> teamPlayerImageModelMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<TeamPlayerImageModel> grandTeamPlayerImageModelMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<TeamPlayerImageModel> simpleTeamPlayerImageModelMutableLiveData = new MutableLiveData<>();

    private final MutableLiveData<AdsStatusModel> adsStatusModelMutableLiveData = new MutableLiveData<>();



    public MatchDetailRepository() {
        apiInterface = WebServices.getInterface();
    }

    public static MatchDetailRepository getInstance() {
        if (matchDetailRepository == null) {
            matchDetailRepository = new MatchDetailRepository();
        }
        return matchDetailRepository;
    }

    public MutableLiveData<MatchDetailModel> getMatchDetailModelMutableLiveData() {
        Call<MatchDetailModel> call = apiInterface.getMatchDetailData();
        call.enqueue(new Callback<MatchDetailModel>() {
            @Override
            public void onResponse(@NonNull Call<MatchDetailModel> call, @NonNull Response<MatchDetailModel> response) {

                if (response.isSuccessful()) {
                    matchDetailModelMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MatchDetailModel> call, @NonNull Throwable t) {

            }
        });
        return matchDetailModelMutableLiveData;

    }

    public MutableLiveData<MatchNewsModel> getMatchNewsModelLiveData() {

        Call<MatchNewsModel> newsModelCall = apiInterface.getMatchNewsData();
        newsModelCall.enqueue(new Callback<MatchNewsModel>() {
            @Override
            public void onResponse(@NonNull Call<MatchNewsModel> call, @NonNull Response<MatchNewsModel> response) {
                if (response.isSuccessful()) {
                    matchNewsModelLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MatchNewsModel> call, @NonNull Throwable t) {

            }
        });
        return matchNewsModelLiveData;
    }

    public MutableLiveData<FootballDataModel> getFootballDataLiveData() {
        Call<FootballDataModel> call = apiInterface.getFootBallData();
        call.enqueue(new Callback<FootballDataModel>() {
            @Override
            public void onResponse(@NonNull Call<FootballDataModel> call, @NonNull Response<FootballDataModel> response) {
                if (response.isSuccessful()) {
                    footballDataLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<FootballDataModel> call, @NonNull Throwable t) {

            }
        });
        return footballDataLiveData;
    }

    public MutableLiveData<ContestCodeModel> getCodeModelMutableLiveData() {
        Call<ContestCodeModel> call = apiInterface.getContestData();
        call.enqueue(new Callback<ContestCodeModel>() {
            @Override
            public void onResponse(@NonNull Call<ContestCodeModel> call, @NonNull Response<ContestCodeModel> response) {

                if (response.isSuccessful()) {
                    codeModelMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ContestCodeModel> call, @NonNull Throwable t) {

            }
        });
        return codeModelMutableLiveData;
    }

    public MutableLiveData<MatchPreviewModel> getMatchPreviewModelMutableLiveData(String id) {
        Call<MatchPreviewModel> matchPreviewModelCall = apiInterface.getMatchPreview(id);
        matchPreviewModelCall.enqueue(new Callback<MatchPreviewModel>() {
            @Override
            public void onResponse(@NonNull Call<MatchPreviewModel> call, @NonNull Response<MatchPreviewModel> response) {
                if (response.isSuccessful()) {
                    matchPreviewModelMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MatchPreviewModel> call, @NonNull Throwable t) {

            }
        });
        return matchPreviewModelMutableLiveData;
    }

    public MutableLiveData<TeamPlayerImageModel> getTeamPlayerImageModelMutableLiveData(String id) {
        Call<TeamPlayerImageModel> call = apiInterface.getTeamPlayerImage(id);
        call.enqueue(new Callback<TeamPlayerImageModel>() {
            @Override
            public void onResponse(@NonNull Call<TeamPlayerImageModel> call, @NonNull Response<TeamPlayerImageModel> response) {
                if (response.isSuccessful()) {
                    teamPlayerImageModelMutableLiveData.setValue(response.body());

                }
            }

            @Override
            public void onFailure(@NonNull Call<TeamPlayerImageModel> call, @NonNull Throwable t) {

            }
        });
        return teamPlayerImageModelMutableLiveData;
    }

    public MutableLiveData<TeamPlayerImageModel> getGrandTeamPlayerImageModelMutableLiveData(String id) {
        Call<TeamPlayerImageModel> call = apiInterface.getGrandTeamPlayerImage(id);
        call.enqueue(new Callback<TeamPlayerImageModel>() {
            @Override
            public void onResponse(@NonNull Call<TeamPlayerImageModel> call, @NonNull Response<TeamPlayerImageModel> response) {
                if (response.isSuccessful()) {
                    grandTeamPlayerImageModelMutableLiveData.setValue(response.body());

                }
            }

            @Override
            public void onFailure(@NonNull Call<TeamPlayerImageModel> call, @NonNull Throwable t) {

            }
        });
        return grandTeamPlayerImageModelMutableLiveData;
    }
    public MutableLiveData<TeamPlayerImageModel> getSimpleTeamPlayerImageModelMutableLiveData(String id) {
        Call<TeamPlayerImageModel> call = apiInterface.getSimpleTeamPlayerImage(id);
        call.enqueue(new Callback<TeamPlayerImageModel>() {
            @Override
            public void onResponse(@NonNull Call<TeamPlayerImageModel> call, @NonNull Response<TeamPlayerImageModel> response) {
                if (response.isSuccessful()) {
                    simpleTeamPlayerImageModelMutableLiveData.setValue(response.body());

                }
            }

            @Override
            public void onFailure(@NonNull Call<TeamPlayerImageModel> call, @NonNull Throwable t) {

            }
        });
        return simpleTeamPlayerImageModelMutableLiveData;
    }

    public MutableLiveData<AdsStatusModel> getAdsStatusModelMutableLiveData() {
        Call<AdsStatusModel> call = apiInterface.getAdsStatus();
        call.enqueue(new Callback<AdsStatusModel>() {
            @Override
            public void onResponse(@NonNull Call<AdsStatusModel> call, @NonNull Response<AdsStatusModel> response) {
                if (response.isSuccessful()) {
                    adsStatusModelMutableLiveData.setValue(response.body());

                }
            }

            @Override
            public void onFailure(@NonNull Call<AdsStatusModel> call, @NonNull Throwable t) {

            }
        });
        return adsStatusModelMutableLiveData;
    }
}
