package annov4.crud.crud.controller;

import annov4.crud.crud.model.User;
import annov4.crud.crud.service.UserService;
import annov4.crud.crud.service.WeatherService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;
    private final WeatherService weatherService;

    @GetMapping()
    public ResponseEntity<List<User>> showAllUsers() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUser(id);
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

    @PostMapping()
    public ResponseEntity<HttpStatus> addNewUser(@RequestBody User user) {
        userService.saveUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> updateUser(@RequestBody User user, @PathVariable Long id) {
        userService.updateUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}