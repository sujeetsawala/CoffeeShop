package com.coffeeshop.SessionFactory;

import com.google.inject.Singleton;
import lombok.Getter;
import org.hibernate.cfg.Configuration;

@Getter
@Singleton
public class SessionFactoryImpl {

    private org.hibernate.SessionFactory sessionFactory;
    public SessionFactoryImpl() {
        this.sessionFactory = new Configuration().configure().buildSessionFactory();
    }
}
