package com.coffeeshop.Handler;

import com.coffeeshop.Controller.CoffeeShopController;
import com.google.inject.Inject;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private CoffeeShopController coffeeShopController;

    public ClientHandler(Socket socket, CoffeeShopController coffeeShopController) throws IOException {
        this.socket = socket;
        this.coffeeShopController = coffeeShopController;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
    }
    @Override
    public void run(){
        try {
            while(true) {
                String[] request = in.readLine().split("\\s+");
                switch (request[0]) {
                    case "1": {
                        out.println("Received order Request for parameters: " + request[1] + "," + request[2]);
                        String result = coffeeShopController.getOrder(request[1], request[2]);
                        System.out.println(result);
                        out.println(result);
                        break;
                    }
                    case "2": {
                        out.println("Received order Request for parameters: " + request[1]);
                        List<String> result = coffeeShopController.getMenuForOutlet(request[1]);
                        out.println(result);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error in Connection " + e.getMessage());
        }
    }
}
