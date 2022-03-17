package com.possible_team_11.Models;


public class Datum {

    private final String id;
    private final String image1;
    private final String image2;
    private final String team1Name;
    private final String team2Name;
    private final String matchDate;
    private final String matchDesc;

    public Datum(String id, String image1, String image2, String team1Name, String team2Name, String matchDate, String matchDesc) {
        this.id = id;
        this.image1 = image1;
        this.image2 = image2;
        this.team1Name = team1Name;
        this.team2Name = team2Name;
        this.matchDate = matchDate;
        this.matchDesc = matchDesc;
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
}
