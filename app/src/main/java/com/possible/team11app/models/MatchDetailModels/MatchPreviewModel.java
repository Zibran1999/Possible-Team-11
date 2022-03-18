package com.possible.team11app.models.MatchDetailModels;

import java.util.List;

public class MatchPreviewModel {

    private List<MatchPreview> data = null;
    private String success;

    public List<MatchPreview> getData() {
        return data;
    }

    public void setData(List<MatchPreview> data) {
        this.data = data;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

}

