package Controllers;
import com.google.gson.JsonObject;
import entities.User;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
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
    private HttpPost httpPost;
    private Controller controller;
    private String jwtToken = "";
    public ClientConnection(Controller controller){
        this.controller = controller;
        this.httpClient = HttpClients.createDefault();
        this.httpGet = new HttpGet("serverURL");
    }
    public void getUserInfo(){ //A test method that makes a https request to the server with the jwt token set in the request header.
                                        //This test method handles the return data of a user entity. Server returns a Json file which is parsed into a user class.
        try{httpGet.setURI(new URI("serverURL/getUserData"));
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
    }
    public String saveUser(User user){ //Method that takes in a user, remakes it to Json format and sends it to the server. Returns
                                            //a string to confirm if it worked or not.
        try{
            HttpPost httpPost1 = new HttpPost("serverURL/saveUser");
            httpPost1.setHeader("Authorization", "Bearer " + jwtToken);
            Gson gson = new Gson();
            String json = gson.toJson(user);
            StringEntity entity = new StringEntity(json);
            httpPost1.setEntity(entity);

            try(CloseableHttpResponse response = httpClient.execute(httpPost1)) {
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
    public String logout(){ // Sends a request to the server with the JWT token in the request to ensure that the server knows who's supposed to log out.
        try {
            HttpPost httpPost1 = new HttpPost("auth/logout");
            httpPost1.setHeader("Authorization", "Bearer " + jwtToken);
            try(CloseableHttpResponse response = httpClient.execute(httpPost1)){
                HttpEntity responseEntity = response.getEntity();
                if(responseEntity != null){
                    return EntityUtils.toString(responseEntity);
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Boolean checkEmailAvailability(){
        try {
            HttpGet httpGet1 = new HttpGet("auth/email");
            try(CloseableHttpResponse response = httpClient.execute(httpGet1)) {
                HttpEntity responseEntity = response.getEntity();

                if(responseEntity != null) {
                    Gson gson = new Gson();
                    JsonObject jsonObject = gson.fromJson(EntityUtils.toString(responseEntity), JsonObject.class);

                    if(jsonObject.has("Email available")) {
                        return true;
                    }
                }
            }
        } catch(IOException e){
            e.printStackTrace();
        }
        return false;
    }
    public Boolean checkUserNameAvailability(){
        try {
            HttpGet httpGet1 = new HttpGet("auth/username");

            try(CloseableHttpResponse response = httpClient.execute(httpGet1)) {
                HttpEntity responseEntity = response.getEntity();

                if(responseEntity != null) {
                    Gson gson = new Gson();
                    JsonObject jsonObject = gson.fromJson(EntityUtils.toString(responseEntity), JsonObject.class);

                    if(jsonObject.has("Username available")) {
                        return true;
                    }
                }
            }
        } catch(IOException e){
            e.printStackTrace();
        }
        return false;
    }
    public String delete(){ // Sends a request to the server with the JWT token in the request body to ensure that the server knows what user is supposed to be deleted.
        try {
            HttpPost httpPost1 = new HttpPost("serverURL/delete");
            httpPost1.setHeader("Authorization", "Bearer " + jwtToken);

            try(CloseableHttpResponse response = httpClient.execute(httpPost1)){
                HttpEntity responseEntity = response.getEntity();

                if(responseEntity != null){
                    return EntityUtils.toString(responseEntity);
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getJwtToken() {
        return jwtToken;
    }
    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
