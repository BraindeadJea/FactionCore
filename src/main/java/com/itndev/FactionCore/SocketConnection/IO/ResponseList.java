package com.itndev.FactionCore.SocketConnection.IO;

import com.itndev.FactionCore.SocketConnection.Server.ConnectionThread;
import com.itndev.FaxLib.Utils.Data.DataStream;

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

    public void removeOldConnections() {
        Threads.forEach(thread -> {
            if(thread.isClosed()) {
                Threads.remove(thread);
            }
        });
    }

    private final ArrayList<ConnectionThread> Threads = new ArrayList<>();

    public void add(ConnectionThread serverThread) {
        Threads.add(serverThread);
    }

    public void remove(ConnectionThread serverThread) {
        Threads.remove(serverThread);
    }

    public void response(DataStream stream) {
        new Thread(() -> {Threads.forEach(serverThread -> {
            try {
                serverThread.send(stream);
            } catch (IOException e) {
                this.Threads.remove(serverThread);
                try {
                    serverThread.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                e.printStackTrace();
            }
        });
        }).start();

    }

    public void closeAll() {
        response(new DataStream());
        Threads.forEach(serverThread -> {
            try {
                serverThread.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
