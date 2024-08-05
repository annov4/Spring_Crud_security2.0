package annov4.crud.crud.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


@Service
public class WeatherService {

    private final RestTemplate restTemplate;
    private final String dadataUrl = "https://cleaner.dadata.ru/api/v1/clean/address";
    private final String apiKey = "cb7093256c8ed475de23fb01148b5f690f3206a7";
    private final String secretKey = "47ed84c5594a87a7ba53a7edb3bff33022d66516";
    private final String yandexUrl = "https://api.weather.yandex.ru/v2/forecast";
    private final String yandexApiKey = "3cf5af7e-da4a-403d-8864-b452374ec1bc";

    public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Coordinates getCoordinates(String address) throws IOException {
        //Create connection
        URL url = new URL(dadataUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");

        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Authorization", "Token " + apiKey);
        connection.setRequestProperty("X-Secret", secretKey);
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

    public WeatherInfo getWeatherInfo(double latitude, double longitude) {
        String url = yandexUrl + "?lat=" + latitude + "&lon=" + longitude;
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Yandex-API-Key", yandexApiKey);

        String response = restTemplate.getForObject(url, String.class);
        return parseWeatherInfo(response);
    }

    private WeatherInfo parseWeatherInfo(String json) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode;
        try {
            rootNode = mapper.readTree(json);
            JsonNode fact = rootNode.path("fact");
            String condition = fact.path("condition").asText();
            return new WeatherInfo(condition);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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

