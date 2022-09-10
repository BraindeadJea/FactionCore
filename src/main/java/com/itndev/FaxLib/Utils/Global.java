package com.itndev.FaxLib.Utils;

import java.util.ArrayList;

public class Global {

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
}
