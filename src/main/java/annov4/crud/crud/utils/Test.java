package annov4.crud.crud.utils;

import annov4.crud.crud.config.WeatherProperties;
import annov4.crud.crud.service.WeatherService;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

public class Test {
    public static void main(String[] args) {

        RestTemplate restTemplate = new RestTemplate();
        WeatherProperties weatherConfig = new WeatherProperties();
        WeatherService weatherService = new WeatherService(restTemplate, weatherConfig);

        try {
            WeatherService.Coordinates coordinates = weatherService.getCoordinates("прасковея");
            WeatherService.WeatherInfo weatherInfo = weatherService.getWeatherInfo(coordinates.getLatitude(), coordinates.getLongitude());

            System.out.printf("Latitude: %f, Longitude: %f, Condition: %s%n",
                    coordinates.getLatitude(), coordinates.getLongitude(), weatherInfo.getCondition());
        } catch (IOException e) {
            System.err.println("Error fetching weather data: " + e.getMessage());
        }
    }
}
