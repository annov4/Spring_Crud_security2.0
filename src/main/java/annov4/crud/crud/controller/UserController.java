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


@RestController
@AllArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final WeatherService weatherService;

    @GetMapping
    public ResponseEntity<User> getCurrentUser(Principal principal) {
        User user = userService.findByName(principal.getName());
        try {
            WeatherService.Coordinates coordinates = weatherService.getCoordinates(user.getHomeAddress());
            WeatherService.WeatherInfo weatherInfo = weatherService.getWeatherInfo(coordinates.getLatitude(), coordinates.getLongitude());
            if ("rain".equalsIgnoreCase(weatherInfo.getCondition())) {
                user.setUmbrella(true);
            } else {
                user.setUmbrella(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}