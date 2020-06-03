package com.coffeeshop.Scheduler;

import com.coffeeshop.Cache.OutletAvailabilityCache;
import com.coffeeshop.Cache.OutletMenuCache;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.TimerTask;

@Singleton
@Getter
public class OutletAvailabilityCacheScheduler extends TimerTask {
    private OutletAvailabilityCache outletAvailabilityCache;
    private final int DELAY_MS = 5;

    @Inject
    public OutletAvailabilityCacheScheduler(OutletAvailabilityCache outletAvailabilityCache) {
        this.outletAvailabilityCache = outletAvailabilityCache;
    }
    @Override
    public void run() {
        LocalDateTime localTime = LocalDateTime.now();
        System.out.println("Run TimerJob at " + localTime.toString());

        this.outletAvailabilityCache.refreshCache();

    }
}
