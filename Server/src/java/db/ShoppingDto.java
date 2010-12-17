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
public class ShoppingDto {
    private int id;
    private String owner;
    private String currency;
    private int amount;
    private SimpleDerbyDB sd;

    public ShoppingDto(int id, String owner, String currency, int amount) {
        this.id = id;
        this.owner = owner;
        this.currency = currency;
        this.amount = amount;
    }

    public ShoppingDto(String owner, String currency, int amount) {
        initDB();
        this.owner = owner;
        this.currency = currency;
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String toString(){
         return "<buy>"
                + "<id>"+id+"</id>"
                + "<owner>"+ owner+"</owner>"
                + "<currency>"+currency+"</currency>"
                + "<amount>"+amount+"</amout>"
                + "</buy>";
    }

    private void initDB(){
        try {
            sd = new SimpleDerbyDB();
        } catch (SQLException ex) {
            Logger.getLogger(RateResource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}
