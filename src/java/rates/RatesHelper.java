/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rates;

import java.util.List;
import java.util.Random;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author tocas
 */
public class RatesHelper {
    Session session = null;

    public RatesHelper(){
        this.session = HibernateUtil.getSessionFactory().getCurrentSession();
    }

    public List getRates(String date){
        List<Rates> ratesList = null;
        Transaction tx = session.beginTransaction();
        Query q = session.createQuery("from Rates where date='"+date+"'");
        ratesList = (List<Rates>) q.list();
        return ratesList;
    }

    public void putRates(String date, String bank, String currency, Float rate){
        Transaction tx = session.beginTransaction();

        Random rand = new Random(System.currentTimeMillis());
        Rates rates = new Rates(rand.nextInt(),date, bank, currency, rate);
        session.save(rates);
        session.flush();
        session.clear();
        tx.commit();
    }

}
