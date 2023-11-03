package cl.nisum.usuarios.service;

import cl.nisum.usuarios.entity.UserEntity;

import java.util.List;

public interface IUserService {
    List<UserEntity> findAll();
    UserEntity saveUser(UserEntity user);
    UserEntity findById(String id);
    UserEntity update(String id, UserEntity old);
    UserEntity delete(String id);
}
