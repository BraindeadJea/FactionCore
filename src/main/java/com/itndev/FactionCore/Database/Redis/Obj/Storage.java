package com.itndev.FactionCore.Database.Redis.Obj;

import com.itndev.FactionCore.Database.Redis.CmdExecute;
import com.itndev.FactionCore.Utils.Database.Redis.StaticVal;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Storage {
    public static final HashMap<Integer, String> TempCommandQueue = new HashMap<>();

    public static void AddCommandToQueue(String command) {
        synchronized (TempCommandQueue) {
            CmdExecute.get().updatehashmap(command);
            if(TempCommandQueue.isEmpty()) {
                //TempCommandQueue.put(StaticVal.getMaxAmount(), "1");
                TempCommandQueue.put(1, command);
            } else {
                //Integer num = Integer.parseInt(TempCommandQueue.get(StaticVal.getMaxAmount()));
                //TempCommandQueue.put(StaticVal.getMaxAmount(), String.valueOf(num + 1));
                TempCommandQueue.put(TempCommandQueue.size() + 1, command);
            }
        }
    }

    public static void ignoreUpdate_AddCommandToQueue(String command) {
        synchronized (TempCommandQueue) {
            if(TempCommandQueue.isEmpty()) {
                //TempCommandQueue.put(StaticVal.getMaxAmount(), "1");
                TempCommandQueue.put(1, command);
            } else {
                //Integer num = Integer.parseInt(TempCommandQueue.get(StaticVal.getMaxAmount()));
                //TempCommandQueue.put(StaticVal.getMaxAmount(), String.valueOf(num + 1));
                TempCommandQueue.put(TempCommandQueue.size() + 1, command);
            }
        }
    }

    public static void AddCommandToQueueFix(String command, String nothing) {
        synchronized (TempCommandQueue) {
            CmdExecute.get().updatehashmap(command);
            if(TempCommandQueue.isEmpty()) {
                //TempCommandQueue.put(StaticVal.getMaxAmount(), "1");
                TempCommandQueue.put(1, command);
            } else {
                //Integer num = Integer.parseInt(TempCommandQueue.get(StaticVal.getMaxAmount()));
                //TempCommandQueue.put(StaticVal.getMaxAmount(), String.valueOf(num + 1));
                TempCommandQueue.put(TempCommandQueue.size() + 1, command);
            }
        }
    }

    public static void AddBulkCommandToQueue(List<String> BulkCMD) {
        synchronized (TempCommandQueue) {
            for(String k : BulkCMD) {
                CmdExecute.get().updatehashmap(k);
            }
            for(String command : BulkCMD) {
                if(TempCommandQueue.isEmpty()) {
                    //TempCommandQueue.put(StaticVal.getMaxAmount(), "1");
                    TempCommandQueue.put(1, command);
                } else {
                    //Integer num = Integer.parseInt(TempCommandQueue.get(StaticVal.getMaxAmount()));
                    //TempCommandQueue.put(StaticVal.getMaxAmount(), String.valueOf(num + 1));
                    TempCommandQueue.put(TempCommandQueue.size() + 1, command);
                }
            }
        }
    }
}
