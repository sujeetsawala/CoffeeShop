package com.coffeeshop;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    static final String ServerHostName = "localhost";
    static final int ServerPort = 8080;

    public static void main(String args[]) {
        try {

            Socket socket = new Socket(ServerHostName, ServerPort);
            BufferedReader in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            BufferedReader keyboard  = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Please select one of the following options");

            while(true) {
                System.out.println("1. Enter outletNo and menu item to be ordered");
                System.out.println("2. Get Menu At Outlet");

                String[] inputs = keyboard.readLine().split("\\ ");

            }
        } catch (Exception e) {
            System.err.println("Client Connection error : " + e.getMessage());
        }

    }
}
