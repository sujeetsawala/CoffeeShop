package com.coffeeshop.Repository;

import com.coffeeshop.Pojos.IngredientThreshold;
import com.coffeeshop.SessionFactory.SessionFactoryImpl;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.hibernate.Session;

import javax.persistence.Query;
import java.util.List;

@Singleton
public class IngredientThresholdRepository {
    private SessionFactoryImpl sessionFactoryImpl;

    @Inject
    public IngredientThresholdRepository(SessionFactoryImpl sessionFactoryImpl) {
        this.sessionFactoryImpl = sessionFactoryImpl;
    }

    public List<IngredientThreshold> getAllIngredientThreshold() {
        Session session = this.sessionFactoryImpl.getSessionFactory().openSession();
        session.beginTransaction();
        Query query = session.createQuery(("select t from IngredientThreshold t"), IngredientThreshold.class);
        List<IngredientThreshold> ingredientThresholdList = (List<IngredientThreshold>)query.getResultList();
        session.getTransaction().commit();
        session.close();

        return ingredientThresholdList;

    }

    public synchronized IngredientThreshold addIngredientThreshold(IngredientThreshold ingredientThreshold) {
        Session session = this.sessionFactoryImpl.getSessionFactory().openSession();
        session.beginTransaction();
        Query query = session.createQuery(("select t from IngredientThreshold t where INGREDIENT = :ingredient"), IngredientThreshold.class).setParameter("ingredient", ingredientThreshold.getIngredientName().toString());
        List<IngredientThreshold> ingredientThresholdList = (List<IngredientThreshold>)query.getResultList();
        if(!ingredientThresholdList.isEmpty())
            ingredientThresholdList.get(0).setTresholdQuantity(ingredientThreshold.getTresholdQuantity());
        else
            ingredientThresholdList.add(ingredientThreshold);
        session.save(ingredientThresholdList.get(0));
        session.getTransaction().commit();
        session.close();

        return ingredientThreshold;

    }

    public IngredientThreshold getIngredientThresholdByIngredient(String ingredient) {
        Session session = this.sessionFactoryImpl.getSessionFactory().openSession();
        session.beginTransaction();
        Query query = session.createQuery(("select t from IngredientThreshold t where INGREDIENT = :ingredient"), IngredientThreshold.class).setParameter("ingredient", ingredient);
        List<IngredientThreshold> ingredientThresholdList = (List<IngredientThreshold>)query.getResultList();
        IngredientThreshold ingredientThreshold = ingredientThresholdList.get(0);
        session.getTransaction().commit();
        session.close();

        return ingredientThreshold;

    }
}
