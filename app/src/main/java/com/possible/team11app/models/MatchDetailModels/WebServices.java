package com.possible.team11app.models.MatchDetailModels;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WebServices {
//    https://gedgetsworld.in/teamdream11.com/
    private static final String BASE_URL = "https://minutenewsflash.com/admin/posible_team/APIs/";
    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static ApiInterface getInterface() {
        return retrofit.create(ApiInterface.class);
    }


}
