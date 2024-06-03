package annov4.crud.crud.repository;

import annov4.crud.crud.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findAll();

}
