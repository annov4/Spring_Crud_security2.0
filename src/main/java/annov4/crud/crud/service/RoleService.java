package annov4.crud.crud.service;

import annov4.crud.crud.model.Role;
import annov4.crud.crud.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.Set;
@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Iterable<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role getRoleById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }
}
