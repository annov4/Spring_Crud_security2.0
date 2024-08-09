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
    private String apiKey = "cb7093256c8ed475de23fb01148b5f690f3206a7";
    private String secretKey = "47ed84c5594a87a7ba53a7edb3bff33022d66516";
    private String accessKey = "3cf5af7e-da4a-403d-8864-b452374ec1bc";
}
