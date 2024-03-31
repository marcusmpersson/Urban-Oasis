package Controllers;
import entities.User;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import com.google.gson.Gson;

public class ClientConnection {
    private CloseableHttpClient httpClient;
    private HttpGet httpGet;
    public ClientConnection(){
        this.httpClient = HttpClients.createDefault();
        this.httpGet = new HttpGet("");
    }
    public void makeRequest(String url){ //Method that makes a http request to the server that fetches a User from the database?
        try{httpGet.setURI(new URI(url));
            try(CloseableHttpResponse response = httpClient.execute(httpGet)){
                int statusCode = response.getStatusLine().getStatusCode();
                System.out.println(statusCode);
                HttpEntity entity = response.getEntity();
                if(entity!=null){
                    String responseBody = EntityUtils.toString(entity);
                    System.out.println(responseBody);

                    Gson gson = new Gson();
                    User user = gson.fromJson(responseBody, User.class);
                }
            }
        }catch(IOException  |URISyntaxException e){
            e.printStackTrace();
        }
        finally{
            try{
            httpClient.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
