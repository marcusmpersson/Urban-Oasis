package Controllers;
import Builders.LocalDateTimeTypeAdapter;
import com.google.gson.GsonBuilder;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.google.gson.Gson;

/**
 * Class that handles all general communication between server and client.
 * WARNING ----- SUBJECT TO CHANGE
 */
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

    /**
     * Method that retrieves user data from the server.
     * WARNING -- SUBJECT TO CHANGE.
     */
    public User getUserInfo(){
        try{httpGet.setURI(new URI("serverURL/getUserData"));
            httpGet.setHeader("Authorization", "Bearer " + jwtToken);

            try(CloseableHttpResponse response = httpClient.execute(httpGet)){
                int statusCode = response.getStatusLine().getStatusCode();
                System.out.println(statusCode);
                HttpEntity entity = response.getEntity();

                if(entity!=null){
                    String responseBody = EntityUtils.toString(entity);
                    System.out.println(responseBody);
                    Gson gson = new GsonBuilder().
                            excludeFieldsWithoutExposeAnnotation()
                            .create();
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
     */
    public String saveUser(User user){

        try{
            HttpPost httpPost1 = new HttpPost("https://2ceab96d-998c-4d7a-aa8c-7c5ae1e24d2b.mock.pstmn.io/saveuser"); // Sets the "waypoint" to which function
                                                                                                                            //on the server should be called.
            httpPost1.setHeader("Authorization", "Bearer " + jwtToken); // Sets the JwtToken to our request header, if there's no token
            user.setLastUpdatedTime(LocalDateTime.now());                                                                        // the server won't accept our requests.
            Gson gson = new GsonBuilder(). // Creates a Gson object that will exclude object variables that are not set with @expose.
                    excludeFieldsWithoutExposeAnnotation()
                    .create();

            String json = gson.toJson(user);
            StringEntity entity = new StringEntity(json);
            httpPost1.setEntity(entity);

            try(CloseableHttpResponse response = httpClient.execute(httpPost1)) {  // Executes our request with the user as our payload.
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
    /*
       Sends a request to the server with the JWT token in the request to ensure that the server knows who's supposed to log out.
     */
    public String logout(){
        try {
            HttpPost httpPost1 = new HttpPost("auth/logout");
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
     */
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

    /**
     * Method that checks if a username is already used in the database and
     * returns true or false depending on response.
     * @return boolean
     */
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

    /**
     * Sends a request to the server with the JWT token in the request body to ensure that the
     * server knows what user is supposed to be deleted.
     * @return server response
     */
    public String delete(){
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
