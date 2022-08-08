package com.itndev.EloSystem.API;

import com.itndev.EloSystem.Package.EloResult;
import com.itndev.EloSystem.Package.Pair;
import com.itndev.EloSystem.Storage.DatabaseIO;
import com.itndev.EloSystem.Utils.EloUtils;

import java.util.ArrayList;
import java.util.List;

public class EloUpdater {

    public static List<EloResult> ProcessWarElo(List<String> Team1UUID, List<String> Team2UUID, Boolean hasTeam1Won, String WinnerFaction, String LoserFaction) {
        if(Team1UUID.isEmpty() || Team2UUID.isEmpty()) {
            return null;
        }
        List<EloResult> eloResults = new ArrayList<>();
        List<Pair> Pairs = new ArrayList<>();
        if(Team1UUID.size() == Team2UUID.size()) {
            int x = Team1UUID.size();
            List<String> T_Team1UUID = Team1UUID;
            List<String> T_Team2UUID = Team2UUID;
            for(int c = 1; c <= x; c++) {
                String UUID1 = T_Team1UUID.get(0);
                String UUID2 = T_Team2UUID.get(0);
                T_Team1UUID.remove(0);
                T_Team2UUID.remove(0);
                Pair pair = new Pair(UUID1, UUID2, hasTeam1Won, 1);
                Pairs.add(pair);
            }
        } else if(Team1UUID.size() > Team2UUID.size()) {
            int x = Team2UUID.size();
            List<String> T_Team1UUID = Team1UUID;
            List<String> T_Team2UUID = Team2UUID;
            String TemplateUUID = null;
            for(int c = 1; c <= x; c++) {
                if(c < x) {
                    String UUID1 = T_Team1UUID.get(0);
                    String UUID2 = T_Team2UUID.get(0);
                    T_Team1UUID.remove(0);
                    T_Team2UUID.remove(0);
                    Pair pair = new Pair(UUID1, UUID2, hasTeam1Won, 1);
                    Pairs.add(pair);
                } else {
                    if(TemplateUUID == null) {
                        String UUID1 = T_Team1UUID.get(0);
                        String UUID2 = T_Team2UUID.get(0);
                        T_Team1UUID.remove(0);
                        T_Team2UUID.remove(0);
                        TemplateUUID = UUID2;
                        Pair pair = new Pair(UUID1, UUID2, hasTeam1Won, 1);
                        Pairs.add(pair);
                    } else {
                        for(String UUID1 : T_Team1UUID) {
                            Pair pair = new Pair(UUID1, TemplateUUID, hasTeam1Won, 3);
                            Pairs.add(pair);
                        }
                    }
                }
            }
        } else {
            int x = Team1UUID.size();
            List<String> T_Team1UUID = Team1UUID;
            List<String> T_Team2UUID = Team2UUID;
            String TemplateUUID = null;
            for(int c = 1; c <= x; c++) {
                if(c < x) {
                    String UUID1 = T_Team1UUID.get(0);
                    String UUID2 = T_Team2UUID.get(0);
                    T_Team1UUID.remove(0);
                    T_Team2UUID.remove(0);
                    Pair pair = new Pair(UUID1, UUID2, hasTeam1Won, 1);
                    Pairs.add(pair);
                } else {
                    if(TemplateUUID == null) {
                        String UUID1 = T_Team1UUID.get(0);
                        String UUID2 = T_Team2UUID.get(0);
                        T_Team1UUID.remove(0);
                        T_Team2UUID.remove(0);
                        TemplateUUID = UUID1;
                        Pair pair = new Pair(UUID1, UUID2, hasTeam1Won, 1);
                        Pairs.add(pair);
                    } else {
                        for(String UUID2 : T_Team2UUID) {
                            Pair pair = new Pair(TemplateUUID, UUID2, hasTeam1Won, 2);
                            Pairs.add(pair);
                        }
                    }
                }
            }
        }
        for(Pair pair : Pairs) {
            eloResults.addAll(EloUtils.caculate(pair));
        }
        for(EloResult eloResult : eloResults) {
            if(eloResult.getHasWon()) {
                eloResult.InjectFactionUUID(WinnerFaction);
            } else {
                eloResult.InjectFactionUUID(LoserFaction);
            }
            DatabaseIO.setElo(eloResult.getUUID(), eloResult.getCurrentElo());
        }
        return eloResults;
    }
}
