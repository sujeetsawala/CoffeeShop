package com.coffeeshop.Cache;

import com.coffeeshop.Pojos.Composition;
import com.coffeeshop.Pojos.Menus;
import com.coffeeshop.Pojos.OutletName;
import com.coffeeshop.Repository.MenuRepository;
import com.coffeeshop.Repository.OutletRepository;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Singleton
public class OutletAvailabilityCache {
    private ConcurrentMap<String, List<Composition>> outletAvailabilityCache = new ConcurrentHashMap<>();
    private OutletRepository outletRepository;

    @Inject
    public OutletAvailabilityCache(OutletRepository outletRepository)  {
        this.outletRepository = outletRepository;
    }

    public boolean isCacheLoaded() {
        return !outletAvailabilityCache.isEmpty();
    }

    public void refreshCache() {
        List<OutletName> outlets = this.outletRepository.getAllOutlets();
        for(OutletName outlet: outlets) {
            List<Composition> compositions = this.outletRepository.getAvailableCompositionForOutlet(outlet.toString());
            outletAvailabilityCache.put(outlet.toString(), compositions);
        }

    }

    public void add(String key, List<Composition> value) {
        if (key == null) {
            return;
        }
        if (value == null || value.size() == 0) {
            this.outletAvailabilityCache.remove(key);
        } else {
            outletAvailabilityCache.put(key, value);
        }
    }


    public List<Composition> get(String key) {
        return this.outletAvailabilityCache.get(key);
    }

}
