package annov4.crud.crud.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "weather")
public class WeatherConfig {
    @Value("${weather.api-key}")
    private String apiKey;

    @Value("${weather.secret-key}")
    private String secretKey;

    @Value("${weather.access-key}")
    private String accessKey;
}
