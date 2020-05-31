package com.coffeeshop.Cache;

import com.coffeeshop.Pojos.Composition;
import com.coffeeshop.Pojos.Menus;
import com.coffeeshop.Pojos.OutletName;
import com.coffeeshop.Repository.IngredientThresholdRepository;
import com.coffeeshop.Repository.MenuRepository;
import com.coffeeshop.Repository.OutletMenuRepository;
import com.coffeeshop.Repository.OutletRepository;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.springframework.scheduling.annotation.Scheduled;

import java.awt.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Singleton
public class MenuCompositionCache {
    private ConcurrentMap<Menus, List<Composition>> menuCompositionCache = new ConcurrentHashMap<>();


    private MenuRepository menuRepository;

    @Inject
    public MenuCompositionCache(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public boolean isCacheLoaded() {
        return !menuCompositionCache.isEmpty();
    }

    public void refreshCache() {
        System.out.println("MenuCompositionCache is loading");
        List<Menus> menus = this.menuRepository.getAllMenus();
        for(Menus menu: menus) {
            List<Composition> compositions = this.menuRepository.findMenuCompositionByMenu(menu.toString());
            this.menuCompositionCache.put(menu, compositions);
        }
    }

    public List<Composition> get(Menus key) {
        return this.menuCompositionCache.get(key);
    }
}
