package annov4.crud.crud.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordGeneratorEncoder {
    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedAdminPassword = passwordEncoder.encode("admin");
        String hashedUserPassword = passwordEncoder.encode("user");
        System.out.println(hashedUserPassword);
        System.out.println(hashedAdminPassword);
    }
}
