package annov4.crud.crud.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "weather")
public class WeatherProperties {
    private String apiKey;
    private String secretKey;
    private String accessKey;
}
