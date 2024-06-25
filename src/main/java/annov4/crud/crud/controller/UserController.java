package annov4.crud.crud.controller;

import annov4.crud.crud.model.User;
import annov4.crud.crud.service.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
@RequestMapping()
public class UserController {

    private final UserServiceImpl userService;


    @GetMapping
    public ResponseEntity<User> getUserDetails(Authentication authentication) {
        User user = (User) userService.loadUserByUsername(authentication.getName());
        return ResponseEntity.ok(user);
    }
}