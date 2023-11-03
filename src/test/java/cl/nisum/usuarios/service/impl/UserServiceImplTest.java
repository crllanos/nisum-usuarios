package cl.nisum.usuarios.service.impl;

import cl.nisum.usuarios.Util;
import cl.nisum.usuarios.entity.UserEntity;
import cl.nisum.usuarios.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Util util;

    @Test
    public void shouldNotSaveUser_invalidPassword(){
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.saveUser(mockUser_invalidPassword("aeiou"));
        });
        String expectedMessage = "Invalid password";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void shouldNotSaveUser_invalidEmail(){
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.saveUser(mockUser_invalidEmail("correo_novalido.cl"));
        });
        String expectedMessage = "Invalid e-mail";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void shouldNotSaveUser_uniqueEmail(){
        String mailRegistrado = "email@registrado.cl";
        when(userRepository.findByEmail(mailRegistrado)).thenReturn(UserEntity.builder().email(mailRegistrado).build());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.saveUser(mockUser_uniqueEmail(mailRegistrado));
        });
        String expectedMessage = "Email already exists";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void shouldSaveUser_Ok(){
        when(userRepository.save(any())).thenReturn(mockUser());
        assertNotNull(userService.saveUser(mockUser()));
    }

    @Test
    public void shouldListUser_Ok(){
        assertNotNull(userService.findAll());
    }

    @Test
    public void shouldNotGetUser_invalidUUID(){
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.findById("abc");
        });
        String expectedMessage = "Invalid UUID string";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void shouldGetUser_Ok(){
        when(userRepository.findById(UUID.fromString(mockuuid))).thenReturn(Optional.ofNullable(mockUser()));
        assertNotNull(userService.findById(mockuuid));
    }

    @Test
    public void shouldUpdateUser_Ok(){
        when(userRepository.findById(UUID.fromString(mockuuid))).thenReturn(Optional.ofNullable(mockUser()));
        when(userRepository.save(any())).thenReturn(mockUser());
        assertNotNull(userService.update(mockuuid, mockUser()));
    }

    @Test
    public void shouldDeleteUser_Ok(){
        when(userRepository.findById(UUID.fromString(mockuuid))).thenReturn(Optional.ofNullable(mockUser()));
        assertNotNull(userService.delete(mockuuid));
    }



    /**
     * mock data
     */
    private static final String mockuuid = "c7a642f1-a413-4f32-8d98-27b9b3c1ff10";

    private UserEntity mockUser(){
        return UserEntity.builder().name("test").email("correo@valido.cl").password("AbCdEfG1").build();
    }

    private UserEntity mockUser_invalidPassword(String pass){
        UserEntity user = mockUser();
        user.setPassword(pass);
        return user;
    }

    private UserEntity mockUser_invalidEmail(String mail){
        UserEntity user = mockUser();
        user.setEmail(mail);
        return user;
    }

    private UserEntity mockUser_uniqueEmail(String unique){
        UserEntity user = mockUser();
        user.setEmail(unique);
        return user;
    }

    private List<UserEntity> mockUserList(){
        List<UserEntity> list = new ArrayList<>();
        list.add(mockUser());
        list.add(mockUser());
        list.add(mockUser());
        return list;
    }
}