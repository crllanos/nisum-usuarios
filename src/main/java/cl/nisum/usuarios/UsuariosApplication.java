package cl.nisum.usuarios;

import cl.nisum.usuarios.entity.AdminEntity;
import cl.nisum.usuarios.entity.RoleEntity;
import cl.nisum.usuarios.service.IAdminService;
import cl.nisum.usuarios.service.IRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@SpringBootApplication
@PropertySource("classpath:application.yaml")
public class UsuariosApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsuariosApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}


	/**
	 * Script for creation of dummy admins, in order to test the JWT integration
	 *
	 */
	@Bean
	CommandLineRunner run(IAdminService adminService, IRoleService roleService){
		return args -> {
			log.info("generando admins dummy...");
			String roleAdmin = "ROLE_ADMIN";
			String roleSuperAdmin = "ROLE_SUPERADMIN";

			roleService.saveRole(RoleEntity.builder().rolename(roleAdmin).build());
			roleService.saveRole(RoleEntity.builder().rolename(roleSuperAdmin).build());

			adminService.saveAdmin(AdminEntity.builder().username("bwayne").password("batman").build());
			adminService.saveAdmin(AdminEntity.builder().username("ckent").password("superman").build());

			adminService.addRoleToAdmin("bwayne", roleAdmin);
			adminService.addRoleToAdmin("ckent", roleSuperAdmin);
		};
	}

}
