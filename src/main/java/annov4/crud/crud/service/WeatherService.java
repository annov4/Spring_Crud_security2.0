package annov4.crud.crud.service;

import annov4.crud.crud.config.WeatherProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class WeatherService {

    private final RestTemplate restTemplate;
    private final WeatherProperties weatherProperties;
    private final String dadataUrl = "https://cleaner.dadata.ru/api/v1/clean/address";

    private final String yandexUrl = "https://api.weather.yandex.ru/v2/forecast";


    public WeatherService(RestTemplate restTemplate, WeatherProperties weatherProperties) {
        this.restTemplate = restTemplate;
        this.weatherProperties = weatherProperties;
        System.out.println("API_KEY: " + weatherProperties.getApiKey());
        System.out.println("SECRET_KEY: " + weatherProperties.getSecretKey());
        System.out.println("ACCESS_KEY: " + weatherProperties.getAccessKey());
    }

    public Coordinates getCoordinates(String address) throws IOException {
        URL url = new URL(dadataUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");

        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Authorization", "Token " + weatherProperties.getApiKey());
        connection.setRequestProperty("X-Secret", weatherProperties.getSecretKey());
        connection.setDoOutput(true);

        String jsonBody = "[ \"" + address + "\" ]";
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonBody.getBytes("UTF-8");
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            StringBuilder response = new StringBuilder();

            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            return parseCoordinates(response.toString());
        } else {
            throw new IOException("Failed to get response from DaData API, HTTP response code: " + responseCode);
        }
    }

    private Coordinates parseCoordinates(String jsonResponse) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(jsonResponse);
        JsonNode addressNode = root.get(0);

        double latitude = addressNode.path("geo_lat").asDouble();
        double longitude = addressNode.path("geo_lon").asDouble();

        return new Coordinates(latitude, longitude);
    }

    public WeatherInfo getWeatherInfo(double latitude, double longitude) throws IOException {
        String url = yandexUrl + "?lat=" + latitude + "&lon=" + longitude;
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("X-Yandex-API-Key", weatherProperties.getAccessKey());

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String response = in.readLine();
            return parseWeatherInfo(response);
        }

    }

    private WeatherInfo parseWeatherInfo(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);
        JsonNode factNode = root.path("fact");

        String condition = factNode.path("condition").asText();

        return new WeatherInfo(condition);
    }

    public static class Coordinates {
        private final double latitude;
        private final double longitude;

        public Coordinates(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }
    }

    public static class WeatherInfo {
        private final String condition;

        public WeatherInfo(String condition) {
            this.condition = condition;
        }

        public String getCondition() {
            return condition;
        }
    }

}