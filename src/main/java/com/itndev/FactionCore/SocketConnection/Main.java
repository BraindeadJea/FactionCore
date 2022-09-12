package com.itndev.FactionCore.SocketConnection;

import com.itndev.FactionCore.Database.Redis.Connect;
import com.itndev.FactionCore.Database.Redis.Obj.Storage;
import com.itndev.FactionCore.SocketConnection.IO.ProcessList;
import com.itndev.FactionCore.SocketConnection.IO.ResponseList;
import com.itndev.FactionCore.SocketConnection.Server.Server;
import com.itndev.FactionCore.Utils.Database.Redis.Read;
import com.itndev.FactionCore.Utils.Database.Redis.StaticVal;
import com.itndev.FaxLib.Utils.Data.DataStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {

    private static Server server;

    public static void launch() {
        server = new Server();
        new Thread(() -> server.run()).start();
        output();
    }

    private static void output() {
        new Thread(() -> {
            try {
                while (true) {
                    HashMap<Integer, Object> map = new HashMap<>();
                    synchronized (Storage.TempCommandQueue) {
                        if (!Storage.TempCommandQueue.isEmpty()) {
                            map.put(StaticVal.getServerNameArgs(), "BackEnd");
                            map.put(StaticVal.getDataTypeArgs(), "BackEnd-Responce");
                            List<String> temp = new ArrayList<>(Storage.TempCommandQueue.stream().toList());
                            map.put(1, temp);
                            Storage.TempCommandQueue.clear();
                            ResponseList.response(map);
                        }
                        //stream = new DataStream("BackEnd", "BackEnd-Responce", Storage.TempCommandQueue);

                    }

                    Thread.sleep(2);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

    }

    public static void close() {
        ResponseList.closeAll();
    }
}
