package annov4.crud.crud.util;


import annov4.crud.crud.model.Role;
import annov4.crud.crud.model.User;
import annov4.crud.crud.service.RoleService;
import annov4.crud.crud.service.UserService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;


@Component
public class Test {
    private final UserService userService;
    private final RoleService roleService;

    public Test(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;

    }
    @PostConstruct
    public void addAdmin(){
        Role roleAdmin = new Role("ROLE_ADMIN");
        Role roleUser = new Role("ROLE_USER");
        roleService.save(roleAdmin);
        roleService.save(roleUser);
        Set<Role> adminSet = new HashSet<>();
        adminSet.add(roleAdmin);

        Set<Role> userSet = new HashSet<>();
        userSet.add(roleUser);

        User admin = new User(25, "admin", "admin@mail.ru", "admin", adminSet);
        userService.saveUser(admin);

        User user = new User(30, "user","user@mail.ru", "user", userSet);
        userService.saveUser(user);
    }
}