package com.itndev.FactionCore.SocketConnection.Server;

import com.itndev.FactionCore.Server;
import com.itndev.FactionCore.SocketConnection.IO.ProcessList;
import com.itndev.FactionCore.SocketConnection.IO.ResponseList;
import com.itndev.FactionCore.Utils.Database.Redis.Read;
import com.itndev.FactionCore.Utils.Factions.SystemUtils;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;

public class ConnectionThread extends Thread {

    private final Socket socket;

    private ObjectInputStream input;

    private ObjectOutputStream output;

    private Boolean isClosed = false;

    public Boolean isClosed() {
        return this.isClosed;
    }

    public ConnectionThread(Socket clientSocket) {
        this.socket = clientSocket;
    }

    public void send(HashMap<Integer, String> map) throws IOException {
        if(map != null) {
            if(output == null) {
                System.out.println("OutputStream is Null");
                return;
            }
            output.writeObject(map);
            output.flush();
        } else {
            System.out.println("null on map");
        }
    }

    public void close() throws IOException {
        this.closeAll();
        ResponseList.get().remove(this);
    }

    public void run() {

        try {
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            return;
        }

        /*new Thread(() -> {
            while (true) {
                if(socket.isClosed() || !Server.Streamable) {
                    break;
                }
                try {

                } catch (Exception e) {
                    SystemUtils.error_logger(Arrays.toString(e.getStackTrace()));
                    break;
                }
            }
            try {
                socket.close();
            } catch (IOException e) {
                SystemUtils.error_logger(Arrays.toString(e.getStackTrace()));
            }
        }).start();*/
        while (true) {

            if(socket.isClosed() || !Server.Streamable) {
                break;
            }
            try {
                HashMap<Integer, String> map = (HashMap<Integer, String>) input.readObject();
                if (map == null || map.isEmpty()) {
                    this.closeAll();
                    SystemUtils.error_logger("Connection Broken... Plase Reconnect");
                    break;
                }
                //System.out.println(line);
                new Thread(() -> ProcessList.run(map));
                //HashMap<String, String> map = Read.String2HashMap(line);
                //.add(map);
            } catch (IOException | ClassNotFoundException e) {
                SystemUtils.error_logger(Arrays.toString(e.getStackTrace()));
                SystemUtils.error_logger("Connection Broken... Please Reconnect");
                break;
            }
        }
        try {
            this.closeAll();
        } catch (IOException e) {
            SystemUtils.error_logger(Arrays.toString(e.getStackTrace()));
            SystemUtils.error_logger("Connection Broken... Plase Reconnect");
        }
        isClosed = true;
        ResponseList.get().remove(this);
        ResponseList.get().removeOldConnections();
    }

    public void closeAll() throws IOException {
        SystemUtils.error_logger("Disconnecting....");
        socket.close();
    }
}