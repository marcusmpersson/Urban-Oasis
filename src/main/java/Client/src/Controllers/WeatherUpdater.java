package Controllers;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.IOException;


public class WeatherUpdater {
    private CloseableHttpClient closeableHttpClient;
    private HttpGet httpGet;
    private Controller controller;

    public WeatherUpdater(Controller controller){
        this.controller = controller;
        this.closeableHttpClient = HttpClients.createDefault();
        this.httpGet = new HttpGet("http://api.weatherapi.com/v1/current.json?key=7047aacf688747c5afa194647240605&q=Malmö");
    }

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
