package com.itndev.FactionCore.SocketConnection.Client;

import com.itndev.FactionCore.Utils.Factions.SystemUtils;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class Client {

    private String hostname;
    private int port;

    private Socket clientSocket;
    private BufferedReader input;
    private PrintStream output;
    private PrintWriter writer;
    private Scanner scanner = new Scanner(System.in);


    public Client(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
        run();
    }


    private void run() {
        try {
            clientSocket = new Socket("localhost", com.itndev.FactionCore.SocketConnection.Socket.getPort());

            output = new PrintStream(clientSocket.getOutputStream());

            output.println("Connection Enabled");

            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            writer = new PrintWriter(output, true);
            while(clientSocket.isConnected()) {
                String message = input.readLine();
                System.out.println(message);

                String line = scanner.nextLine();
            }

        } catch (IOException e) {
            SystemUtils.error_logger(Arrays.toString(e.getStackTrace()));
        }
    }
}
