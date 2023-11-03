package cl.nisum.usuarios.service;

import cl.nisum.usuarios.entity.RoleEntity;
import org.springframework.stereotype.Service;

@Service
public interface IRoleService {
    RoleEntity saveRole(RoleEntity role);
    RoleEntity findByRolename(String rolename);
}
