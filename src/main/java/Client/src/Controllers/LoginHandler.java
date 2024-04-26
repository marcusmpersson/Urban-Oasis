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

/**
 * Class that handles the communication between client and Server regarding matters that involve logging in
 * and functions that can be accessed before logging in.
 */
public class LoginHandler {
    private CloseableHttpClient httpClient;
    private HttpPost httpPost;
    private Controller controller;

    public LoginHandler(Controller controller){
        this.controller = controller;
        this.httpClient = HttpClients.createDefault();
    }

    /**
     * Method that calls the login function from the server, and gets a response back in Json-format.
     * The response can be that the login was a success or different kinds of error messages.
     * @param email
     * @param password
     * @return
     */
    public String login(String email, String password) {
        try {
            httpPost = new HttpPost("auth/login");  // Defines what function we're trying to reach from the server.
            String requestBody = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\"}"; // Structures the way we'll send the information to the server.
            StringEntity entity = new StringEntity(requestBody);
            httpPost.setEntity(entity);

            try(CloseableHttpResponse response = httpClient.execute(httpPost)) { // Executes the request to the server with the function
                                                                                    // we defined earlier.
                HttpEntity responseEntity = response.getEntity(); // Handles the response we get from the server and makes it into an HTTP entity.

                if(responseEntity != null) { // If there is something in the response we'll make a Gson object and fill the object with
                                            // the requested data.
                    Gson gson = new Gson();
                    JsonObject jsonResponse = gson.fromJson(EntityUtils.toString(responseEntity), JsonObject.class);

                    if(jsonResponse.has("token")){ // If the response contained a JwtToken we set the token and return.
                        String token = jsonResponse.get("token").getAsString();
                        controller.setJwtToken(token);
                        return "Login was successful";
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
    public String register(String email, String userName, String password) {
        try {
            httpPost = new HttpPost("auth/register");
            String requestBody = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\", \"username\": \"" + userName + "\"}";
            StringEntity entity = new StringEntity(requestBody);
            httpPost.setEntity(entity);

            try(CloseableHttpResponse response = httpClient.execute(httpPost)){
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

                    }

                    else if (response.getStatusLine().getStatusCode() == 422) {
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
