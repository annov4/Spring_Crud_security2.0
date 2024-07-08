package annov4.crud.crud.dao;


import annov4.crud.crud.model.Role;

import java.util.List;

public interface RoleDao {

    public Role findRoleById(Long id);

    public List<Role> getAllRoles();

    List<Role> getRoles();

    public void save(Role role);
}
