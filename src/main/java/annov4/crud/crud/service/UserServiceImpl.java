package annov4.crud.crud.service;

import annov4.crud.crud.model.Role;
import annov4.crud.crud.model.User;
import annov4.crud.crud.repository.RoleRepository;
import annov4.crud.crud.repository.UserRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void saveUser(User user) {
            User existingUser = userRepository.findByName(user.getName());
            if (existingUser != null) {
                throw new RuntimeException("User with this name already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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
    public void updateUser(User user) {
        User updatedUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        updatedUser.setName(user.getName());
        updatedUser.setAge(user.getAge());
        updatedUser.setEmail(user.getEmail());

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            updatedUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            updatedUser.setRoles(user.getRoles());
        }

        userRepository.save(updatedUser);
    }
    @PostConstruct
    public void init() {
        Role adminRole = new Role(1L, "ROLE_ADMIN");
        Role userRole = new Role(2L, "ROLE_USER");
        roleRepository.saveAll(Arrays.asList(adminRole, userRole));

        User admin = new User();
        admin.setName("admin");
        admin.setEmail("admin@mail.ru");
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
