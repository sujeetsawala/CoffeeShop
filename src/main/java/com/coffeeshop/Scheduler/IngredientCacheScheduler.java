package com.coffeeshop.Scheduler;

import com.coffeeshop.Cache.IngredientCache;
import com.coffeeshop.Cache.OutletMenuCache;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.TimerTask;

@Singleton
@Getter
public class IngredientCacheScheduler extends TimerTask {
    private IngredientCache ingredientCache;
    private final int PERIOD_MS = 60000;
    private final int DELAY_MS = 1000;

    @Inject
    public IngredientCacheScheduler(IngredientCache ingredientCache) {
        this.ingredientCache = ingredientCache;
    }
    @Override
    public void run() {
        LocalDateTime localTime = LocalDateTime.now();
        System.out.println("Run TimerJob at " + localTime.toString());

        this.ingredientCache.refreshCache();

    }
}
