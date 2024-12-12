package com.example.myapplication.db;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class ServerConnection {

    static private ServerConnection instance;
    static public ServerConnection getInstance() {
        if (instance == null) {
            instance = new ServerConnection();
        }
        return instance;
    }

    private Socket client;
    private String ip;
    private int port;

    private PrintWriter writer;
    private BufferedReader reader;

    private ServerConnection(){
    }

    public void startConnection(String ip, int port) throws IOException, JSONException {

        this.ip = ip;
        this.port = port;
        System.out.println("=== Connecting to the Server... ===");
        client = new Socket(ip, port);
        writer = new PrintWriter(client.getOutputStream(), true);
        reader = new BufferedReader(new InputStreamReader(client.getInputStream()));

    }

    public void sendMessage(String msg) throws IOException {
        writer.println(msg);
    }

    public String readMessage() throws IOException{

        int timeout = 1000;
        String line = "";
        //client.setSoTimeout(timeout);

        for(int i=0;i<4;i++){
            line += reader.readLine();
        }

        return line;
    }

    public void stopConnection() throws IOException {
        reader.close();
        writer.close();
        client.close();
    }
}
