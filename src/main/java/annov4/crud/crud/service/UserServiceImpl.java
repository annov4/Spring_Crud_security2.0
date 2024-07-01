package annov4.crud.crud.service;

import annov4.crud.crud.model.User;
import annov4.crud.crud.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void saveUser(User user) {
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
    public boolean updateUser(User user, Long id) {
        {
            if (userRepository.existsById(id)) {
                User existingUser = userRepository.findById(id).get();
                existingUser.setName(user.getName());
                existingUser.setAge(user.getAge());
                existingUser.setEmail(user.getEmail());
                existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
                existingUser.setRoles(user.getRoles());
                userRepository.save(existingUser);
                return true;
            } else {
                return false;
            }
        }
    }
}

