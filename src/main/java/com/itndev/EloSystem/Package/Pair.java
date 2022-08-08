package com.itndev.EloSystem.Package;

public class Pair {

    private String UUID1;
    private String UUID2;

    private Boolean hasTeam1Won; //if true UUID1's team won | if false UUID2's team won

    private Integer Mode; //if 1, normal Pair -> update both's elo based on caculation | if 2, unfair Pair -> UUID1 is just a template for UUID2's elo update as pair, only update UUID2's elo based on caculation | if 3, unfair Pair -> UUID2 is just a template for UUID1's elo update as pair, only update UUID1's elo based on caculation

    public Pair(String PlayerUUID1, String PlayerUUID2, Boolean hasTeam1Won_, Integer Mode_) {
        UUID1 = PlayerUUID1;
        UUID2 = PlayerUUID2;
        hasTeam1Won = hasTeam1Won_;
        Mode = Mode_;
    }

    public String getUUID1() {
        return UUID1;
    }

    public String getUUID2() {
        return UUID1;
    }

    public Boolean getHasTeam1Won() {
        return hasTeam1Won;
    }

    public Integer getMode() {
        return Mode;
    }
}
