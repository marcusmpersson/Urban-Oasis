package controller;
import com.google.gson.GsonBuilder;
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
import java.time.LocalDateTime;
import com.google.gson.Gson;

/**
 * Class that handles all general communication between server and client.
 * @author Christian Storck
 */
public class ClientConnection {
    private CloseableHttpClient httpClient;
    private HttpGet httpGet;
    private HttpPost httpPost;
    private Controller controller;
    private static final String server_url = "http://129.151.219.155:3000/";
    private String jwtToken = "";

    public ClientConnection(Controller controller){
        this.controller = controller;
        this.httpClient = HttpClients.createDefault();
        this.httpGet = new HttpGet("serverURL");
    }

    /**
     * Method that retrieves user data from the server.
     * @author Christian Storck
     */
    public User getUserInfo(){
        try{httpGet.setURI(new URI(server_url + "auth/getUserData"));
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

                    return user;
                }
            }
        }catch(IOException  |URISyntaxException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Method that takes a user, converts it to Json-format and sends it to the server to be saved
     * @param user
     * @return server response
     * @author Christian Storck
     */
    public String saveUser(User user){

        try{
            HttpPost httpPost1 = new HttpPost(server_url + "saveuser");

            httpPost1.setHeader("Authorization", "Bearer " + jwtToken);
            user.setLastUpdatedTime(LocalDateTime.now());
            Gson gson = new GsonBuilder().
                    excludeFieldsWithoutExposeAnnotation()
                    .create();

            String json = gson.toJson(user);
            StringEntity entity = new StringEntity(json);
            entity.setContentType("application/json");
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

    /**
     * Method that allows the user to update their saved account information that is stored in the
     * database.
     * Requires a valid JwtToken.
     * @param email
     * @param userName
     * @param password
     * @return server validation response
     * @author Christian Storck
     */
    public Boolean updateAccountInfo(String email, String userName, String password){
        try {
            httpPost = new HttpPost(server_url + "auth/update");
            String requestBody = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\", \"username\": \"" + userName + "\"}";
            StringEntity entity = new StringEntity(requestBody);
            entity.setContentType("application/json");
            httpPost.setEntity(entity);

            try(CloseableHttpResponse response = httpClient.execute(httpPost)){
                HttpEntity responseEntity = response.getEntity();

                String responseString = EntityUtils.toString(responseEntity);

                if (responseString.equals("Updated")) {
                    return true;
                } else {
                    return false;
                }
            }

        } catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Sends a request to the server with the JWT token in the request to ensure that the server knows who's supposed to log out.
     * @return server response String
     * @author Christian Storck
     */
    public String logout(){
        try {
            HttpPost httpPost1 = new HttpPost(server_url + "auth/logout");
            httpPost1.setHeader("Authorization", "Bearer " + jwtToken);

            try(CloseableHttpResponse response = httpClient.execute(httpPost1)) {
                HttpEntity responseEntity = response.getEntity();

                if(responseEntity != null) {
                    return EntityUtils.toString(responseEntity);
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Method that checks if an email is already used in the database and returns true or false depending on response.
     * @return boolean
     * @author Christian Storck
     */
    public Boolean checkEmailAvailability(String email){
        try {
            HttpPost httpPost1 = new HttpPost(server_url + "auth/email");
            String requestBody = "{\"email\": \"" + email + "\"}";
            StringEntity entity = new StringEntity(requestBody);
            entity.setContentType("application/json");
            httpPost1.setEntity(entity);

            try(CloseableHttpResponse response = httpClient.execute(httpPost1)) {
                HttpEntity responseEntity = response.getEntity();

                String responseString = EntityUtils.toString(responseEntity);

                if(responseString.equals("Email available")) {
                    return true;
                }
                else{
                    return false;
                }
            }
        } catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Method that checks if a username is already used in the database and
     * returns true or false depending on response.
     * @return boolean
     * @author Christian Storck
     */
    public Boolean checkUserNameAvailability(String username){
        try {
            HttpPost httpPost1 = new HttpPost(server_url + "auth/email");
            String requestBody = "{\"username\": \"" + username + "\"}";
            StringEntity entity = new StringEntity(requestBody);
            entity.setContentType("application/json");
            httpPost1.setEntity(entity);

            try(CloseableHttpResponse response = httpClient.execute(httpPost1)) {
                HttpEntity responseEntity = response.getEntity();

                String responseString = EntityUtils.toString(responseEntity);

                if(responseString.equals("Username available")) {
                    return true;
                }
                else{
                    return false;
                }
            }
        } catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Sends a request to the server with the JWT token in the request body to ensure that the
     * server knows what user is supposed to be deleted.
     * @return server response
     * @author Christian Storck
     */
    public String delete(){
        try {
            HttpPost httpPost1 = new HttpPost(server_url + "auth/delete");
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
