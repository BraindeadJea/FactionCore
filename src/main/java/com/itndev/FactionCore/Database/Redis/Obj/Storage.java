package com.itndev.FactionCore.Database.Redis.Obj;

import com.itndev.FactionCore.Database.Redis.CmdExecute;
import com.itndev.FactionCore.Utils.Database.Redis.StaticVal;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Storage {
    public static final ConcurrentHashMap<String, String> TempCommandQueue = new ConcurrentHashMap<>();


    public static void AddCommandToQueue(String command) {
        CmdExecute.updatehashmap(command);
        synchronized (TempCommandQueue) {
            if(!TempCommandQueue.containsKey(StaticVal.getMaxAmount())) {
                TempCommandQueue.put(StaticVal.getMaxAmount(), "1");
                TempCommandQueue.put("1", command);
            } else {
                Integer num = Integer.parseInt(TempCommandQueue.get(StaticVal.getMaxAmount()));
                TempCommandQueue.put(StaticVal.getMaxAmount(), String.valueOf(num + 1));
                TempCommandQueue.put(String.valueOf(num + 1), command);
                num = null;
            }
        }
    }

    public static void AddCommandToQueueFix(String command, String nothing) {
        CmdExecute.updatehashmap(command);
        synchronized (TempCommandQueue) {
            if (!TempCommandQueue.containsKey(StaticVal.getMaxAmount())) {
                TempCommandQueue.put(StaticVal.getMaxAmount(), "1");
                TempCommandQueue.put("1", command);
            } else {
                Integer num = Integer.parseInt(TempCommandQueue.get(StaticVal.getMaxAmount()));
                TempCommandQueue.put(StaticVal.getMaxAmount(), String.valueOf(num + 1));
                TempCommandQueue.put(String.valueOf(num + 1), command);
                num = null;
            }
        }
    }

    public static void AddBulkCommandToQueue(List<String> BulkCMD) {
        for(String k : BulkCMD) {
            CmdExecute.updatehashmap(k);
        }
        synchronized (TempCommandQueue) {
            for(String command : BulkCMD) {
                if(!TempCommandQueue.containsKey(StaticVal.getMaxAmount())) {
                    TempCommandQueue.put(StaticVal.getMaxAmount(), "1");
                    TempCommandQueue.put("1", command);
                } else {
                    int num = Integer.parseInt(TempCommandQueue.get(StaticVal.getMaxAmount()));
                    TempCommandQueue.put(StaticVal.getMaxAmount(), String.valueOf(num + 1));
                    TempCommandQueue.put(String.valueOf(num + 1), command);
                }
            }
        }
    }
}
