package Controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.sql.Timestamp;

public class LoginHandler {
    private CloseableHttpClient httpClient;
    private HttpPost httpPost;
    private Controller controller;

    public LoginHandler(Controller controller){
        this.controller = controller;
        this.httpClient = HttpClients.createDefault();
    }
    public String login(String email, String password) { // A method that sends user login info to the server and returns JWT token if successful.
        try {
            HttpPost httpPost1 = new HttpPost("auth/login");
            String requestBody = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\"}";
            StringEntity entity = new StringEntity(requestBody);
            httpPost.setEntity(entity);

            try(CloseableHttpResponse response = httpClient.execute(httpPost1)) {
                HttpEntity responseEntity = response.getEntity();

                if(responseEntity != null) {
                    Gson gson = new Gson();
                    JsonObject jsonResponse = gson.fromJson(EntityUtils.toString(responseEntity), JsonObject.class);

                    if(jsonResponse.has("token")){
                        String token = jsonResponse.get("token").getAsString();
                        return token;
                    }

                    else if(jsonResponse.has("Invalid credentials")){
                        return "Invalid Credentials";
                    }

                    else if (response.getStatusLine().getStatusCode() == 422) {
                        return "Something went wrong";
                    }
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String register(String email, String userName, String password) { // A method that send user info to the server and returns a string to confirm if registration was successful.
        try {
            HttpPost httpPost1 = new HttpPost("auth/register");
            String requestBody = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\", \"username\": \"" + userName + "\"}";
            StringEntity entity = new StringEntity(requestBody);
            httpPost.setEntity(entity);

            try(CloseableHttpResponse response = httpClient.execute(httpPost1)){
                HttpEntity responseEntity = response.getEntity();
                if(responseEntity != null){
                    Gson gson = new Gson();
                    JsonObject jsonResponse = gson.fromJson(EntityUtils.toString(responseEntity), JsonObject.class);
                    
                    if(jsonResponse.has("Response")){
                        Boolean trueOrFalse = true;
                        return jsonResponse.get("Response").getAsString();
                    }
                    else if (jsonResponse.has("Email already exists")) {
                        return jsonResponse.get("Email already exists").getAsString();

                    } else if (response.getStatusLine().getStatusCode() == 422) {
                        String errorMessage;
                        return errorMessage = "Something went wrong";
                    }
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
