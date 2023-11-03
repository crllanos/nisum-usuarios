package cl.nisum.usuarios.repository;

import cl.nisum.usuarios.entity.AdminEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends CrudRepository<AdminEntity, Long> {

    AdminEntity findByUsername(String username);

}
