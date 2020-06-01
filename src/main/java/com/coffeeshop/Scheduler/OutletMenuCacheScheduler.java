package com.coffeeshop.Scheduler;

import com.coffeeshop.Cache.OutletMenuCache;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.TimerTask;

@Singleton
@Getter
public class OutletMenuCacheScheduler extends TimerTask {
    private OutletMenuCache outletMenuCache;
    private final int PERIOD_MS = 60000;
    private final int DELAY_MS = 1000;

    @Inject
    public OutletMenuCacheScheduler(OutletMenuCache outletMenuCache) {
        this.outletMenuCache = outletMenuCache;
    }
    @Override
    public void run() {
        LocalDateTime localTime = LocalDateTime.now();
        System.out.println("Run TimerJob at " + localTime.toString());

        this.outletMenuCache.refreshCache();

    }
}
