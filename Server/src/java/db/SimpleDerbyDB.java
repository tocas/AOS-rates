/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tocas
 */
public class SimpleDerbyDB {

    private Connection con;

    public SimpleDerbyDB() throws SQLException {
        try {
            con = DriverManager.getConnection("jdbc:derby://localhost:1527/aos", "tocas", "heslo123");
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public ArrayList<RateDto> getRatesFromDay(String date) throws SQLException {
        ArrayList<RateDto> listRateDto = new ArrayList<RateDto>();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM TOCAS.Rate WHERE DATE = '" + date + "'");

            while (rs.next()) {
                RateDto rd = new RateDto(rs.getInt("id"),rs.getString("date"), rs.getString("bank"), rs.getString("currency"), rs.getFloat("rate"));
                listRateDto.add(rd);
                }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return listRateDto;

    }

    public ArrayList<RateDto> getRatesFromDayAndBank(String date,String bank) throws SQLException {
        ArrayList<RateDto> listRateDto = new ArrayList<RateDto>();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM TOCAS.Rate WHERE DATE = '" + date + "' and BANK = '"+bank+"'");

            while (rs.next()) {
                RateDto rd = new RateDto(rs.getInt("id"),rs.getString("date"), rs.getString("bank"), rs.getString("currency"), rs.getFloat("rate"));
                listRateDto.add(rd);
                }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return listRateDto;
    }
    public ArrayList<RateDto> getRatesFromDayAndBankAndCurrency(String date,String bank,String currency) throws SQLException {
        ArrayList<RateDto> listRateDto = new ArrayList<RateDto>();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM TOCAS.Rate WHERE DATE = '" + date + "' and BANK = '"+bank+"' and CURRENCY = '"+currency+"'");

            while (rs.next()) {
                RateDto rd = new RateDto(rs.getInt("id"),rs.getString("date"), rs.getString("bank"), rs.getString("currency"), rs.getFloat("rate"));
                listRateDto.add(rd);
                }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return listRateDto;
    }


    public boolean putRates(ArrayList<RateDto> listRateDto){
        try {
            for(int i = 0; i < listRateDto.size(); i++){

                PreparedStatement ps = con.prepareCall("INSERT INTO TOCAS.RATE VALUES (?, ?, ?, ?, ?)");
                ps.setInt(1, listRateDto.get(i).getId());
                ps.setString(2, listRateDto.get(i).getDate());
                ps.setString(3, listRateDto.get(i).getBank());
                ps.setString(4, listRateDto.get(i).getCurrency());
                ps.setFloat(5, listRateDto.get(i).getRate());
                ps.executeUpdate();

            }
        } catch (SQLException ex) {
            Logger.getLogger(SimpleDerbyDB.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        //check for buy
        checkWatches(listRateDto);

        return true;
    }

    public int nextId(){
        int retrunID = 0;
        Statement stmt;
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT MAX(ID) FROM TOCAS.Rate");
            rs.next();
            retrunID = rs.getInt(1);
            retrunID = retrunID + 1;

        } catch (SQLException e) {
                System.out.println(e);
        }
        return retrunID;
    }

    //WATCH DB
    public int nextIdWatch(){
        int retrunID = 0;
        Statement stmt;
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT MAX(ID) FROM TOCAS.WATCH");
            rs.next();
            retrunID = rs.getInt(1);
            retrunID = retrunID + 1;

        } catch (SQLException e) {
                System.out.println(e);
        }
        return retrunID;
    }

    public int nextIdShopping(){
        int retrunID = 0;
        Statement stmt;
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT MAX(ID) FROM TOCAS.SHOPPING");
            rs.next();
            retrunID = rs.getInt(1);
            retrunID = retrunID + 1;

        } catch (SQLException e) {
                System.out.println(e);
        }
        return retrunID;
    }

    public boolean insterWatch(ArrayList<WatchDto> watches){
        for(int i = 0; i < watches.size(); i++){

            PreparedStatement ps;
            try {
                ps = con.prepareCall("INSERT INTO TOCAS.WATCH VALUES (?, ?, ?, ?)");
                ps.setInt(1, watches.get(i).getId());
                ps.setString(2, watches.get(i).getOwner());
                ps.setString(3, watches.get(i).getCurrency());
                ps.setFloat(4, watches.get(i).getWatchrate());
                ps.executeUpdate();
             } catch (SQLException ex) {
                Logger.getLogger(SimpleDerbyDB.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        return true;
    }

    public ArrayList<WatchDto> getWatchRates(){
        Statement stmt;
        ArrayList<WatchDto> arrayOfWatchs = new ArrayList<WatchDto>();
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM TOCAS.Watch");
            while(rs.next()){
                WatchDto tmp = new WatchDto(rs.getInt("ID"), rs.getString("owner"), rs.getString("currency"), rs.getFloat("watchrate"));
                arrayOfWatchs.add(tmp);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SimpleDerbyDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return arrayOfWatchs;
    }

    public ArrayList<WatchDto> getWatchRates(String user){
        Statement stmt;
        ArrayList<WatchDto> arrayOfWatchs = new ArrayList<WatchDto>();
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM TOCAS.Watch WHERE OWNER = '" + user + "'");
            while(rs.next()){
                WatchDto tmp = new WatchDto(rs.getInt("ID"), rs.getString("owner"), rs.getString("currency"), rs.getFloat("watchrate"));
                arrayOfWatchs.add(tmp);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SimpleDerbyDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return arrayOfWatchs;
    }

    public ArrayList<ShoppingDto> getShoppingRates(String user){
        Statement stmt;
        ArrayList<ShoppingDto> arrayOfBuys= new ArrayList<ShoppingDto>();
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM TOCAS.Shopping WHERE OWNER = '" + user + "'");
            while(rs.next()){
                ShoppingDto tmp = new ShoppingDto(rs.getInt("ID"), rs.getString("owner"), rs.getString("currency"), rs.getInt("amount"));
                arrayOfBuys.add(tmp);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SimpleDerbyDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return arrayOfBuys;
    }

    private void buyCurrency(String owner, String currency, int amount){
            PreparedStatement ps;
            try {
                ps = con.prepareCall("INSERT INTO TOCAS.SHOPPING VALUES (?, ?, ?, ?)");
                ps.setInt(1, nextIdShopping());
                ps.setString(2, owner);
                ps.setString(3, currency);
                ps.setFloat(4, amount);
                ps.executeUpdate();
             } catch (SQLException ex) {
                Logger.getLogger(SimpleDerbyDB.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    private void checkWatches(ArrayList<RateDto> arrayRateDto){
        ArrayList<WatchDto> listWatches = getWatchRates();
        for(int i = 0; i < arrayRateDto.size();i++){
            for(int j = 0; j < listWatches.size();j++){
                if(arrayRateDto.get(i).getCurrency().compareTo(listWatches.get(j).getCurrency())==0){
                    if(arrayRateDto.get(i).getRate() < listWatches.get(j).getWatchrate()){
                        buyCurrency(listWatches.get(j).getOwner(), listWatches.get(j).getCurrency(), 100);
                    }
                }
            }
        }
    }


}
