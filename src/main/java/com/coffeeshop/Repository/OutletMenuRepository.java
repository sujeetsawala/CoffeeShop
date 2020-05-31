package com.coffeeshop.Repository;


import com.coffeeshop.Pojos.*;
import com.coffeeshop.SessionFactory.SessionFactoryImpl;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.hibernate.Session;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class OutletMenuRepository {

    private SessionFactoryImpl sessionFactoryImpl;

    @Inject
    public OutletMenuRepository(SessionFactoryImpl sessionFactoryImpl) {
        this.sessionFactoryImpl = sessionFactoryImpl;
    }

    public List<Menus> findMenuForOutletName(OutletName outletName) {
        Session session = this.sessionFactoryImpl.getSessionFactory().openSession();
        session.beginTransaction();
        Query query = session.createQuery("select p from MenuDetails p join p.OutletMenuDetails c where c.outletName = :outletName ").setParameter("outletName", outletName);
        List<MenuDetails> menuDetails = (List<MenuDetails>) query.getResultList();
        session.getTransaction().commit();
        session.close();

        List<Menus> menus = new ArrayList<>();
        for(int i = 0; i < menuDetails.size(); i++)
            menus.add(menuDetails.get(i).getMenuName());
        return menus;
    }

    public synchronized void addOutletMenuDetails(List<OutletMenuDetails> outletMenuDetailsList) {
        Session session = this.sessionFactoryImpl.getSessionFactory().openSession();
        session.beginTransaction();
        for(OutletMenuDetails outletMenuDetails: outletMenuDetailsList) {
            session.persist(outletMenuDetails);
        }
        session.getTransaction().commit();
        session.close();
    }
}
