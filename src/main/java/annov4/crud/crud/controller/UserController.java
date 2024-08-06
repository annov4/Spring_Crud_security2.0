package annov4.crud.crud.controller;

import annov4.crud.crud.model.User;
import annov4.crud.crud.service.UserService;
import annov4.crud.crud.service.WeatherService;
import lombok.AllArgsConstructor;
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

    @GetMapping
    public ResponseEntity getCurrentUser(Principal principal) throws IOException {
        User user = userService.findByName(principal.getName());
        WeatherService.Coordinates coordinates = weatherService.getCoordinates(user.getHome_address());
        WeatherService.WeatherInfo weatherInfo = weatherService.getWeatherInfo(coordinates.getLatitude(), coordinates.getLongitude());
        Map<String, Object> response = new HashMap<>();
        response.put("user", user);
        response.put("weatherCondition", weatherInfo.getCondition());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}