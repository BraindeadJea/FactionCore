package com.itndev.FactionCore.SocketConnection.Server;

import com.itndev.FactionCore.Server;
import com.itndev.FactionCore.SocketConnection.IO.ProcessList;
import com.itndev.FactionCore.Utils.Database.Redis.Read;
import com.itndev.FactionCore.Utils.Factions.SystemUtils;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;

public class ConnectionThread extends Thread {

    private Socket socket;
    private InputStream input;
    private BufferedReader reader;
    private DataOutputStream out;
    private PrintWriter writer;

    public ConnectionThread(Socket clientSocket) {
        this.socket = clientSocket;
    }

    public void send(HashMap<String, String> localmap) {
        String map = Read.HashMap2String(localmap);
        writer.println(map + "\n\r");
    }

    public void close() throws IOException {
        socket.close();
    }

    public void run() {

        try {
            input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
            out = new DataOutputStream(socket.getOutputStream());
            writer = new PrintWriter(out, true);
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
                String line = reader.readLine();
                if ((line == null) || line.equalsIgnoreCase("QUIT")) {
                    socket.close();
                    break;
                }
                new Thread(() -> ProcessList.run(Read.String2HashMap(line))).start();
                //HashMap<String, String> map = Read.String2HashMap(line);
                //.add(map);
            } catch (IOException e) {
                SystemUtils.error_logger(Arrays.toString(e.getStackTrace()));
                break;
            }
        }
        try {
            socket.close();
        } catch (IOException e) {
            SystemUtils.error_logger(Arrays.toString(e.getStackTrace()));
        }
    }
}