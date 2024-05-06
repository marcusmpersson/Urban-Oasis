package Controllers;

public class Main {
    public static void main(String[] args) {

        Controller controller = Controller.getInstance();
        WeatherUpdater weatherUpdater = new WeatherUpdater();
        weatherUpdater.getCurrentWeather();

        // Glöm inte att stänga av clientConnections httpClient när man stänger av programmet.

    }
}
