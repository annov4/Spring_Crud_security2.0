package annov4.crud.crud.service;

import annov4.crud.crud.model.Role;
import annov4.crud.crud.model.User;
import annov4.crud.crud.repository.RoleRepository;
import annov4.crud.crud.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singleton(new Role(2L, "ROLE_USER")));
        userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByName(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    @Override
    public boolean userExists(String username) {
        return userRepository.findByName(username) != null;
    }
    @Override
    public void updateUser(User user1) {
        User user2 = userRepository.findById(user1.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user2.setName(user2.getName());
        user2.setAge(user2.getAge());
        user2.setEmail(user2.getEmail());

        if (user2.getPassword() != null && !user2.getPassword().isEmpty()) {
            user2.setPassword(passwordEncoder.encode(user2.getPassword()));
        }

        if (user2.getRoles() != null && !user2.getRoles().isEmpty()) {
            user2.setRoles(user2.getRoles());
        }

        userRepository.save(user2);
    }
    @PostConstruct
    public void init() {
        Role adminRole = new Role(1L, "ROLE_ADMIN");
        Role userRole = new Role(2L, "ROLE_USER");
        roleRepository.saveAll(Arrays.asList(adminRole, userRole));

        User admin = new User();
        admin.setName("admin");
        admin.setEmail("example1@mail.vo");
        admin.setPassword(passwordEncoder.encode("1111"));
        admin.setRoles(new HashSet<>(Collections.singletonList(adminRole)));
        userRepository.save(admin);

        User user = new User();
        user.setName("user");
        user.setEmail("example2@mail.vo");
        user.setPassword(passwordEncoder.encode("2222"));
        user.setRoles(new HashSet<>(Collections.singletonList(userRole)));
        userRepository.save(user);
    }

}
