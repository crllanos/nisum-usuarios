package cl.nisum.usuarios.service.impl;

import cl.nisum.usuarios.entity.RoleEntity;
import cl.nisum.usuarios.repository.RoleRepository;
import cl.nisum.usuarios.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleServiceImpl implements IRoleService {

    private final RoleRepository roleRepository;

    @Override
    public RoleEntity saveRole(RoleEntity role) {
        return roleRepository.save(role);
    }

    @Override
    public RoleEntity findByRolename(String rolename) {
        return roleRepository.findByRolename(rolename);
    }
}