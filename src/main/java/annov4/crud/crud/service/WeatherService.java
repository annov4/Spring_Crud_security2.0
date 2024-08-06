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


@Service
public class WeatherService {

    private final RestTemplate restTemplate;
    private final String dadataUrl = "https://cleaner.dadata.ru/api/v1/clean/address";
    private final String apiKey = "${APIKEY}";
    private final String secretKey = "${SECRETKEY}";
    private final String yandexUrl = "https://api.weather.yandex.ru/v2/forecast";
    private final String accessKey = "${ACCESSKEY}";

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

    public WeatherInfo getWeatherInfo(double latitude, double longitude) throws IOException {
        String url = yandexUrl + "?lat=" + latitude + "&lon=" + longitude;
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("X-Yandex-API-Key", accessKey);

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