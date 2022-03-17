package com.possible_team_11.Models.MatchDetailModels;

import com.google.gson.annotations.SerializedName;

public class MatchDatum {

    private final String id;
    private final String image1;
    private final String image2;
    @SerializedName("team_1_name")
    private final String team1Name;
    @SerializedName("team_2_name")
    private final String team2Name;
    @SerializedName("match_date")
    private final String matchDate;
    @SerializedName("match_desc")
    private final String matchDesc;
    @SerializedName("match_time")
    private final String matchTime;

    public MatchDatum(String id, String image1, String image2, String team1Name, String team2Name, String matchDate, String matchDesc, String matchTime) {
        this.id = id;
        this.image1 = image1;
        this.image2 = image2;
        this.team1Name = team1Name;
        this.team2Name = team2Name;
        this.matchDate = matchDate;
        this.matchDesc = matchDesc;
        this.matchTime = matchTime;
    }

    public String getId() {
        return id;
    }

    public String getImage1() {
        return image1;
    }

    public String getImage2() {
        return image2;
    }

    public String getTeam1Name() {
        return team1Name;
    }

    public String getTeam2Name() {
        return team2Name;
    }

    public String getMatchDate() {
        return matchDate;
    }

    public String getMatchDesc() {
        return matchDesc;
    }

    public String getMatchTime() {
        return matchTime;
    }
}
