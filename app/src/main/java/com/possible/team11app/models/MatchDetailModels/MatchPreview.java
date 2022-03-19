package com.possible.team11app.models.MatchDetailModels;

import com.google.gson.annotations.SerializedName;

public class MatchPreview {

    private String id;
    @SerializedName("TeamId")
    private String cricketTeamId;
    @SerializedName("TeamDesc")
    private String cricketTeamDesc;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCricketTeamId() {
        return cricketTeamId;
    }

    public void setCricketTeamId(String cricketTeamId) {
        this.cricketTeamId = cricketTeamId;
    }

    public String getCricketTeamDesc() {
        return cricketTeamDesc;
    }

    public void setCricketTeamDesc(String cricketTeamDesc) {
        this.cricketTeamDesc = cricketTeamDesc;
    }

}
