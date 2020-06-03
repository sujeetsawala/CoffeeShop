package com.coffeeshop;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class StockClient {
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

                    System.out.println("1. Get All Outlets");
                    System.out.println("2. Add ingredients to an outlet");
                    System.out.println("3. Update ingredient threshold");
                    /**
                     *  Add ingredients to an outlet: 2 outletName [Composition]
                     *  Update ingredient threshold: 3 IngredientName IngredientQuantity
                     *  Get All Outlets: 1
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
