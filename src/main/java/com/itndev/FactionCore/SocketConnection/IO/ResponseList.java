package com.itndev.FactionCore.SocketConnection.IO;

import com.itndev.FactionCore.SocketConnection.Server.ConnectionThread;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ResponseList {

    private static ResponseList instance = null;

    private ResponseList() {
        instance = this;
    }

    public static ResponseList get() {
        if(instance == null) {
            instance = new ResponseList();
        }
        return instance;
    }

    private ArrayList<ConnectionThread> Threads = new ArrayList<>();

    public void add(ConnectionThread serverThread) {
        Threads.add(serverThread);
    }

    public void remove(ConnectionThread serverThread) {
        Threads.remove(serverThread);
    }

    public void response(HashMap<String, String> map) {
        Threads.forEach(serverThread -> new Thread(() -> serverThread.send(map)).start());
    }

    public void closeAll() {
        Threads.forEach(serverThread -> {
            try {
                serverThread.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
