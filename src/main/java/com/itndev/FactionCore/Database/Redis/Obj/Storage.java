package com.itndev.FactionCore.Database.Redis.Obj;

import com.itndev.FactionCore.Database.Redis.CmdExecute;
import com.itndev.FactionCore.Utils.Database.Redis.StaticVal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Storage {
    public static final List<String> TempCommandQueue = new ArrayList<>();

    public static void AddCommandToQueue(String command) {
        synchronized (TempCommandQueue) {
            CmdExecute.get().updatehashmap(command);
            TempCommandQueue.add(command);
        }
    }

    public static void ignoreUpdate_AddCommandToQueue(String command) {
        synchronized (TempCommandQueue) {
            TempCommandQueue.add(command);
        }
    }

    public static void AddCommandToQueueFix(String command, String nothing) {
        synchronized (TempCommandQueue) {
            CmdExecute.get().updatehashmap(command);
            TempCommandQueue.add(command);
        }
    }

    public static void AddBulkCommandToQueue(List<String> BulkCMD) {
        synchronized (TempCommandQueue) {
            BulkCMD.forEach(CmdExecute.get()::updatehashmap);
            TempCommandQueue.addAll(BulkCMD);
        }
    }
}
