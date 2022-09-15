package com.itndev.FactionCore.SocketConnection.IO;

import com.itndev.FactionCore.SocketConnection.Server.Old.ConnectionThread;
import com.itndev.FactionCore.SocketConnection.Server.PacketHandler;

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
            PacketHandler.getChannels().writeAndFlush(stream);
        }).start();

    }

    public static void closeAll() throws InterruptedException {
        response(new HashMap<Integer, Object>());
        PacketHandler.getChannels().close().sync();
        //Threads.forEach(ConnectionThread::close);
    }
}
