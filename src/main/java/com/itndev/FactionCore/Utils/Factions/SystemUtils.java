package com.itndev.FactionCore.Utils.Factions;

import com.itndev.FactionCore.Database.Redis.Obj.Storage;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class SystemUtils {

    public static String getPrefix() {
        return "&a&o&l[ &r&f국가 &a&o&l] &r&f";
    }

    private static DecimalFormat df = new DecimalFormat("0.00");

    public static void SendMoney(String UUID, Double Amount) {
        Storage.AddCommandToQueue("eco:=:give:=:" + UUID + ":=:" + df.format(Amount));
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
        if(k.contains("<%&LISTSPLITTER&%>")) {
            String[] parts = k.split("<%&LISTSPLITTER&%>");
            ArrayList<String> memlist = new ArrayList<>();
            for(String d : parts) {
                memlist.add(d);
            }
            parts = null;
            return memlist;
        } else {
            ArrayList<String> memlist = new ArrayList<>();
            memlist.add(k);
            return memlist;
        }
    }
    public static String list2string(ArrayList<String> list) {
        String k = "";
        Integer i = 0;
        for(String c : list) {
            i = i + 1;
            if(list.size() > i) {
                k = k + c + "<%&LISTSPLITTER&%>";
            } else {
                k = k + c;
            }

            c = null;
        }
        i = null;
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
        Storage.AddCommandToQueue("notify:=:" + UUID + ":=:" + UUID + ":=:" + Message + ":=:" + "false");
    }

    public static void UUID_BASED_PURE_MSG_SENDER(String UUID, String Message) {
        //FactionUtils.SendFactionMessage(UUID, "puremessagesendoptiontrue", "single", "");
        Storage.AddCommandToQueue("notify:=:" + UUID + ":=:" + "puremessagesendoptiontrue" + ":=:" + Message + ":=:" + "false");
    }
}
