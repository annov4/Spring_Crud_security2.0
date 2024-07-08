package annov4.crud.crud.service;

import annov4.crud.crud.dao.RoleDao;
import annov4.crud.crud.model.Role;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleDao roleDao;
    @Override
    @Transactional
    public void save(Role role) {
        roleDao.save(role);

    }
}
