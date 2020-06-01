package com.coffeeshop;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *  Client for order from outlets
 *  author: Sujeet Sawala
 */

public class Client {
    static final String ServerHostName = "localhost";
    static final int ServerPort = 8080;

    public static void main(String args[]) {
        try {
            Socket socket = new Socket(ServerHostName, ServerPort);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Please select one of the following options");
            try {
                while (true) {
                    System.out.println("1. Enter outletNo and menu item to be ordered");
                    System.out.println("2. Get Menu At Outlet");
                    System.out.println("3. All Outlets");

                    /**
                     *  Order Input: 1 OutletName MenuName
                     *  Get Menu Input: 2 OutletName
                     *  Get All Outlets: 3
                     */

                    String request = keyboard.readLine();
                    out.println(request);
                    System.out.println(in.readLine());
                }
            } finally {
                in.close();
                out.close();
                keyboard.close();
                socket.close();
            }
        } catch (Exception e) {
            System.err.println("Error Connecting socket :" + e.getMessage());
        }
    }
}
