package com.itndev.FactionCore.SocketConnection.Server;

import com.itndev.FactionCore.SocketConnection.IO.ResponseList;
import com.itndev.FactionCore.Utils.Factions.SystemUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
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
            while (com.itndev.FactionCore.Server.Streamable) {
                Socket socket = serverSocket.accept();
                ConnectionThread ServerThread = new ConnectionThread(socket);
                ServerThread.start();
                ResponseList.get().add(ServerThread);
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
