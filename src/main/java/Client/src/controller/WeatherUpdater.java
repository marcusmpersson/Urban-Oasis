package controller;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.IOException;

/**
 * Class that handles API calls to weatherAPI.com.
 * @author Christian Storck
 */
public class WeatherUpdater {
    private CloseableHttpClient closeableHttpClient;
    private HttpGet httpGet;
    private Controller controller;

    /**
     * Constructor for the class, contains the API key, requested method, as well as locale for our search.
     * @param controller
     * @author Christian Storck
     */
    public WeatherUpdater(Controller controller){
        this.controller = controller;
        this.closeableHttpClient = HttpClients.createDefault();
        this.httpGet = new HttpGet("http://api.weatherapi.com/v1/current.json?key=7047aacf688747c5afa194647240605&q=Malmö");
    }

    /**
     * Method that gets the current weather in malmö as a Json object and then filters out everything except for the
     * weather description.
     * @return String current weather
     * @author Christian Storck
     */
    public String getCurrentWeather(){
        try(CloseableHttpResponse response = closeableHttpClient.execute(httpGet)){
            HttpEntity httpEntity = response.getEntity();

            if(httpEntity != null){

                String weatherString = EntityUtils.toString(httpEntity);
                JsonObject jsonObject = JsonParser.parseString(weatherString).getAsJsonObject();
                String weatherText = jsonObject.getAsJsonObject("current").getAsJsonObject("condition").get("text").getAsString();
                System.out.println(weatherText);

                return weatherText;
            }

        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
