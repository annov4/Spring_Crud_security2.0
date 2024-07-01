package annov4.crud.crud.service;

import annov4.crud.crud.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    void saveUser(User user);
    boolean updateUser(User user, Long id);

    List<User> getAllUsers();

    User getUserById(Long id);

    void deleteUserById(Long id);

}