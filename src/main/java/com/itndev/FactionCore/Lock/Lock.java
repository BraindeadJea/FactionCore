package com.itndev.FactionCore.Lock;

import java.util.ArrayList;
import java.util.HashMap;

public class Lock {

    public static ArrayList<String> list = new ArrayList<>();
    private static HashMap<String, LocalLock> map = new HashMap<>();
    private final static LocalLock sync = new LocalLock("public");

    private final static Object publiclock = new Object();

    public static LocalLock getLock(String key) {
        if(!map.containsKey(key)) {
            map.put(key, new LocalLock(key));
            return sync;
        }
        return map.get(key);
    }

    public static Object getPublicLock() {
        return publiclock;
    }


    public static Boolean hasLock(String key) {
        getLock(key);
        return map.containsKey(key);
    }

    public static void AckLock(String key) {
        list.add(key);
    }

    public static Boolean CachedhasLock(String key) {
        return list.contains(key);
    }

    /**
     locking -> how the lock is done

     if(Lock.CachedhasLock(lockkey)) {
        synchronized (Lock.getLock(lockkey).getLock()) {
            //do
        }
     } else {
        if (Lock.hasLock(lockkey)) {
            synchronized (Lock.getLock(lockkey).getLock()) {
                //do
                Lock.AckLock(lockkey);
            }
        } else {
            synchronized (Lock.getPublicLock()) {
                //do
            }
        }
     }
     */


}
