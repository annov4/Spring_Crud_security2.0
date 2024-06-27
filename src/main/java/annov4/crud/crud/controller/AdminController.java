package annov4.crud.crud.controller;

import annov4.crud.crud.model.Role;
import annov4.crud.crud.model.User;
import annov4.crud.crud.service.RoleServiceImpl;
import annov4.crud.crud.service.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    private final UserServiceImpl userService;
    private final RoleServiceImpl roleService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> listUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/user-create")
    public ResponseEntity<String> saveUser(@RequestBody User user) {
        userService.saveUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        User user = userService.getUserById(id);
        return user != null
                ? new ResponseEntity<>(user, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/user-update")
    public ResponseEntity<String> updateUser(@RequestBody User user, @RequestParam Set<Long> roles) {
        Set<Role> userRoles = roles.stream()
                .map(roleService::getRoleById)
                .collect(Collectors.toSet());
        user.setRoles(userRoles);
        userService.updateUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @DeleteMapping("/user-delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}