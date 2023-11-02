package cl.nisum.usuarios.service.impl;

import cl.nisum.usuarios.Util;
import cl.nisum.usuarios.entity.UserEntity;
import cl.nisum.usuarios.repository.UserRepository;
import cl.nisum.usuarios.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;

    private final Util util;

    @Override
    public List<UserEntity> findAll() {
        log.info("UserService.findAll()");
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public UserEntity saveUser(UserEntity user) {
        log.info(String.format("UserService.saveUser() : %s", util.obj2Json(user)));

        // isValidUser(user, true); // @todo

        user.setId(UUID.randomUUID());
        user.setCreated(LocalDateTime.now());
        user.setModified(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());
        user.setToken(user.getId().toString());

        // para efectos del ejemplo, el usuario quedará activo de manera aleatoria
        int randomNum = ThreadLocalRandom.current().nextInt(1, 10);
        user.setActive(randomNum %2 == 0);

        log.info(String.format("Persisting: %s", util.obj2Json(user)));
        return userRepository.save(user);
    }
}
