package com.possible.team11app.models.ContestCodeModels;

import com.google.gson.annotations.SerializedName;

public class
CodesModel {
    @SerializedName("username")
    String userName;
    @SerializedName("winningAmount")
    String winnerAmount;
    String id, entryFees, totalWinner, totalTeam, contestCode, images;

    public CodesModel(String userName, String winnerAmount, String id, String entryFees, String totalWinner, String totalTeam, String contestCode, String images) {
        this.userName = userName;
        this.winnerAmount = winnerAmount;
        this.id = id;
        this.entryFees = entryFees;
        this.totalWinner = totalWinner;
        this.totalTeam = totalTeam;
        this.contestCode = contestCode;
        this.images = images;
    }

    public String getUserName() {
        return userName;
    }

    public String getWinnerAmount() {
        return winnerAmount;
    }

    public String getId() {
        return id;
    }

    public String getEntryFees() {
        return entryFees;
    }

    public String getTotalWinner() {
        return totalWinner;
    }

    public String getTotalTeam() {
        return totalTeam;
    }

    public String getContestCode() {
        return contestCode;
    }

    public String getImages() {
        return images;
    }
}
