/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.fel.cvut.aosDesktop;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;


/** Jersey REST client generated for REST resource:RateResource [/rate]<br>
 *  USAGE:<pre>
 *        NewJerseyClient client = new NewJerseyClient();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 *  </pre>
 * @author tocas
 */
public class RestClient {
    private WebResource webResource;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/Server/aos";

    public RestClient() {
        com.sun.jersey.api.client.config.ClientConfig config = new com.sun.jersey.api.client.config.DefaultClientConfig();
        client = Client.create(config);
        webResource = client.resource(BASE_URI).path("rate");
    }

    public String getXml(String date, String bank, String currency) throws UniformInterfaceException {
        return webResource.path(java.text.MessageFormat.format("{0}/{1}/{2}", new Object[]{date, bank, currency})).accept(javax.ws.rs.core.MediaType.APPLICATION_XML).get(String.class);
    }

    public String getWatch(String owner) throws UniformInterfaceException {
        return webResource.path(java.text.MessageFormat.format("user/{0}", new Object[]{owner})).accept(javax.ws.rs.core.MediaType.APPLICATION_XML).get(String.class);
    }

    public String getShopping(String owner) throws UniformInterfaceException {
        return webResource.path(java.text.MessageFormat.format("shopping/{0}", new Object[]{owner})).accept(javax.ws.rs.core.MediaType.APPLICATION_XML).get(String.class);
    }

    public void putXml(Object requestEntity) throws UniformInterfaceException {
        webResource.type(javax.ws.rs.core.MediaType.APPLICATION_XML).put(requestEntity);
    }

    public String getUpdateCNB(String bank,String date) throws UniformInterfaceException {
        return webResource.path(java.text.MessageFormat.format("update/{0}/{1}", new Object[]{bank, date})).accept(javax.ws.rs.core.MediaType.APPLICATION_XML).get(String.class);
    }

    public <T> T getImage(Class<T> responseType, String image) throws UniformInterfaceException {
        return webResource.path(java.text.MessageFormat.format("images/{0}", new Object[]{image})).get(responseType);
    }

    public void putRate(Object requestEntity, String date, String bank, String currency, String rate) throws UniformInterfaceException {
        webResource.path(java.text.MessageFormat.format("{0}/{1}/{2}/{3}", new Object[]{date, bank, currency, rate})).type(javax.ws.rs.core.MediaType.APPLICATION_XML).put(requestEntity);
    }
    public void putWatchRate(Object requestEntity, String user, String currency, String watchrate) throws UniformInterfaceException {
        webResource.path(java.text.MessageFormat.format("{0}/{1}/{2}", new Object[]{user, currency, watchrate})).type(javax.ws.rs.core.MediaType.APPLICATION_XML).put(requestEntity);
    }

    public void close() {
        client.destroy();
    }

}
