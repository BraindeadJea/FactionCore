package com.itndev.FactionCore.Utils.Factions;

import com.itndev.FactionCore.Database.Redis.Obj.Storage;
import com.itndev.FactionCore.Utils.CommonUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class SystemUtils {

    public static String getPrefix() {
        return "&a&o&l[ &r&f국가 &a&o&l] &r&f";
    }

    private static DecimalFormat df = new DecimalFormat("0.00");

    public static void SendMoney(String UUID, Double Amount) {
        Storage.AddCommandToQueue("eco:=:give:=:" + CommonUtils.String2Byte(UUID) + ":=:" + CommonUtils.String2Byte(df.format(Amount)));
    }

    public static SimpleDateFormat timeformat = new SimpleDateFormat("yyyy-MM-dd hh:mm");

    public static UUID Convert2UUID(String UUID2) {
        return UUID.fromString(UUID2);
    }

    public static String FactionUUIDToDate(String FactionUUID) {
        Long time = Long.valueOf(FactionUUID.split("=")[0]);
        return timeformat.format(new Date(time));
    }

    public static String getDate(Long timedata) {
        return timeformat.format(new Date(timedata));
    }

    public static ArrayList string2list(String k) {
        if(k.contains(" ")) {
            String[] parts = k.split(" ");
            ArrayList<String> memlist = new ArrayList<>();
            for(String d : parts) {
                memlist.add(CommonUtils.Byte2String(d));
            }
            return memlist;
        } else {
            ArrayList<String> memlist = new ArrayList<>();
            memlist.add(CommonUtils.Byte2String(k));
            return memlist;
        }
    }
    public static String list2string(ArrayList<String> list) {
        String k = "";
        int i = 0;
        for(String c : list) {
            i = i + 1;
            if(list.size() > i) {
                k = k + CommonUtils.String2Byte(c) + " ";
            } else {
                k = k + CommonUtils.String2Byte(c);
            }
        }
        return k;
    }

    public static void logger(String message) {
        System.out.println("[SYSTEM/" + SystemUtils.getDate(System.currentTimeMillis()) + "] " + message);
    }

    public static void error_logger(String message) {
        System.out.println("[ERROR/" + SystemUtils.getDate(System.currentTimeMillis()) + "] " + message);
    }

    public static String Args2String(String[] args, int Start) {
        String FinalString = "";
        for(Integer k = Start; k < args.length; k++) {
            if(args[k] == null) {
                break;
            }
            if(k == args.length - 1) {
                FinalString = FinalString + args[k];
            } else {
                FinalString = FinalString + args[k] + " ";
            }
            k = null;
        }
        return FinalString;
    }

    public static void UUID_BASED_MSG_SENDER(String UUID, String Message) {
        Storage.AddCommandToQueue("notify:=:" + CommonUtils.String2Byte(UUID) + ":=:" + CommonUtils.String2Byte(UUID) + ":=:" + CommonUtils.String2Byte(Message) + ":=:" + CommonUtils.String2Byte("false"));
    }

    public static String KeepAlive() {
        String UUID = java.util.UUID.randomUUID().toString();
        Storage.ignoreUpdate_AddCommandToQueue("keepalive:=:" + CommonUtils.String2Byte(UUID));
        return UUID;
    }

    public static void UUID_BASED_PURE_MSG_SENDER(String UUID, String Message) {
        //FactionUtils.SendFactionMessage(UUID, "puremessagesendoptiontrue", "single", "");
        Storage.AddCommandToQueue("notify:=:" + CommonUtils.String2Byte(UUID) + ":=:" + CommonUtils.String2Byte("puremessagesendoptiontrue") + ":=:" + CommonUtils.String2Byte(Message) + ":=:" + CommonUtils.String2Byte("false"));
    }
}
