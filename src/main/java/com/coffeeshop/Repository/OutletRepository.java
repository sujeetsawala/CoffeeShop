package com.coffeeshop.Repository;

import com.coffeeshop.Pojos.Composition;
import com.coffeeshop.Pojos.OutletDetails;
import com.coffeeshop.Pojos.OutletName;
import com.coffeeshop.SessionFactory.SessionFactoryImpl;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.hibernate.Session;

import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class OutletRepository {
    private SessionFactoryImpl sessionFactoryImpl;

    @Inject
    public OutletRepository(SessionFactoryImpl sessionFactoryImpl) {
        this.sessionFactoryImpl = sessionFactoryImpl;
    }

    public List<OutletName> getAllOutlets() {
        try {
            Session session = this.sessionFactoryImpl.getSessionFactory().openSession();
            session.beginTransaction();
            Query query = session.createQuery("select p from OutletDetails p", OutletDetails.class);
            List<OutletDetails> outletDetails = (List<OutletDetails>) query.getResultList();
            session.getTransaction().commit();
            session.close();
            List<OutletName> OutletName = new ArrayList<>();
            for (int i = 0; i < outletDetails.size(); i++)
                OutletName.add(outletDetails.get(i).getOutletName());

            return OutletName;
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean isOutletPresent(String outletName) throws Exception {
        try {
            Session session = this.sessionFactoryImpl.getSessionFactory().openSession();
            session.beginTransaction();
            Query query = session.createQuery("select p from OutletDetails p where outletName :outletName", OutletDetails.class).setParameter("outletName", outletName);
            List<OutletDetails> outletDetails = (List<OutletDetails>) query.getResultList();
            session.getTransaction().commit();
            session.close();

            if(outletDetails.size() > 0)
                return true;
            return false;
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            throw e;
        }
    }

    public List<Composition> getAvailableCompositionForOutlet(String outletName) {
       try {
           Session session = this.sessionFactoryImpl.getSessionFactory().openSession();
           session.beginTransaction();
           Query query = session.createQuery(("select t from OutletDetails t join fetch t.availableComposition where OUTLET_NAME = :outletName"), OutletDetails.class).setParameter("outletName", outletName);
           List<OutletDetails> outletDetails = (List<OutletDetails>) query.getResultList();
           session.getTransaction().commit();
           session.close();
           if(outletDetails != null)
               return (List<Composition>) outletDetails.get(0).getAvailableComposition();
           return new ArrayList<>();
       } catch (Exception e) {
           System.err.println(e.getMessage());
           return new ArrayList<>();
       }

    }

    public synchronized void updateAvailableCompositionForOutlet(String outletName, List<Composition> updateComposition) {
        try {
            Session session = this.sessionFactoryImpl.getSessionFactory().openSession();
            session.beginTransaction();
            Query query = session.createQuery(("select t from OutletDetails t where OUTLET_NAME = :outletName"), OutletDetails.class).setParameter("outletName", outletName);
            List<OutletDetails> outletDetails = (List<OutletDetails>) query.getResultList();
            outletDetails.get(0).setAvailableComposition(updateComposition);
            session.merge(outletDetails.get(0));
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public synchronized OutletDetails addOutletAvailabiltyDetails(OutletDetails outletDetails) {
        Session session = this.sessionFactoryImpl.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(outletDetails);
        session.getTransaction().commit();
        session.close();
        return outletDetails;
    }

}
