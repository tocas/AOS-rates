/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rate;

import db.RateDto;
import db.ShoppingDto;
import db.SimpleDerbyDB;
import db.WatchDto;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.MimetypesFileTypeMap;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author tocas
 */

@Path("/rate")
public class RateResource {
    @Context
    private UriInfo context;
    SimpleDerbyDB sd;

    /** Creates a new instance of RateResource */
    public RateResource() {
        try {
            sd = new SimpleDerbyDB();
        } catch (SQLException ex) {
            Logger.getLogger(RateResource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Retrieves representation of an instance of rate.RateResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/xml")
    @Path("{date}/")
    public String getXml(@PathParam("date") String date) {
        String response = "<xml>";
        try {
            ArrayList<RateDto> listRateDto = sd.getRatesFromDay(date);
            for (int i = 0; i < listRateDto.size(); i++) {
                response = response + listRateDto.get(i).toString();
            }
        } catch (SQLException ex) {
            Logger.getLogger(RateResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        response = response+"</xml>";
        return response;
    }

    @GET
    @Produces("application/xml")
    @Path("{date}/{bank}/")
    public String getXml(@PathParam("date") String date,@PathParam("bank") String bank) {
        String response = "<xml>";
        try {
            ArrayList<RateDto> listRateDto = sd.getRatesFromDayAndBank(date, bank);
            for (int i = 0; i < listRateDto.size(); i++) {
                response = response + listRateDto.get(i).toString();
            }
        } catch (SQLException ex) {
            Logger.getLogger(RateResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        response = response+"</xml>";
        return response;
    }

    @GET
    @Produces("application/xml")
    @Path("{date}/{bank}/{currency}")
    public String getXml(@PathParam("date") String date,@PathParam("bank") String bank,@PathParam("currency") String currency) {
        String response = "<xml>";
        try {
            ArrayList<RateDto> listRateDto = sd.getRatesFromDayAndBankAndCurrency(date, bank,currency);
            for (int i = 0; i < listRateDto.size(); i++) {
                response = response + listRateDto.get(i).toString();
            }
        } catch (SQLException ex) {
            Logger.getLogger(RateResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        response = response+"</xml>";
        if(response.compareTo("<xml></xml>")==0){
            throw new WebApplicationException(404);
        }
        return response;
    }


    @GET
    @Produces("application/xml")
    @Path("update/CNB/{date}")
    public String getUpdateCNB(@PathParam("date") String date){
        RateDto rdUSD = new RateDto();
        rdUSD.setBank("CNB");
        rdUSD.setDate(date);
        rdUSD.setCurrency("USD");

        RateDto rdEUR = new RateDto();
        rdEUR.setBank("CNB");
        rdEUR.setDate(date);
        rdEUR.setCurrency("EUR");

        URL url = null;
        String czechDate = czechDate(date);

        try {
            url = new URL("http://www.cnb.cz/cs/financni_trhy/devizovy_trh/kurzy_devizoveho_trhu/denni_kurz.txt?date=" + czechDate);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String str;
            str = in.readLine();
            String downloadedDate = str.substring(0, 10);
            if(downloadedDate.compareTo(czechDate) != 0)
                return "<xml><response><type>FAILD</type><error>Date don't match "+downloadedDate+" is not same as "+czechDate+"</error></responcse></xml>";
            str = in.readLine();
            while ((str = in.readLine()) != null) {
                if(str.contains("USD")) rdUSD.setRate(parseRate(str.substring(str.length()-6)));
                if(str.contains("EUR")) rdEUR.setRate(parseRate(str.substring(str.length()-6)));

            }
        } catch (IOException ex) {
            Logger.getLogger(RateResource.class.getName()).log(Level.SEVERE, null, ex);
            return "<xml><response><type>>FAILD</type></response></xml>";
        }
        //instert data
        ArrayList<RateDto> arrayRates = new ArrayList<RateDto>();
        arrayRates.add(rdEUR);
        arrayRates.add(new RateDto(rdUSD.getId()+1, rdUSD.getDate(), rdUSD.getBank(), rdUSD.getCurrency(), rdUSD.getRate()));
        boolean isOK = sd.putRates(arrayRates);
        if(isOK) return "<xml><response><type>>OK</type></response></xml>";
        return "<xml><response><type>>FAILD</type></response></xml>";
    }


    @GET
    @Path("/images/{image}")
    @Produces("image/*")
    public Response getImage(@PathParam("image") String image) {
        File f = new File("/Users/tocas/Documents/CVUT/ProjectNew/AOS-rates/Server/web/"+image);
        
        if (!f.exists()) {
            throw new WebApplicationException(404);
        }

        String mt = new MimetypesFileTypeMap().getContentType(f);
        return Response.ok(f, mt).build();
    }

    //Watch
    @GET
    @Produces("application/xml")
    @Path("user/{owner}")
    public String getWatch(@PathParam("owner") String owner) {
        String response = "<xml>";   
        ArrayList<WatchDto> listWatchDto = sd.getWatchRates(owner);
        for (int i = 0; i < listWatchDto.size(); i++) {
            response = response + listWatchDto.get(i).toString();
        }
        response = response+"</xml>";
        return response;
    }

    //Schopping
    @GET
    @Produces("application/xml")
    @Path("shopping/{owner}")
    public String getShopping(@PathParam("owner") String owner) {
        String response = "<xml>";
        ArrayList<ShoppingDto> listShoppingDto = sd.getShoppingRates(owner);
        for (int i = 0; i < listShoppingDto.size(); i++) {
            response = response + listShoppingDto.get(i).toString();
        }
        response = response+"</xml>";
        return response;
    }

    /**
     * PUT method for updating or creating an instance of RateResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/xml")
    public void putXml(String content) {

    }

    @PUT @Path("{date}/{bank}/{currency}/{rate}")
    @Consumes( "application/xml")
    public void putRate(@PathParam("date") String date,@PathParam("bank") String bank, @PathParam("currency") String currency,@PathParam("rate") Float rate) {
        RateDto rd = new RateDto(date, bank, currency, rate);
        ArrayList<RateDto> arrDTO = new ArrayList<RateDto>();
        arrDTO.add(rd);
        boolean ok = sd.putRates(arrDTO);
    }
     //Watche
    @PUT
    @Path("{user}/{currency}/{watchrate}")
    @Consumes( "application/xml")
    public void putWatchRate(@PathParam("user") String user, @PathParam("currency") String currency,@PathParam("watchrate") Float watchrate) {
        WatchDto wd = new WatchDto(user, currency, watchrate);
        ArrayList<WatchDto> arrDTO = new ArrayList<WatchDto>();
        arrDTO.add(wd);
        boolean ok = sd.insterWatch(arrDTO);
    }


    private Float parseRate(String str){
        String befourDot = str.substring(0,str.indexOf(","));
        String afterDot = str.substring(str.indexOf(",")+1,str.indexOf(",")+3);
        return new Float(befourDot + "."+afterDot);
    }

    private String czechDate(String date){
        String czechDate = date.substring(8, date.length());
        String czechMonth = date.substring(5,7);
        String czechYear = date.substring(0,4);
        return czechDate + "." + czechMonth + "."+ czechYear;
    }

   



    
}
