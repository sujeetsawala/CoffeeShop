package com.coffeeshop.Controller;

import com.coffeeshop.Cache.MenuCompositionCache;
import com.coffeeshop.Cache.OutletMenuCache;
import com.coffeeshop.Cache.IngredientCache;
import com.coffeeshop.Cache.OutletAvailabilityCache;
import com.coffeeshop.Pojos.Composition;
import com.coffeeshop.Pojos.Ingredients;
import com.coffeeshop.Pojos.Menus;
import com.coffeeshop.Pojos.OutletName;
import com.coffeeshop.Repository.IngredientThresholdRepository;
import com.coffeeshop.Repository.MenuRepository;
import com.coffeeshop.Repository.OutletRepository;
import com.coffeeshop.Repository.OutletMenuRepository;
import com.coffeeshop.SessionFactory.SessionFactoryImpl;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Singleton
public class CoffeeShopController {
    /**
     *  All methods to coffeshop will be decalred here
     *
     */

    private SessionFactoryImpl sessionFactoryImpl;

    private OutletMenuCache outletMenuCache;

    private IngredientCache ingredientCache;

    private OutletAvailabilityCache outletAvailabilityCache;

    private MenuCompositionCache menuCompositionCache;

    private IngredientThresholdRepository ingredientThresholdRepository;

    private MenuRepository menuRepository;

    private OutletRepository outletRepository;

    private OutletMenuRepository outletMenuRepository;

    @Inject
    public CoffeeShopController(SessionFactoryImpl sessionFactoryImpl,
                                OutletMenuCache outletMenuCache,
                                IngredientCache ingredientCache,
                                OutletAvailabilityCache outletAvailabilityCache,
                                IngredientThresholdRepository ingredientThresholdRepository,
                                MenuRepository menuRepository,
                                OutletRepository outletRepository,
                                OutletMenuRepository outletMenuRepository,
                                MenuCompositionCache menuCompositionCache) {
        this.sessionFactoryImpl = sessionFactoryImpl;
        this.outletMenuCache = outletMenuCache;
        this.ingredientCache = ingredientCache;
        this.outletAvailabilityCache = outletAvailabilityCache;
        this.ingredientThresholdRepository = ingredientThresholdRepository;
        this.menuRepository = menuRepository;
        this.outletRepository = outletRepository;
        this.outletMenuRepository = outletMenuRepository;
        this.menuCompositionCache = menuCompositionCache;

    }


    public List<String> getAllOutlets() {
        List<OutletName> outletNames =  this.outletAvailabilityCache.getKeys();
        List<String> output = new ArrayList<>();
        if(outletNames != null) {
            for(OutletName outletName : outletNames) {
                output.add(outletName.toString());
            }
        }
        return output;
    }
    public List<String> getMenuForOutlet(String outletName) {
        List<Menus> menus =  this.outletMenuCache.get(OutletName.valueOf(outletName));
        List<String> output = new ArrayList<>();
        if(menus != null) {
            for(Menus menu : menus) {
                output.add(menu.toString());
            }
        }

        return output;
    }

    public String getOrder(String outletName, String menuName) {
        try {
            List<Menus> menus = this.outletMenuCache.get(OutletName.valueOf(outletName));
            if(menus.contains(Menus.valueOf(menuName))) {
                List<Composition> menuComposition = this.menuCompositionCache.get(Menus.valueOf(menuName));
                List<Composition> outletAvailability = this.outletAvailabilityCache.getValue(OutletName.valueOf(outletName));
                if(menuComposition.size() > 0 && this.checkIfIngredientsAvailable(OutletName.valueOf(outletName), outletAvailability, menuComposition)) {
                    List<Composition> updatedOutletAvailability = this.removeIngredientQuantity(outletAvailability, menuComposition);
                    this.outletRepository.updateAvailableCompositionForOutlet(outletName, updatedOutletAvailability);
                    this.outletAvailabilityCache.add(OutletName.valueOf(outletName), updatedOutletAvailability);
                    return "Order Accepted";
                }
                else
                    return "Out Of Stock";
            }
            else {
                return "Menu Item not Available at this outlet";
            }
        } catch (Exception e) {
            System.err.println("Unable to process the order: " + e.getMessage());
            return "Error while processing the order";
        }
    }

    private List<Composition> removeIngredientQuantity(List<Composition> outletAvailableComposition, List<Composition> updatedComposition) {
        List<Composition> finalComposition = new ArrayList<>();
        for (int j = 0; j < outletAvailableComposition.size(); j++) {
            Ingredients outletIngredient = outletAvailableComposition.get(j).getIngredient();
            boolean foundFlag = true;
             for (int i = 0; i < updatedComposition.size(); i++) {
                Ingredients updatedIngredient = updatedComposition.get(i).getIngredient();
                if(outletIngredient != null && updatedIngredient != null && outletIngredient == updatedIngredient) {
                    int ingredientQuantity = outletAvailableComposition.get(j).getIngredientQuantity() - updatedComposition.get(i).getIngredientQuantity();
                    Composition composition = new Composition();
                    composition.setIngredient(outletIngredient);
                    composition.setIngredientQuantity(ingredientQuantity);
                    finalComposition.add(composition);
                    foundFlag = false;
                }
            }
            if(foundFlag)
                finalComposition.add(outletAvailableComposition.get(j));
        }
        return finalComposition;
    }

    private boolean checkIfIngredientsAvailable(OutletName outletName, List<Composition> outletAvailability, List<Composition> menuComposition) {
        boolean checkFlag = true;
        for(int i = 0; i < menuComposition.size(); i++)
        {
            boolean foundFlag =  false;
            for(int j = 0; j < outletAvailability.size(); j++)
            {
                Ingredients menuIngredient = menuComposition.get(i).getIngredient();
                Ingredients outletIngredient = outletAvailability.get(j).getIngredient();
                if(outletIngredient != null && menuIngredient != null && menuIngredient == outletIngredient ) {
                    foundFlag = true;
                    Integer ingredientThreshold = this.ingredientCache.get(menuIngredient);
                    if(outletAvailability.get(j).getIngredientQuantity() < ingredientThreshold)
                        System.out.println("Ingredient " + menuIngredient.toString() + " running low at Outlet " + outletName.toString());
                    if(outletAvailability.get(j).getIngredientQuantity() < menuComposition.get(i).getIngredientQuantity()) {
                        checkFlag = false;
                    }
                }
            }
            checkFlag = checkFlag && foundFlag;
        }
        return checkFlag;
    }
}
