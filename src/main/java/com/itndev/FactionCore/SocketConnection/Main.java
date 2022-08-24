package com.itndev.FactionCore.SocketConnection;

import com.itndev.FactionCore.Database.Redis.Connect;
import com.itndev.FactionCore.Database.Redis.Obj.Storage;
import com.itndev.FactionCore.SocketConnection.IO.ProcessList;
import com.itndev.FactionCore.SocketConnection.Server.Server;

import java.util.HashMap;

public class Main {

    public static Server server;

    public static void launch() {
        server = new Server();
        new Thread(() -> server.run()).start();
        output();
    }

    public static void output() {
        new Thread(() -> {
            while(true) {
                if(!com.itndev.FactionCore.Server.Streamable) {
                    break;
                }
                synchronized (Storage.TempCommandQueue) {
                    HashMap<String, String> map = Storage.TempCommandQueue;
                    Storage.TempCommandQueue.clear();
                    ProcessList.run(map);
                }
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
