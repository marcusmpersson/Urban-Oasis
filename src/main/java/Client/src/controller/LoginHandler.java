package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.IOException;

/**
 * Class that handles the communication between client and Server regarding matters that involve logging in
 * and functions that can be accessed before logging in.
 */
public class LoginHandler {
    private static final String server_url = "http://129.151.219.155:3000/";
    private CloseableHttpClient httpClient;
    private HttpPost httpPost;
    private Controller controller;

    public LoginHandler(Controller controller){
        this.controller =controller;
        this.httpClient = HttpClients.createDefault();
    }

    /**
     * Method that calls the login function from the server, and gets a response back in Json-format.
     * The response can be that the login was a success or different kinds of error messages.
     * @param email
     * @param password
     * @return
     */
    public Boolean login(String email, String password) {
        try {
            httpPost = new HttpPost(server_url + "auth/login");  // Defines what function we're trying to reach from the server.
            String requestBody = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\"}"; // Structures the way we'll send the information to the server.
            StringEntity entity = new StringEntity(requestBody);
            entity.setContentType("application/json");
            httpPost.setEntity(entity);

            try(CloseableHttpResponse response = httpClient.execute(httpPost)) { // Executes the request to the server with the function specified in the httpost.
                int statusCode = response.getStatusLine().getStatusCode();

                if(statusCode == HttpStatus.SC_OK){
                    HttpEntity responseEntity = response.getEntity(); // Handles the response we get from the server and makes it into an HTTP entity.
                    System.out.println(response.toString());



                    if(responseEntity != null) { // If there is something in the response we'll make a Gson object and fill the object with
                                                // the requested data.
                        Gson gson = new Gson();
                        JsonObject jsonResponse = gson.fromJson(EntityUtils.toString(responseEntity), JsonObject.class);

                        if(jsonResponse.has("token")){ // If the response contained a JwtToken we set the token and return.
                            String token = jsonResponse.get("token").getAsString();
                            controller.setJwtToken(token);
                            return true;
                        }
                    }

                    else {
                        return false;
                    }
                }
            }
        } catch(IOException | JsonSyntaxException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Method that calls the register function on the server. The server returns a string confirming if we could
     * login or not.
     * @param email
     * @param userName
     * @param password
     * @return String confirmation
     */
    public Boolean register(String email, String userName, String password) {
        try {
            httpPost = new HttpPost(server_url + "auth/register");
            String requestBody = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\", \"username\": \"" + userName + "\"}";
            StringEntity entity = new StringEntity(requestBody);
            entity.setContentType("application/json");
            httpPost.setEntity(entity);

            try(CloseableHttpResponse response = httpClient.execute(httpPost)){
                HttpEntity responseEntity = response.getEntity();

                String responseString = EntityUtils.toString(responseEntity);

                if (responseString.equals("Registered")) {
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
}
