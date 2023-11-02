package cl.nisum.usuarios.controller;

import cl.nisum.usuarios.Util;
import cl.nisum.usuarios.dto.UserRequestDTO;
import cl.nisum.usuarios.dto.UserResponseDTO;
import cl.nisum.usuarios.entity.UserEntity;
import cl.nisum.usuarios.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user-registry/")
public class UserController {

    private final IUserService iUserService;

    private final Util util;

    @GetMapping
    public List<UserResponseDTO> getUserList(){
        log.info("GET /user-registry/");
        List<UserResponseDTO> response = new ArrayList<>();
        for(UserEntity u : iUserService.findAll()){
            response.add(userEntity2DTO(u));
        }
        log.info(String.format("response: %d", response.size()));
        return response;
    }

    @PostMapping
    public UserResponseDTO createUser(@RequestBody UserRequestDTO userRequest){
        log.info("POST /user-registry/");
        log.info(String.format("Creating user: %s", util.obj2Json(userRequest)));
        UserEntity saved = iUserService.saveUser(userRequest2Entity(userRequest));
        log.info(String.format("response: %s", util.obj2Json(saved)));
        return userEntity2DTO(saved);
    }



    /**
     * User entity to DTO
     *
     */
    private UserResponseDTO userEntity2DTO(UserEntity entity){
        return UserResponseDTO.builder()
                .id(entity.getId().toString())
                .name(entity.getName())
                .email(entity.getEmail())
                .created(entity.getCreated())
                .modified(entity.getModified())
                .lastLogin(entity.getLastLogin())
                .token(entity.getToken())
                .active(entity.isActive())
                .build();
    }

    /**
     * User DTO to entity
     *
     */
    private UserEntity userRequest2Entity(UserRequestDTO userRequest) {
        return UserEntity.builder()
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .build() ;
    }}
