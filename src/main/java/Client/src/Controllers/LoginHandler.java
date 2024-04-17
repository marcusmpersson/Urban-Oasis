package Client.src.Controllers;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
public class LoginHandler {
    private CloseableHttpClient httpClient;
    private HttpPost httpPost;

    public LoginHandler(){
        this.httpClient = HttpClients.createDefault();
        this.httpPost = new HttpPost("/login");
    }
    public String login(String userName, String password){
        try{
            String requestBody = "{\"email\": \"" + userName + "\", \"password\": \"" + password + "\"}";
            StringEntity entity = new StringEntity(requestBody);
            httpPost.setEntity(entity);
            try(CloseableHttpResponse response = httpClient.execute(httpPost)){
                HttpEntity responseEntity = response.getEntity();
                if(entity != null){
                    return EntityUtils.toString(responseEntity);
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }
    public void register(String email, String userName, String password){

    }
}
