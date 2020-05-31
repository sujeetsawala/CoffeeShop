package com.coffeeshop.Cache;

import com.coffeeshop.Pojos.Composition;
import com.coffeeshop.Pojos.Menus;
import com.coffeeshop.Pojos.OutletName;
import com.coffeeshop.Repository.IngredientThresholdRepository;
import com.coffeeshop.Repository.OutletMenuRepository;
import com.coffeeshop.Repository.OutletRepository;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Singleton
public class OutletMenuCache {
    private static final int CACHE_REFRESH_TIME = 57 * 60 * 1000;
    private ConcurrentMap<String, List<Menus>> outletMenuCache = new ConcurrentHashMap<>();


    private OutletMenuRepository outletMenuRepository;
    private OutletRepository outletRepository;

    @Inject
    public OutletMenuCache(IngredientThresholdRepository ingredientThresholdRepository, OutletMenuRepository outletMenuRepository, OutletRepository outletRepository) {
        this.outletMenuRepository = outletMenuRepository;
        this.outletRepository = outletRepository;
    }

    public boolean isCacheLoaded() {
       return  !outletMenuCache.isEmpty();
    }

    @Scheduled(fixedDelay = CACHE_REFRESH_TIME, initialDelay = 5)
    public void refreshCache() {
        List<OutletName> outletNames = this.outletRepository.getAllOutlets();
        for(OutletName outletName: outletNames) {
            List<Menus> menus = this.outletMenuRepository.findMenuForOutletName(outletName);
            this.outletMenuCache.put(outletName.toString(), menus);
        }
    }

    public List<Menus> get(String key) {
        return this.outletMenuCache.get(key);
    }
}
