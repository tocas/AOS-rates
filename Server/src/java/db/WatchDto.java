/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package db;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import rate.RateResource;


/**
 *
 * @author tocas
 */
public class WatchDto {
    private int id;
    private String owner;
    private String currency;
    private float watchrate;
    private SimpleDerbyDB sd;

    public WatchDto(int id, String owner, String currency, float watchrate) {
        this.id = id;
        this.owner = owner;
        this.currency = currency;
        this.watchrate = watchrate;
    }

    public WatchDto(String owner, String currency, float watchrate) {
        initDB();
        this.id = sd.nextIdWatch();
        this.owner = owner;
        this.currency = currency;
        this.watchrate = watchrate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public float getWatchrate() {
        return watchrate;
    }

    public void setWatchrate(float watchrate) {
        this.watchrate = watchrate;
    }
    @Override
    public String toString(){
        return "<watch>"
                + "<id>"+id+"</id>"
                + "<user>"+ owner+"</user>"
                + "<currency>"+currency+"</currency>"
                + "<watchrate>"+watchrate+"</watchrate>"
                + "</watch>";
    }
    private void initDB(){
        try {
            sd = new SimpleDerbyDB();
        } catch (SQLException ex) {
            Logger.getLogger(RateResource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}
