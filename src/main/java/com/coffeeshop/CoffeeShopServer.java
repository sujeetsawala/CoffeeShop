package com.coffeeshop;

import com.coffeeshop.Cache.IngredientCache;
import com.coffeeshop.Cache.MenuCompositionCache;
import com.coffeeshop.Cache.OutletAvailabilityCache;
import com.coffeeshop.Cache.OutletMenuCache;
import com.coffeeshop.Controller.CoffeeShopController;
import com.coffeeshop.Pojos.*;
import com.coffeeshop.SessionFactory.SessionFactoryImpl;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.Query;
import java.util.List;
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

            Composition composition1 = new Composition();
            composition1.setIngredient(Ingredients.HOT_MILK);
            composition1.setIngredientQuantity(50);

            Composition composition2 = new Composition();
            composition2.setIngredient(Ingredients.HOT_MILK);
            composition2.setIngredientQuantity(60);

            Composition composition3 = new Composition();
            composition3.setIngredient(Ingredients.HOT_WATER);
            composition3.setIngredientQuantity(60);

            Composition composition4 = new Composition();
            composition4.setIngredient(Ingredients.HOT_MILK);
            composition4.setIngredientQuantity(100);

            Composition composition5 = new Composition();
            composition5.setIngredient(Ingredients.HOT_WATER);
            composition5.setIngredientQuantity(100);

            OutletDetails outletDetails = new OutletDetails();
            outletDetails.setOutletName(OutletName.OUTLET1);
            outletDetails.getAvailableComposition().add(composition4);
            outletDetails.getAvailableComposition().add(composition5);


            OutletDetails outletDetails2 = new OutletDetails();
            outletDetails2.setOutletName(OutletName.OUTLET2);
            outletDetails2.getAvailableComposition().add(composition1);
            //outletDetails2.getAvailableIngredients().add(composition2);



//            outletMenuDetails1.getAvailableIngredients().add(composition1);
//            outletMenuDetails1.getAvailableIngredients().add(composition3);
//            outletMenuDetails2.getAvailableIngredients().add(composition2);
            OutletMenuDetails outletMenuDetails1 = new OutletMenuDetails();
            outletMenuDetails1.setOutletName(OutletName.OUTLET1);
//            outletMenuDetails1.getMenuDetails().add(menuDetails);
//            outletMenuDetails1.getMenuDetails().add(menuDetails2);


            OutletMenuDetails outletMenuDetails2 = new OutletMenuDetails();
            outletMenuDetails2.setOutletName(OutletName.OUTLET2);
//            outletMenuDetails2.getMenuDetails().add(menuDetails);
//            outletMenuDetails2.getMenuDetails().add(menuDetails2);

            MenuDetails menuDetails = new MenuDetails();
            menuDetails.setMenuName(Menus.HOT_MILK);
            menuDetails.getMenuCompositions().add(composition1);
            menuDetails.getMenuCompositions().add(composition3);
            menuDetails.getOutletMenuDetails().add(outletMenuDetails1);
            menuDetails.getOutletMenuDetails().add(outletMenuDetails2);

            MenuDetails menuDetails2 = new MenuDetails();
            menuDetails2.setMenuName(Menus.HOT_WATER);
            menuDetails2.getMenuCompositions().add(composition2);
            menuDetails2.getOutletMenuDetails().add(outletMenuDetails1);
            menuDetails2.getOutletMenuDetails().add(outletMenuDetails2);

            outletMenuDetails1.getMenuDetails().add(menuDetails);
            outletMenuDetails1.getMenuDetails().add(menuDetails2);
            outletMenuDetails2.getMenuDetails().add(menuDetails);




            SessionFactory sessionFactory = sessionFactoryImpl.getSessionFactory();
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            session.persist(outletMenuDetails1);
            session.persist(outletMenuDetails2);
            session.save(outletDetails);
            session.save(outletDetails2);
            session.getTransaction().commit();
            session.close();

//            session = sessionFactory.openSession();
//            session.beginTransaction();
//            Query createQuery = session.createQuery("select p from MenuDetails p join p.OutletMenuDetails c where c.outletName = :xyz ").setParameter("xyz", OutletName.OUTLET2);
//            List<MenuDetails> xyx = (List<MenuDetails>) createQuery.getResultList();
//            session.getTransaction().commit();
//            session.close();
//            for(int i = 0;  i < xyx.size(); i++)
//                System.out.println(xyx.get(i).getMenuName());
//
//            session = sessionFactory.openSession();
//            session.beginTransaction();
//            createQuery = session.createQuery(("select t from MenuDetails t where MENU_NAME = :xyz")).setParameter("xyz","HOT_MILK");
//            List<MenuDetails> p = (List<MenuDetails>) createQuery.getResultList();
//            for(int i = 0; i < p.size(); i++) {
//                List<Composition> x = (List<Composition>)p.get(i).getMenuCompositions();
//                System.out.println(x.get(i).getIngredient() + "," + x.get(i).getIngredientQuantity());
//            }
//            session.getTransaction().commit();
//            session.close();
//
//
//            session = sessionFactory.openSession();
//            session.beginTransaction();
//            createQuery = session.createQuery(("select t from OutletDetails t where OUTLET_NAME = :xyz")).setParameter("xyz","OUTLET1");
//            List<OutletDetails> r = (List<OutletDetails>) createQuery.getResultList();
//            List<Composition> x = (List<Composition>)r.get(0).getAvailableComposition();
//            for(int j = 0; j < x.size(); j++) {
//                int val = x.get(j).getIngredientQuantity() - 10;
//                x.get(j).setIngredientQuantity(val);
//            }
//            session.merge(r.get(0));
//            session.getTransaction().commit();
//            session.close();
//
//            session = sessionFactory.openSession();
//            session.beginTransaction();
//            createQuery = session.createQuery(("select t from OutletDetails t where OUTLET_NAME = :xyz")).setParameter("xyz","OUTLET1");
//            List<OutletDetails> q = (List<OutletDetails>) createQuery.getResultList();
//            for(int i = 0; i < q.size(); i++) {
//                List<Composition> o = (List<Composition>)q.get(i).getAvailableComposition();
//                for(int j = 0; j < o.size(); j++) {
//                    System.out.println(j + ":" + o.get(j).getIngredient() + "," + o.get(j).getIngredientQuantity());
//                }
//            }
//            session.getTransaction().commit();
//            session.close();
            IngredientThreshold ingredientTreshold1 = new IngredientThreshold();
            ingredientTreshold1.setTresholdQuantity(10);
            ingredientTreshold1.setIngredientName(Ingredients.HOT_MILK);

            IngredientThreshold ingredientTreshold2 = new IngredientThreshold();
            ingredientTreshold2.setTresholdQuantity(5);
            ingredientTreshold2.setIngredientName(Ingredients.HOT_WATER);

            session = sessionFactoryImpl.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(ingredientTreshold1);
            session.save(ingredientTreshold2);
            session.getTransaction().commit();
            session.close();



            OutletAvailabilityCache outletAvailabilityCache = injector.getInstance(OutletAvailabilityCache.class);
            OutletMenuCache outletMenuCache = injector.getInstance(OutletMenuCache.class);
            IngredientCache ingredientCache = injector.getInstance(IngredientCache.class);
            MenuCompositionCache menuCompositionCache = injector.getInstance(MenuCompositionCache.class);

            outletAvailabilityCache.refreshCache();
            outletMenuCache.refreshCache();
            ingredientCache.refreshCache();
            menuCompositionCache.refreshCache();

            System.out.println("Is Cache loaded: " + menuCompositionCache.isCacheLoaded());
            //coffeeShopController.getOrder("OUTLET1", "HOT_MILK");


//            ServerSocket serverSocket = new ServerSocket(PORT);
//            System.out.println("Server started.\nListening for connections on port : " + PORT + " ...\n");
//
//            while (true) {
//                ClientHandler clientHandler = new ClientHandler(serverSocket.accept());
//
//                if (verbose) {
//                    System.out.println("Connection opened. (" + new Date() + ")");
//                }
//
//                // create dedicated thread to manage the client connection
//                threadPool.execute(clientHandler);
//            }
        } catch (Exception e) {
            System.err.println("Server Connection error : " + e.getMessage());
        }
    }
}
