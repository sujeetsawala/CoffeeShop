package com.coffeeshop.Scheduler;

import com.coffeeshop.Cache.IngredientCache;
import com.coffeeshop.Cache.MenuCompositionCache;
import com.coffeeshop.Cache.OutletAvailabilityCache;
import com.coffeeshop.Cache.OutletMenuCache;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.TimerTask;

@Singleton
@Getter
public class MenuCompositionCacheSchedular extends TimerTask {

    private MenuCompositionCache menuCompositionCache;
    private final int PERIOD_MS = 60 * 60 * 1000;
    private final int DELAY_MS = 5;

    @Inject
    public MenuCompositionCacheSchedular(MenuCompositionCache menuCompositionCache) {
        this.menuCompositionCache = menuCompositionCache;
    }
    @Override
    public void run() {
        LocalDateTime localTime = LocalDateTime.now();
        System.out.println("Run TimerJob at " + localTime.toString());

        this.menuCompositionCache.refreshCache();

    }
}
