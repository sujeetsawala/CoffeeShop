package com.coffeeshop.Repository;

import com.coffeeshop.Pojos.Composition;
import com.coffeeshop.Pojos.MenuDetails;
import com.coffeeshop.Pojos.Menus;
import com.coffeeshop.SessionFactory.SessionFactoryImpl;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.hibernate.Session;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class MenuRepository {
    private SessionFactoryImpl sessionFactoryImpl;

    @Inject
    public MenuRepository(SessionFactoryImpl sessionFactoryImpl) {
        this.sessionFactoryImpl = sessionFactoryImpl;
    }

    public List<Menus> getAllMenus() {
        Session session = this.sessionFactoryImpl.getSessionFactory().openSession();
        session.beginTransaction();
        Query query = session.createQuery(("select t from MenuDetails t"), MenuDetails.class);
        List<MenuDetails> menuDetails = (List<MenuDetails>)query.getResultList();
        List<Menus> menus = new ArrayList<>();
        for(MenuDetails menuDetail: menuDetails) {
            menus.add(menuDetail.getMenuName());
        }
        session.getTransaction().commit();
        session.close();

        return menus;
    }

    public List<Composition> findMenuCompositionByMenu(String menuName) {
        Session session = this.sessionFactoryImpl.getSessionFactory().openSession();
        session.beginTransaction();
        Query query = session.createQuery(("select t from MenuDetails t join fetch t.menuCompositions where MENU_NAME = :menuName"), MenuDetails.class).setParameter("menuName", menuName);
        List<MenuDetails> menuDetails = (List<MenuDetails>)query.getResultList();
        List<Composition> compositions = new ArrayList<>();
        compositions = (List<Composition>)menuDetails.get(0).getMenuCompositions();
        session.getTransaction().commit();
        session.close();

        return compositions;
    }

    public void addMenuDetails(MenuDetails menuDetails) {
        Session session = this.sessionFactoryImpl.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(menuDetails);
        session.getTransaction().commit();
        session.close();
    }

}
