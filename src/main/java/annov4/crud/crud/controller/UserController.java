package annov4.crud.crud.controller;

import annov4.crud.crud.model.User;
import annov4.crud.crud.service.UserService;
import annov4.crud.crud.service.WeatherService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final WeatherService weatherService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public ResponseEntity<Map<String, Object>> getCurrentUser(Principal principal) throws IOException {
        String username = principal.getName();

        User user = userService.findByName(username);

        WeatherService.Coordinates coordinates = weatherService.getCoordinates(user.getAddress());
        logger.info("latitude={}, longitude={}", coordinates.getLatitude(), coordinates.getLongitude());

        WeatherService.WeatherInfo weatherInfo = weatherService.getWeatherInfo(coordinates.getLatitude(), coordinates.getLongitude());
        logger.info("weatherInfo : {}", weatherInfo.getCondition());

        Map<String, Object> response = new HashMap<>();
        response.put("user", user);
        response.put("weatherCondition", weatherInfo.getCondition());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

