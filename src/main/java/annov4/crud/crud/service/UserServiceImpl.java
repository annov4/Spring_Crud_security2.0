package annov4.crud.crud.service;

import annov4.crud.crud.dao.UserDao;
import annov4.crud.crud.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Primary
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserDao userDao, @Lazy PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        return userDao.loadUserByUsername(name);
    }


    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Override
    public User findByName(String username) {
        return userDao.findByName(username);
    }

    @Override
    public User getUser(Long id) {
        return userDao.getUser(id);
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.saveUser(user);

    }

    @Override
    @Transactional
    public void updateUser(User user) {
        User existingUser = userDao.getUser(user.getId());

        if (user.getAge() != existingUser.getAge()) {
            existingUser.setAge(user.getAge());
        }

        if (!user.getName().equals(existingUser.getName())) {
            existingUser.setName(user.getName());
        }

        if (!user.getEmail().equals(existingUser.getEmail())) {
            existingUser.setEmail(user.getEmail());
        }

        if (user.getPassword() != null && !user.getPassword().isEmpty() && !user.getPassword().equals(existingUser.getPassword())) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            existingUser.setPassword(existingUser.getPassword());
        }
        if (!user.getRole().equals(existingUser.getRole())) {
            existingUser.setRole(user.getRole());
        }

        userDao.updateUser(existingUser);
    }

    @Override
    public void deleteUser(Long id) {
        userDao.deleteUser(id);

    }
}