package com.coffeeshop;

import com.coffeeshop.Cache.IngredientCache;
import com.coffeeshop.Cache.MenuCompositionCache;
import com.coffeeshop.Cache.OutletAvailabilityCache;
import com.coffeeshop.Cache.OutletMenuCache;
import com.coffeeshop.Controller.CoffeeShopController;
import com.coffeeshop.Controller.StockController;
import com.coffeeshop.Handler.ClientHandler;
import com.coffeeshop.Pojos.*;
import com.coffeeshop.Scheduler.IngredientCacheScheduler;
import com.coffeeshop.Scheduler.MenuCompositionCacheSchedular;
import com.coffeeshop.Scheduler.OutletAvailabilityCacheScheduler;
import com.coffeeshop.Scheduler.OutletMenuCacheScheduler;
import com.coffeeshop.SessionFactory.SessionFactoryImpl;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import javax.persistence.Query;
import java.net.ServerSocket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CoffeeShopServer {

    static final int PORT = 8080;
    static final boolean verbose = true;
    static ExecutorService threadPool = Executors.newFixedThreadPool(10);



    public static void main(String[] args) throws Exception {
        try {
            Injector injector = Guice.createInjector();
            SessionFactoryImpl sessionFactoryImpl = injector.getInstance(SessionFactoryImpl.class);
            CoffeeShopController coffeeShopController = injector.getInstance(CoffeeShopController.class);
            StockController stockController = injector.getInstance(StockController.class);

            System.out.println("Please select 1 if need to initialise the database");
            Scanner sc = new Scanner(System.in);
            int flag = sc.nextInt();
            if(flag == 1)
                stockController.init();


            MenuCompositionCacheSchedular menuCompositionCacheScheduler = injector.getInstance(MenuCompositionCacheSchedular.class);
            IngredientCacheScheduler ingredientCacheScheduler = injector.getInstance(IngredientCacheScheduler.class);
            OutletMenuCacheScheduler outletMenuCacheScheduler = injector.getInstance(OutletMenuCacheScheduler.class);
            OutletAvailabilityCacheScheduler outletAvailabilityCacheScheduler = injector.getInstance(OutletAvailabilityCacheScheduler.class);
            Timer timer = new Timer();
            timer.schedule(menuCompositionCacheScheduler, menuCompositionCacheScheduler.getDELAY_MS(), menuCompositionCacheScheduler.getPERIOD_MS());
            timer.schedule(ingredientCacheScheduler, ingredientCacheScheduler.getDELAY_MS(), ingredientCacheScheduler.getPERIOD_MS());
            timer.schedule(outletMenuCacheScheduler, outletMenuCacheScheduler.getDELAY_MS(), outletMenuCacheScheduler.getPERIOD_MS());
            timer.schedule(outletAvailabilityCacheScheduler, outletAvailabilityCacheScheduler.getDELAY_MS());

            ServerSocket serverSocket = new ServerSocket(PORT);



            System.out.println("Server started.\nListening for connections on port : " + PORT + " ...\n");

            while (true) {
                ClientHandler clientHandler = new ClientHandler(serverSocket.accept(), coffeeShopController);

                if (verbose) {
                    System.out.println("Connection opened. (" + new Date() + ")");
                }

                // create dedicated thread to manage the client connection
                threadPool.execute(clientHandler);
            }
        } catch (Exception e) {
            System.err.println("Server Connection error : " + e.getMessage());
        }
    }
}
