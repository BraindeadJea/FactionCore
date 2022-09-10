package com.itndev.FaxLib.Utils.Data;

import com.itndev.FaxLib.Utils.Global;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class MapFixer {

    private static MapFixer instance = null;
    public static MapFixer get() {
        if(instance == null) {
            instance = new MapFixer();
        }
        return instance;
    }

    public HashMap<String, String> Fixer_1_1(ConcurrentHashMap<String, String> map) {
        HashMap<String, String> finalmap = new HashMap<>();
        for(String key : map.keySet()) {
            finalmap.put(key, map.get(key));

        }
        return finalmap;
    }

    public ConcurrentHashMap<String, String> Fixer_1_2(HashMap<String, String> map) {
        ConcurrentHashMap<String, String> finalmap = new ConcurrentHashMap<>();
        for(String key : map.keySet()) {
            finalmap.put(key, map.get(key));

        }
        return finalmap;
    }

    public HashMap<String, ArrayList<String>> Fixer_2_1(HashMap<String, String> map) {
        HashMap<String, ArrayList<String>> finalmap = new HashMap<>();
        if(!map.isEmpty()) {
            for(String key : map.keySet()) {
                finalmap.put(key, Global.string2list(map.get(key)));
            }
        }
        return finalmap;
    }

    public HashMap<String, String> Fixer_2_2(HashMap<String, ArrayList<String>> map) {
        HashMap finalMap = new HashMap<String, String>();
        if(!map.isEmpty()) {
            for(String key : map.keySet()) {
                finalMap.put(key, Global.list2string(map.get(key)));
            }
        }
        return finalMap;
    }

    public ConcurrentHashMap<String, ArrayList<String>> Fixer_3_1(HashMap<String, String> map) {
        ConcurrentHashMap<String, ArrayList<String>> finalmap = new ConcurrentHashMap<>();
        if(!map.isEmpty()) {
            for(String key : map.keySet()) {
                finalmap.put(key, Global.string2list(map.get(key)));
            }
        }
        return finalmap;
    }

    public HashMap<String, String> Fixer_3_2(ConcurrentHashMap<String, ArrayList<String>> map) {
        synchronized (map) {
            HashMap finalMap = new HashMap<String, String>();
            if(!map.isEmpty()) {
                for(String key : map.keySet()) {
                    finalMap.put(key, Global.list2string(map.get(key)));
                }
            }
            return finalMap;
        }
    }
}
