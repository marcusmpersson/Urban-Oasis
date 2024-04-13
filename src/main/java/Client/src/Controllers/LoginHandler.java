package Controllers;

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
    private Controller controller;

    public LoginHandler(Controller controller){
        this.controller = controller;
        this.httpClient = HttpClients.createDefault();
    }
    public String login(String email, String password){ // A method that sends user login info to the server and returns JWT token if successful.
        try{
            HttpPost httpPost1 = new HttpPost("serverURL/login");
            String requestBody = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\"}";
            StringEntity entity = new StringEntity(requestBody);
            httpPost.setEntity(entity);
            try(CloseableHttpResponse response = httpClient.execute(httpPost1)){
                HttpEntity responseEntity = response.getEntity();
                if(responseEntity != null){
                    return EntityUtils.toString(responseEntity);
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }
    public String register(String email, String userName, String password){ // A method that send user info to the server and returns a string to confirm if registration was successful.
        try{
            HttpPost httpPost1 = new HttpPost("serverURL/register");
            String requestBody = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\", \"username\": \"" + userName + "\"}";
            StringEntity entity = new StringEntity(requestBody);
            httpPost.setEntity(entity);
            try(CloseableHttpResponse response = httpClient.execute(httpPost1)){
                HttpEntity responseEntity = response.getEntity();
                if(responseEntity != null){
                    return EntityUtils.toString(responseEntity);
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
