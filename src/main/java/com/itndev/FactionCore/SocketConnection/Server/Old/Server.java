package com.itndev.FactionCore.SocketConnection.Server.Old;

import com.itndev.FactionCore.SocketConnection.IO.ResponseList;
import com.itndev.FactionCore.Utils.Factions.SystemUtils;

import java.io.*;
import java.net.ServerSocket;
import java.util.Arrays;

public class Server {

    private ServerSocket serverSocket;



    public void run() {
        try {
            while(true) {
                if(com.itndev.FactionCore.Server.Streamable) {
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            serverSocket = new ServerSocket(com.itndev.FactionCore.SocketConnection.Socket.getPort());
            while (true) {
                ConnectionThread ServerThread = new ConnectionThread(serverSocket.accept());
                ServerThread.start();
                ResponseList.add(ServerThread);
                SystemUtils.logger("New Connection From Client");
                SystemUtils.logger(ServerThread.toString());
            }

            //Socket socket = serverSocket.accept();
            //System.out.println("[ " + socket.getInetAddress() + " ] client connected");
            //OutputStream output = socket.getOutputStream();
            /*InputStream input = socket.getInputStream();
            PrintWriter writer = new PrintWriter(output, true);
            while (socket.isConnected()) {
                writer.println("//정보 보내기");
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                String line = reader.readLine();//정보 읽기
            }*/

        } catch (IOException e) {
            SystemUtils.error_logger(Arrays.toString(e.getStackTrace()));
        }
    }
}
