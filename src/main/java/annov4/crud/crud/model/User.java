package annov4.crud.crud.model;

import lombok.*;
import javax.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "age")
    private int age;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "home_address")
    private String home_address;

    @Column(name = "password")
    private String password;

    @Transient
    private String weatherCondition;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> role;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    public User(int age, String name, String email, String home_address, String password, Set<Role> role) {
        this.age = age;
        this.name = name;
        this.email = email;
        this.home_address = home_address;
        this.password = password;
        this.role = role;

    }
    public User(long id, int age, String name, String email, String home_address, String password, Set<Role> role) {
        this.id = id;
        this.age = age;
        this.name = name;
        this.email = email;
        this.home_address = home_address;
        this.password = password;
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", home_address='" + home_address + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }
}