package com.possible_team_11.Models.MatchDetailModels;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WebServices {
//    https://gedgetsworld.in/teamdream11.com/
    private static final String BASE_URL = "https://softwaresreviewguides.com/dreamteam11/APIs/";
    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static ApiInterface getInterface() {
        return retrofit.create(ApiInterface.class);
    }


}
