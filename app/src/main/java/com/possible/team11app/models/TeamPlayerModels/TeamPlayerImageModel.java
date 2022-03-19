package com.possible.team11app.models.TeamPlayerModels;

import java.util.List;

public class TeamPlayerImageModel {

    private List<TeamPlayerModel> data = null;
    private String success;

    public List<TeamPlayerModel> getData() {
        return data;
    }

    public void setData(List<TeamPlayerModel> data) {
        this.data = data;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

}