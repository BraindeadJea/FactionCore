package com.itndev.FactionCore.Transaction.TransactionUtils;

import com.itndev.FactionCore.Database.MySQL.SQL;
import com.itndev.FactionCore.Factions.Config;
import com.itndev.FactionCore.Transaction.TransactionUtils.FactionCD.CreateFactionUtils;
import com.itndev.FactionCore.Utils.Factions.FactionUtils;
import com.itndev.FactionCore.Utils.Factions.SystemUtils;
import com.itndev.FactionCore.Utils.Factions.ValidChecker;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CreateFaction {
    public static void CreateFaction(String UUID, String[] args) {
        new Thread( () -> {
            try {
                if(args.length < 2) {
                    SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f명령어 사용법 : &f/국가 생성 &7(국가이름)");
                    return;
                }
                CompletableFuture<Boolean> FutureValidCheck = ValidChecker.ValidCheck(args[1]);
                if (FactionUtils.getPlayerRank(UUID).equalsIgnoreCase("nomad")) {
                    if (FutureValidCheck.get(40, TimeUnit.MILLISECONDS)) {
                        if (args[1].length() <= 12) {
                            String newFactionUUID = FactionUtils.newFactionUUID();
                            if (SQL.getDatabase().TryClaimName(args[1], newFactionUUID).get()) {//try creating a faction name) {
                                //Main.econ.withdrawPlayer(op, Config.FactionCreateBalance);
                                CreateFactionUtils.CreateFaction(UUID, newFactionUUID, args[1]);
                            } else {
                                SystemUtils.SendMoney(UUID, Config.FactionCreateBalance);
                                SystemUtils.UUID_BASED_MSG_SENDER(UUID,  SystemUtils.getPrefix() + "&r&f해당 국가가 이미 존재합니다");
                            }
                            newFactionUUID = null;
                        } else {
                            SystemUtils.SendMoney(UUID, Config.FactionCreateBalance);
                            SystemUtils.UUID_BASED_MSG_SENDER(UUID, SystemUtils.getPrefix() + "&r&f국가 이름의 길이는 12자를 초과할수 없습니다");
                        }
                    } else {
                        SystemUtils.SendMoney(UUID, Config.FactionCreateBalance);
                        SystemUtils.UUID_BASED_MSG_SENDER(UUID, SystemUtils.getPrefix() + "&r&f해당 문자/단어는 국가 이름에 들어갈수 없습니다");
                    }
                } else {
                    SystemUtils.SendMoney(UUID, Config.FactionCreateBalance);
                    SystemUtils.UUID_BASED_MSG_SENDER(UUID, SystemUtils.getPrefix() + "&r&f당신은 이미 다른 국가에 소속되어 있습니다. 기존 국가를 나간후에 다시 시도해 주십시오");
                }
                FutureValidCheck = null;

            } catch (ExecutionException | InterruptedException | TimeoutException e) {
                SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&c&lERROR &7오류 발생 : 오류코드 DB-D02 (시스템시간:" + SystemUtils.getDate(System.currentTimeMillis()) + ")");
                e.printStackTrace();
            }
        }).start();

    }
}
