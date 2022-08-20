package com.itndev.EloSystem.Utils;

import com.itndev.EloSystem.Package.EloResult;
import com.itndev.EloSystem.Package.Pair;
import com.itndev.EloSystem.Storage.DatabaseIO;

import java.util.ArrayList;
import java.util.List;

public class EloUtils {

    public static Integer getEloFromUUID(String UUID) {
        return DatabaseIO.getElo(UUID);
    }

    public static List<EloResult> caculate(Pair pair) {
        List<EloResult> eloResults = new ArrayList<>();
        String UUID1 = pair.getUUID1();
        String UUID2 = pair.getUUID2();
        Integer Elo1 = getEloFromUUID(UUID1);
        Integer Elo2 = getEloFromUUID(UUID2);
        Boolean hasTeam1Won = pair.getHasTeam1Won();
        Integer Mode = pair.getMode();
        EloResult eloResult1;
        EloResult eloResult2;
        if(Elo1 > Elo2) {
            Integer difference = Elo1 - Elo2;
            Integer differenceFactor = 0;
            for(int x = 1; x <= 100; x++) {
                if(difference <= x*10) {
                    differenceFactor = x;
                    break;
                }
                if(x == 100) {
                    differenceFactor = 101;
                }
            }

            if(hasTeam1Won) {
                Integer Gain = 16 - differenceFactor;
                Integer Lose = 8 - (differenceFactor/(2));
                eloResult1 = new EloResult(UUID1, "idk", true, Elo1, Elo1 + Math.max(Gain, 4));
                eloResult2 = new EloResult(UUID1, "idk", false, Elo1, Math.max(Elo1 - Math.max(Lose, 2), 0));
            } else {
                Integer Gain = 16 + differenceFactor;
                Integer Lose = 8 + (differenceFactor/(2));
                eloResult1 = new EloResult(UUID1, "idk", false, Elo1, Math.max(Elo1 - Lose, 0));
                eloResult2 = new EloResult(UUID2, "idk", true, Elo2, Elo2 + Gain);
            }
        } else if(Elo1 < Elo2) {
            Integer difference = Elo2 - Elo1;
            Integer differenceFactor = 0;
            for(int x = 1; x <= 100; x++) {
                if(difference <= x*10) {
                    differenceFactor = x;
                    break;
                }
                if(x == 100) {
                    differenceFactor = 101;
                }
            }

            if(hasTeam1Won) {
                Integer Gain = 16 - differenceFactor;
                Integer Lose = 8 - (differenceFactor/(2));
                eloResult1 = new EloResult(UUID1, "idk", true, Elo1, Elo1 + Math.max(Gain, 4));
                eloResult2 = new EloResult(UUID1, "idk", false, Elo1, Math.max(Elo1 - Math.max(Lose, 2), 0));
            } else {
                Integer Gain = 16 + differenceFactor;
                Integer Lose = 8 + (differenceFactor/(2));
                eloResult1 = new EloResult(UUID1, "idk", false, Elo1, Math.max(Elo1 - Lose, 0));
                eloResult2 = new EloResult(UUID2, "idk", true, Elo2, Elo2 + Gain);
            }
        } else {
            Integer Gain = 16;
            Integer Lose = 8;
            if(hasTeam1Won) {
                eloResult1 = new EloResult(UUID1, "idk", true, Elo1, Elo1 + Gain);
                eloResult2 = new EloResult(UUID2, "idk", false, Elo2, Math.max(Elo2 - Lose, 0));
            } else {
                eloResult1 = new EloResult(UUID1, "idk", false, Elo1, Math.max(Elo1 - Lose, 0));
                eloResult2 = new EloResult(UUID2, "idk", true, Elo2, Elo2 + Gain);
            }
            //-8, +16
        }
        if(Mode == 1) {
            eloResults.add(eloResult1);
            eloResults.add(eloResult2);
        } else if(Mode == 2) {
            eloResults.add(eloResult2);
        } else {
            eloResults.add(eloResult1);
        }
        return eloResults;
    }
}
