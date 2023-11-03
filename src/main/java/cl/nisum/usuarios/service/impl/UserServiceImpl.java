package cl.nisum.usuarios.service.impl;

import cl.nisum.usuarios.Util;
import cl.nisum.usuarios.entity.UserEntity;
import cl.nisum.usuarios.repository.UserRepository;
import cl.nisum.usuarios.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;

    private final Util util;

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private static final Pattern VALID_PASSWORD_REGEX =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$");

    @Override
    public List<UserEntity> findAll() {
        log.info("UserService.findAll()");
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public UserEntity saveUser(UserEntity user) {
        log.info(String.format("UserService.saveUser() : %s", util.obj2Json(user)));

        isValidUser(user, true);

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

    @Override
    public UserEntity findById(String id) {
        log.info(String.format("UserService.findById() : %s", id));
        return userRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new EntityNotFoundException(String.format("User %s not found", id)));
    }

    @Override
    public UserEntity update(String id, UserEntity old) {
        log.info(String.format("UserService.update() : %s, %s ", id, util.obj2Json(old)));

        isValidUser(old, false);

        UserEntity update = this.findById(id);
        update.setName(old.getName());
        update.setEmail(old.getEmail());
        update.setPassword(old.getPassword());
        update.setPhones(old.getPhones());
        update.setModified(LocalDateTime.now());

        log.info(String.format("Updating: %s", util.obj2Json(update)));
        return userRepository.save(update);
    }

    @Override
    public UserEntity delete(String id) {
        log.info(String.format("UserService.delete() : %s", id));
        UserEntity del = this.findById(id);
        userRepository.delete(del);
        return del;
    }





    /**
     * Validations
     *
     * */
    private void isValidUser(UserEntity user, boolean isNewEmail){
        // Validates password
        if(ObjectUtils.isEmpty(user.getPassword())  || !validatePassword(user.getPassword())){
            throw new IllegalArgumentException("Invalid password");
        }

        // validates email
        if(ObjectUtils.isEmpty(user.getEmail()) || !validateEmail(user.getEmail())){
            throw new IllegalArgumentException("Invalid e-mail");
        }

        if(isNewEmail){
            // validate email unique
            if(!ObjectUtils.isEmpty(userRepository.findByEmail(user.getEmail()))){
                throw new IllegalArgumentException("Email already exists");
            }
        }
    }

    private static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    private static boolean validatePassword(String passw) {
        Matcher matcher = VALID_PASSWORD_REGEX.matcher(passw);
        return matcher.find();
    }
}
