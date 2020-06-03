package com.coffeeshop.Handler;

import com.coffeeshop.Controller.CoffeeShopController;
import com.coffeeshop.Controller.StockController;
import com.coffeeshop.Pojos.Composition;
import com.coffeeshop.Pojos.Ingredients;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StockHandler implements Runnable {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private StockController stockController;

    public StockHandler(Socket socket, StockController stockController) throws IOException {
        this.socket = socket;
        this.stockController = stockController;
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
                        out.println("Received Request for parameters: " + request[0]);
                        List<String> result = stockController.getAllOutlets();
                        System.out.println(result);
                        out.println(result);
                        break;
                    }
                    case "2": {
                        out.println("Received order Request for parameters: " + request[1]);
                        String outletName = request[1];
                        out.println("Enter the list of ingredients to be updated");
                        String[] input = in.readLine().split("\\ ");
                        List<String> ingredientNames = Arrays.asList(input);
                        out.println("Ingredients Received: " + input[0] + "," + input[1]);
                        out.println("Enter the quantity of each of the above ingredients");
                        input = in.readLine().split("\\ ");
                        List<String> ingredientQuantity = Arrays.asList(input);
                        out.println("Ingredients Quantity Received: " + ingredientQuantity);

                        List<Composition> compositions = new ArrayList<>();
                        for(int i = 0; i < ingredientNames.size();i++)
                        {
                            Composition composition = new Composition();
                            composition.setIngredient(Ingredients.valueOf(ingredientNames.get(i)));
                            composition.setIngredientQuantity(Integer.parseInt(ingredientQuantity.get(i)));
                            compositions.add(composition);
                        }

                        stockController.updateIngredientsToOutlet(outletName, compositions);
                        break;
                    }
                    case "3": {
                        out.println("Received Request for parameters: " + request[0]);
                        String ingredientName = request[1];
                        int ingredientQuantity = Integer.parseInt(request[2]);
                        stockController.addIngredientThreshold(ingredientName, ingredientQuantity);
                        out.println("Ingredient threshold updated");
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error in Connection " + e.getMessage());
        }
    }
}
