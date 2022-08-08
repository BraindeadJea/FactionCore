package com.itndev.EloSystem.Package;

public class EloResult {

    private String ThisUUID;
    private String ThisFactionUUID;
    private Boolean ThishasWon;
    private Integer ThisBeforeElo;
    private Integer ThisCurrentElo;

    public EloResult(String UUID, String FactionUUID, Boolean hasWon, Integer BeforeElo, Integer CurrentElo) {
        ThisUUID = UUID;
        ThisFactionUUID = FactionUUID;
        ThishasWon = hasWon;
        ThisBeforeElo = BeforeElo;
        ThisCurrentElo = CurrentElo;
    }

    public void InjectFactionUUID(String FactionUUID) {
        ThisFactionUUID = FactionUUID;
    }

    public String getUUID() {
        return ThisUUID;
    }

    public String getFactionUUID() {
        return ThisFactionUUID;
    }

    public Boolean getHasWon() {
        return ThishasWon;
    }

    public Integer getBeforeElo() {
        return ThisBeforeElo;
    }

    public Integer getCurrentElo() {
        return ThisCurrentElo;
    }
}
