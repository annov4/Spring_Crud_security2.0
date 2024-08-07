package annov4.crud.crud.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "weather")
public class WeatherConfig {
    private String apiKey;
    private String secretKey;
    private String accessKey;
}
