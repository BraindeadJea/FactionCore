package com.itndev.FactionCore.Utils.Database.Redis;

import com.itndev.FactionCore.Database.Redis.CmdExecute;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class Read {

    @Deprecated
    public static void READ_COMPRESSEDMAP(String compressedhashmap) {
        HashMap<String, String> map = String2HashMap(compressedhashmap);
        if (!map.isEmpty()) {
            for(int c = 1; c <= Integer.parseInt(map.get(StaticVal.getMaxAmount())); c++) {
                //JedisManager.updatehashmap(map.get(String.valueOf(c)), clientname);
                CmdExecute.CMD_READ(map.get(String.valueOf(c)));
            }
        }
    }

    public static String HashMap2String(ConcurrentHashMap<String, String> map) {
        String finalbuildstring = StaticVal.getbuffer() + StaticVal.getsplitter();
        if(!map.isEmpty()) {
            int maxamount = Integer.parseInt(map.get(StaticVal.getMaxAmount()));
            for (int c = 1; c <= maxamount; c++) {
                if (c == maxamount) {
                    finalbuildstring = finalbuildstring + map.get(String.valueOf(c)) + StaticVal.getsplitter() + StaticVal.getbuffer();
                } else {
                    finalbuildstring = finalbuildstring + map.get(String.valueOf(c)) + StaticVal.getsplitter();
                }
            }
        } else {
            return null;
        }
        return finalbuildstring;
    }

    public static HashMap<String, String> String2HashMap(String info) {
        HashMap<String, String> finalmap = new HashMap<>();
        if(!info.contains(StaticVal.getsplitter())) {
            if(info.length() > 0) {
                finalmap.put("1", info);
                finalmap.put(StaticVal.getMaxAmount(), "1");
            }
            return finalmap;
        }
        String[] info_args = info.split(StaticVal.getsplitter());
        finalmap.put(StaticVal.getMaxAmount(), String.valueOf(info_args.length - 1));
        for(int c = 0; c < info_args.length; c++) {
            finalmap.put(String.valueOf(c + 1), info_args[c]);
        }
        return finalmap;
    }
}
