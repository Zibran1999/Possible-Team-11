package com.possible_team_11.Models.MatchDetailModels;

public class MessageModel {
    String message,error;

    public MessageModel(String message, String error) {
        this.message = message;
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }
}
