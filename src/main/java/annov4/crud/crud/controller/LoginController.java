package annov4.crud.crud.controller;

import annov4.crud.crud.model.Role;
import annov4.crud.crud.model.User;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping()
@AllArgsConstructor
public class LoginController {
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody User user) {
            Set<Role> roles = user.getRoles();
            return ResponseEntity.ok(roles);
        }
}