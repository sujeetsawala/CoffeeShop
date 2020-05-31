package com.coffeeshop.Cache;

import com.coffeeshop.Pojos.Composition;
import com.coffeeshop.Pojos.IngredientThreshold;
import com.coffeeshop.Pojos.Ingredients;
import com.coffeeshop.Pojos.Menus;
import com.coffeeshop.Repository.IngredientThresholdRepository;
import com.coffeeshop.Repository.MenuRepository;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Singleton
public class IngredientCache {
    private static final int CACHE_REFRESH_TIME = 57 * 60 * 1000;
    private ConcurrentMap<Ingredients, Integer> ingredientCache = new ConcurrentHashMap<>();

    private IngredientThresholdRepository ingredientThresholdRepository;

    @Inject
    public IngredientCache(IngredientThresholdRepository ingredientThresholdRepository) {
        this.ingredientThresholdRepository = ingredientThresholdRepository;
    }

    public boolean isCacheLoaded() {
        return !ingredientCache.isEmpty();
    }

    @Scheduled(fixedDelay = CACHE_REFRESH_TIME, initialDelay = 5)
    public void refreshCache() {
        List<IngredientThreshold> ingredientThresholdList = this.ingredientThresholdRepository.getAllIngredientThreshold();
        for(IngredientThreshold ingredientThreshold: ingredientThresholdList) {
            ingredientCache.put(ingredientThreshold.getIngredientName(), ingredientThreshold.getTresholdQuantity());
        }
    }

    public Integer get(Ingredients key) {
        return this.ingredientCache.get(key);
    }
}