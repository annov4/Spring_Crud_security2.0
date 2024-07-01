package annov4.crud.crud.controller;

import annov4.crud.crud.model.User;
import annov4.crud.crud.service.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
@RequestMapping("/api/user")
@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
public class UserController {

    private final UserServiceImpl userService;

    @GetMapping()
    public ResponseEntity<User> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = (User) userService.loadUserByUsername(userDetails.getUsername());
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}