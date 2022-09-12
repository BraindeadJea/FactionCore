package com.itndev.FactionCore.SocketConnection.IO;

import com.itndev.FactionCore.SocketConnection.Server.ConnectionThread;
import com.itndev.FaxLib.Utils.Data.DataStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ResponseList {



    public static void removeOldConnections() {
        Threads.forEach(thread -> {
            if(thread.isClosed()) {
                Threads.remove(thread);
            }
        });
    }

    private static final ArrayList<ConnectionThread> Threads = new ArrayList<>();

    public static void add(ConnectionThread serverThread) {
        Threads.add(serverThread);
    }

    public static void remove(ConnectionThread serverThread) {
        synchronized (Threads) {
            Threads.remove(serverThread);
        }
    }

    public static void response(HashMap<Integer, Object> stream) {
        new Thread(() -> {
            synchronized (Threads) {
                //System.out.println(ResponseList.class.getCanonicalName());
                Threads.forEach(serverThread -> {
                    try {
                        serverThread.send(stream);
                    } catch (IOException e) {
                        serverThread.close();
                        e.printStackTrace();
                    }
                });
            }
        }).start();

    }

    public static void closeAll() {
        response(new HashMap<Integer, Object>());
        Threads.forEach(serverThread -> {
            try {
                serverThread.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
