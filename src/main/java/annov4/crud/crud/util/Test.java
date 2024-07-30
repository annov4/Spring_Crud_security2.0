package annov4.crud.crud.util;

import annov4.crud.crud.model.Role;
import annov4.crud.crud.model.User;
import annov4.crud.crud.service.RoleService;
import annov4.crud.crud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class Test {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public Test(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        if (userService.findByName("admin") == null) {
            Role adminRole = new Role("ADMIN");
            roleService.save(adminRole);

            User admin = new User();
            admin.setName("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setEmail("admin@example.com");
            admin.setAge(30);

            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            admin.setRole(roles);
            userService.saveUser(admin);
        }

        if (userService.findByName("user") == null) {
            Role userRole = new Role("USER");
            roleService.save(userRole);

            User user = new User();
            user.setName("user");
            user.setPassword(passwordEncoder.encode("user"));
            user.setEmail("user@example.com");
            user.setAge(25);

            Set<Role> roles = new HashSet<>();
            roles.add(userRole);
            user.setRole(roles);
            userService.saveUser(user);
        }
    }
}
