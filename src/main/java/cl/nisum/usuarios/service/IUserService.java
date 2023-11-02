package cl.nisum.usuarios.service;

import cl.nisum.usuarios.entity.UserEntity;

import java.util.List;
import java.util.UUID;

public interface IUserService {
    List<UserEntity> findAll();
    UserEntity saveUser(UserEntity user);
    UserEntity findById(UUID id);
    UserEntity update(UUID id, UserEntity old);
    UserEntity delete(UUID id);
}
