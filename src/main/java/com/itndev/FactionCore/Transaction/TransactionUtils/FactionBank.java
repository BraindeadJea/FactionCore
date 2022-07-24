package com.itndev.FactionCore.Transaction.TransactionUtils;

import com.itndev.FactionCore.Database.MySQL.SQL;
import com.itndev.FactionCore.Factions.Config;
import com.itndev.FactionCore.Utils.Factions.FactionUtils;
import com.itndev.FactionCore.Utils.Factions.SystemUtils;
import com.itndev.FactionCore.Utils.Factions.ValidChecker;

import java.text.DecimalFormat;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class FactionBank {
    private static DecimalFormat df = new DecimalFormat("0.00");

    @Deprecated
    public static void FactionBank(String UUID, String[] args, String balance) {
        new Thread( () -> {
            try {
                if (FactionUtils.isInFaction(UUID)) {
                    if(FactionUtils.isInWar(FactionUtils.getPlayerFactionUUID(UUID))) {
                        SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f전쟁 도중에는 국가 금고를 사용할수 없습니다");
                        return;
                    }
                    Boolean Take = null;
                    if (args[1].equalsIgnoreCase("입금")) {
                        Take = false;
                    } else if (args[1].equalsIgnoreCase("출금")) {
                        Take = true;
                    } else {
                        SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f명령어 사용법 : &f/국가 금고 &7(입금/출금) &7(금액)\n");

                        return;
                    }
                    if (args[2].length() > 20) {
                        SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&c오류 ! &7명령어가 너무 깁니다... 확인 불가\n");
                        return;
                    }
                    if (!ValidChecker.instanceofNumber(args[2])) {
                        SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f명령어 사용법 : &f/국가 금고 &7(입금/출금) &7(금액)\n" +
                                "&7> &f금액에는 숫자를 입력해야 합니다.");
                        return;
                    }
                    Double amount = Double.parseDouble(args[2]);

                    if (!FactionUtils.HigherThenorSameRank(UUID, Config.VipMember)) {
                        SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f권한이 없습니다. &r&c" + Config.VipMember_Lang + " &r&f등급 이상부터 사용이 가능합니다");
                        if(!Take) {
                            SystemUtils.SendMoney(UUID, amount);
                        }
                        return;
                    }

                    Double bal = Double.parseDouble(balance);

                    if (!Take) {
                        amount = amount * -1;
                    } else {
                        if (bal + amount > 9000000000000D) {
                            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f출금시 최대 금액 한도를 초과하므로 자동으로 해당 출금요청을 취소합니다\n");
                            Take = null;
                            amount = null;
                            bal = null;
                            return;
                        }
                    }

                    String FactionUUID = FactionUtils.getPlayerFactionUUID(UUID);

                    CompletableFuture<Double> futurebank = SQL.getDatabase().AddFactionBank(FactionUUID, amount);

                    Double finalbank = futurebank.get();
                    if (finalbank < 0) {
                        SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f국가 금고에서 해당 금액만큼을 출금하기에는 돈이 부족합니다\n");
                        if(!Take) {
                            SystemUtils.SendMoney(UUID, amount);
                        }
                        Take = null;
                        amount = null;
                        bal = null;
                        FactionUUID = null;
                        futurebank = null;
                        finalbank = null;
                        return;
                    }

                    String TakeorGet = null;

                    if (Take) {
                        TakeorGet = "&a출금";
                        //Main.econ.depositPlayer(op, amount);
                        SystemUtils.SendMoney(UUID, amount);
                    } else {
                        TakeorGet = "&a입금";
                    }

                    SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f성공적으로 해당 금액만큼을 국가 금고에서 " + TakeorGet + " &r했습니다. \n" +
                            "&r&7(남은금액 : " + df.format(finalbank) + "원)");
                    Take = null;
                    amount = null;
                    bal = null;
                    FactionUUID = null;
                    futurebank = null;
                    finalbank = null;
                    TakeorGet = null;

                } else {
                    SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f당신은 소속된 국가가 없습니다");
                }
            } catch (ExecutionException | InterruptedException e) {
                SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&c&lERROR &7오류 발생 : 오류코드 DB-D03 (시스템시간:" + SystemUtils.getDate(System.currentTimeMillis()) + ")");
                e.printStackTrace();
            }
        }).start();

    }
}
