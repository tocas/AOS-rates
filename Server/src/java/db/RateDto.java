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
public class RateDto {
    private int id;
    private String date;
    private String bank;
    private String currency;
    private Float rate;
    private SimpleDerbyDB sd;

    public RateDto(String date, String bank, String currency, Float rate) {
        initDB();
        this.id = sd.nextId();
        this.date = date;
        this.bank = bank;
        this.currency = currency;
        this.rate = rate;
    }

    public RateDto(int id, String date, String bank, String currency, Float rate) {
        this.id = id;
        this.date = date;
        this.bank = bank;
        this.currency = currency;
        this.rate = rate;
    }

    public RateDto() {
        initDB();
        this.id = sd.nextId();
    }

    @Override
    public String toString(){
        return "<rate><id>"+id+"</id>"
                + "<date>"+date+"</date>"
                + "<bank>"+bank+"</bank>"
                + "<currency>"+currency+"</currency>"
                + "<values>"+rate+"</values>"
                + "</rate>";
    }

    public int getId(){
        return id;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    private void initDB(){
        try {
            sd = new SimpleDerbyDB();
        } catch (SQLException ex) {
            Logger.getLogger(RateResource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }



}
