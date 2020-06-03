package com.coffeeshop.Cache;

import com.coffeeshop.Pojos.Composition;
import com.coffeeshop.Pojos.Menus;
import com.coffeeshop.Pojos.OutletName;
import com.coffeeshop.Repository.IngredientThresholdRepository;
import com.coffeeshop.Repository.OutletMenuRepository;
import com.coffeeshop.Repository.OutletRepository;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.Getter;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Singleton
@Getter
public class OutletMenuCache {
    private ConcurrentMap<OutletName, List<Menus>> outletMenuCache = new ConcurrentHashMap<>();


    private OutletMenuRepository outletMenuRepository;
    private OutletRepository outletRepository;

    @Inject
    public OutletMenuCache(IngredientThresholdRepository ingredientThresholdRepository, OutletMenuRepository outletMenuRepository, OutletRepository outletRepository) {
        this.outletMenuRepository = outletMenuRepository;
        this.outletRepository = outletRepository;
    }

    public boolean isCacheLoaded() {
       return !outletMenuCache.isEmpty();
    }

    public void refreshCache() {
        System.out.println("OutletMenuCache is loading");
        List<OutletName> outletNames = this.outletRepository.getAllOutlets();
        for(OutletName outletName: outletNames) {
            List<Menus> menus = this.outletMenuRepository.findMenuForOutletName(outletName);
            this.outletMenuCache.put(outletName, menus);
        }
    }

    public List<Menus> get(OutletName key) {
        return this.outletMenuCache.get(key);
    }

}
