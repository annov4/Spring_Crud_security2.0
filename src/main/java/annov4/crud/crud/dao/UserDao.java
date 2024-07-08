package annov4.crud.crud.dao;

import annov4.crud.crud.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;


public interface UserDao extends UserDetailsService {

    List<User> findAll();

    User findByName(String username);

    User getUser(Long id);

    void saveUser(User user);

    void updateUser(User user);

    void deleteUser(Long id);
}
