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
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Singleton
public class OutletAvailabilityCache {
    private ConcurrentMap<OutletName, List<Composition>> outletAvailabilityCache = new ConcurrentHashMap<>();
    private OutletRepository outletRepository;

    @Inject
    public OutletAvailabilityCache(OutletRepository outletRepository)  {
        this.outletRepository = outletRepository;
    }

    public boolean isCacheLoaded() {
        return !outletAvailabilityCache.isEmpty();
    }

    public void refreshCache() {
        System.out.println("OutletAvailabilityCache is loading: ");
        List<OutletName> outlets = this.outletRepository.getAllOutlets();
        for(OutletName outlet: outlets) {
            List<Composition> compositions = this.outletRepository.getAvailableCompositionForOutlet(outlet.toString());
            outletAvailabilityCache.put(outlet, compositions);
        }
    }

    public void add(OutletName key, List<Composition> value) {
        if (key == null) {
            return;
        }
        if (value == null || value.size() == 0) {
            this.outletAvailabilityCache.remove(key);
        } else {
            outletAvailabilityCache.put(key, value);
        }
    }


    public List<Composition> getValue(OutletName key) {
        return this.outletAvailabilityCache.get(key);
    }

    public List<OutletName> getKeys() {
        Set<OutletName> outletNames = this.outletAvailabilityCache.keySet();
        return outletNames.stream().collect(Collectors.toList());
    }


}
