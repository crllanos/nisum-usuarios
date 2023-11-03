package cl.nisum.usuarios.service.impl;

import cl.nisum.usuarios.entity.AdminEntity;
import cl.nisum.usuarios.entity.RoleEntity;
import cl.nisum.usuarios.repository.AdminRepository;
import cl.nisum.usuarios.service.IAdminService;
import cl.nisum.usuarios.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminServiceImpl implements IAdminService, UserDetailsService {
    private final AdminRepository adminRepository;
    private final IRoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AdminEntity saveAdmin(AdminEntity admin) {
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        return adminRepository.save(admin);
    }

    @Override
    public AdminEntity findAdmin(String username) {
        return adminRepository.findByUsername(username);
    }

    @Override
    public void addRoleToAdmin(String username, String rolename) {
        AdminEntity user = this.findAdmin(username);
        RoleEntity role = roleService.findByRolename(rolename);
        user.getRoles().add(role);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdminEntity userEntity = this.findAdmin(username);
        if(userEntity == null){
            throw new UsernameNotFoundException("Invalid credentials.");
        }

        Collection<SimpleGrantedAuthority> authRoles = new ArrayList<>();
        userEntity.getRoles().forEach(r -> {
            authRoles.add(new SimpleGrantedAuthority(r.getRolename()));
        });

        return new User(userEntity.getUsername(), userEntity.getPassword(), authRoles);
    }
}