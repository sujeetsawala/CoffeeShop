package com.coffeeshop.Controller;

import com.coffeeshop.Cache.IngredientCache;
import com.coffeeshop.Cache.MenuCompositionCache;
import com.coffeeshop.Cache.OutletAvailabilityCache;
import com.coffeeshop.Cache.OutletMenuCache;
import com.coffeeshop.Pojos.*;
import com.coffeeshop.Repository.IngredientThresholdRepository;
import com.coffeeshop.Repository.MenuRepository;
import com.coffeeshop.Repository.OutletMenuRepository;
import com.coffeeshop.Repository.OutletRepository;
import com.coffeeshop.SessionFactory.SessionFactoryImpl;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class StockController {
    private SessionFactoryImpl sessionFactoryImpl;

    private IngredientThresholdRepository ingredientThresholdRepository;

    private MenuRepository menuRepository;

    private OutletRepository outletRepository;

    private OutletMenuRepository outletMenuRepository;

    @Inject
    public StockController(SessionFactoryImpl sessionFactoryImpl,
                                IngredientThresholdRepository ingredientThresholdRepository,
                                MenuRepository menuRepository,
                                OutletRepository outletRepository,
                                OutletMenuRepository outletMenuRepository
                                ) {
        this.sessionFactoryImpl = sessionFactoryImpl;
        this.ingredientThresholdRepository = ingredientThresholdRepository;
        this.menuRepository = menuRepository;
        this.outletRepository = outletRepository;
        this.outletMenuRepository = outletMenuRepository;

    }

    public void init() {
        List<Composition> compositions = new ArrayList<>();
        Composition composition1 = new Composition();
        composition1.setIngredient(Ingredients.HOT_MILK);
        composition1.setIngredientQuantity(20);

        Composition composition2 = new Composition();
        composition2.setIngredient(Ingredients.HOT_WATER);
        composition2.setIngredientQuantity(15);

        Composition composition3 = new Composition();
        composition3.setIngredient(Ingredients.TEA_LEAVES);
        composition3.setIngredientQuantity(10);

        compositions.add(composition1);
        compositions.add(composition2);
        compositions.add(composition3);

        List<Composition> availableCompositions = new ArrayList<>();
        Composition availableComposition1 = new Composition();
        availableComposition1.setIngredient(Ingredients.HOT_MILK);
        availableComposition1.setIngredientQuantity(200);

        Composition availableComposition2 = new Composition();
        availableComposition2.setIngredient(Ingredients.HOT_WATER);
        availableComposition2.setIngredientQuantity(150);

        Composition availableComposition3 = new Composition();
        availableComposition3.setIngredient(Ingredients.TEA_LEAVES);
        availableComposition3.setIngredientQuantity(100);

        availableCompositions.add(availableComposition1);
        availableCompositions.add(availableComposition2);
        availableCompositions.add(availableComposition3);

        List<String> menuNames = new ArrayList<>();
        menuNames.add(Menus.HOT_COFFEE.toString());
        menuNames.add(Menus.HOT_TEA.toString());
        menuNames.add(Menus.GINGER_TEA.toString());
        menuNames.add(Menus.HOT_MILK.toString());

        List<String> outletNames = new ArrayList<>();
        outletNames.add(OutletName.OUTLET1.toString());
        outletNames.add(OutletName.OUTLET2.toString());
        outletNames.add(OutletName.OUTLET3.toString());
        outletNames.add(OutletName.OUTLET4.toString());
        outletNames.add(OutletName.OUTLET5.toString());
        outletNames.add(OutletName.OUTLET6.toString());

        this.AddMenusToOutlets(menuNames, compositions, outletNames);
        this.addIngredientsToOutlet(OutletName.OUTLET1.toString(), availableCompositions);
        this.addIngredientsToOutlet(OutletName.OUTLET2.toString(), compositions);
        this.addIngredientsToOutlet(OutletName.OUTLET3.toString(), availableCompositions);
        this.addIngredientsToOutlet(OutletName.OUTLET4.toString(), compositions);
        this.addIngredientsToOutlet(OutletName.OUTLET5.toString(), availableCompositions);

        this.addIngredientThreshold(Ingredients.HOT_MILK.toString(), 20);
        this.addIngredientThreshold(Ingredients.HOT_WATER.toString(), 10);
        this.addIngredientThreshold(Ingredients.TEA_LEAVES.toString(), 15);
    }

    public void AddMenusToOutlets(List<String> menuNames, List<Composition> menuComposition, List<String> outlets) {
        List<MenuDetails> menuDetailsList = new ArrayList<>();
        List<OutletMenuDetails> outletMenuDetailsList = new ArrayList<>();
        for(String menuName: menuNames) {
            MenuDetails menuDetails = new MenuDetails();
            menuDetails.setMenuName(Menus.valueOf(menuName));
            menuDetails.setMenuCompositions(menuComposition);
            menuDetailsList.add(menuDetails);
        }

        for(String outlet: outlets) {
            OutletMenuDetails outletMenuDetails = new OutletMenuDetails();
            outletMenuDetails.setOutletName(OutletName.valueOf(outlet));
            outletMenuDetailsList.add(outletMenuDetails);
        }

        for(OutletMenuDetails outletMenuDetails: outletMenuDetailsList) {
            for(MenuDetails menuDetails: menuDetailsList) {
                outletMenuDetails.getMenuDetails().add(menuDetails);
            }
        }
        this.outletMenuRepository.addOutletMenuDetails(outletMenuDetailsList);

    }

    public void addIngredientsToOutlet(String outletName, List<Composition> compositions) {
        OutletDetails outletDetails = new OutletDetails();
        outletDetails.setOutletName(OutletName.valueOf(outletName));
        outletDetails.setAvailableComposition(compositions);
        this.outletRepository.addOutletAvailabiltyDetails(outletDetails);
    }

    public void updateIngredientsToOutlet(String outletName, List<Composition> compositions) {
        try {
            if(this.outletRepository.isOutletPresent(outletName)) {
                List<Composition> outletAvailability = this.outletRepository.getAvailableCompositionForOutlet(outletName);
                List<Composition> updatedOutletAvailability = this.addIngredientQuantity(outletAvailability, compositions);
                this.outletRepository.updateAvailableCompositionForOutlet(outletName, updatedOutletAvailability);
            }
            else {
                this.addIngredientsToOutlet(outletName, compositions);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }

    public void addIngredientThreshold(String ingredient, Integer ingredientQuantity) {
        IngredientThreshold ingredientThreshold = new IngredientThreshold();
        ingredientThreshold.setIngredientName(Ingredients.valueOf(ingredient));
        ingredientThreshold.setTresholdQuantity(ingredientQuantity);
        this.ingredientThresholdRepository.addIngredientThreshold(ingredientThreshold);
    }


    private List<Composition> addIngredientQuantity(List<Composition> outletAvailableComposition, List<Composition> updatedComposition) {
        List<Composition> finalComposition = new ArrayList<>();
        for (int j = 0; j < outletAvailableComposition.size(); j++) {
            Ingredients outletIngredient = outletAvailableComposition.get(j).getIngredient();
            boolean foundFlag = true;
            for (int i = 0; i < updatedComposition.size(); i++) {
                Ingredients updatedIngredient = updatedComposition.get(i).getIngredient();
                if(outletIngredient != null && updatedIngredient != null && outletIngredient == updatedIngredient) {
                    int ingredientQuantity = outletAvailableComposition.get(j).getIngredientQuantity() + updatedComposition.get(i).getIngredientQuantity();
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
}
