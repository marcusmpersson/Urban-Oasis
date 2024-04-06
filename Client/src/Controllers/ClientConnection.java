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
    private Controller controller;
    private String jwtToken = "";
    public ClientConnection(Controller controller){
        this.controller = controller;
        this.httpClient = HttpClients.createDefault();
        this.httpGet = new HttpGet("serverURL");
    }
    public void makeRequest(String url){ //A test method that makes a https request to the server with the jwt token set in the request header.
                                        //This test method handles the return data of a user entity. Server returns a Json file which is parsed into a user class.
        try{httpGet.setURI(new URI(url));
            httpGet.setHeader("Authorization", "Bearer " + jwtToken);
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
    public String getJwtToken() {
        return jwtToken;
    }
    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
