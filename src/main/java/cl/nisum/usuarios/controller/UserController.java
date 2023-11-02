package cl.nisum.usuarios.controller;

import cl.nisum.usuarios.Util;
import cl.nisum.usuarios.dto.PhoneDTO;
import cl.nisum.usuarios.dto.UserRequestDTO;
import cl.nisum.usuarios.dto.UserResponseDTO;
import cl.nisum.usuarios.entity.PhoneEntity;
import cl.nisum.usuarios.entity.UserEntity;
import cl.nisum.usuarios.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @GetMapping("/{id}")
    public UserResponseDTO getUserById(@PathVariable UUID id){
        log.info(String.format("GET /user-registry/%s", id));
        UserEntity found = iUserService.findById(id);
        log.info(String.format("response: %s", util.obj2Json(found)));
        return userEntity2DTO(found);
    }

    @PutMapping("/{id}")
    public UserResponseDTO updateUser(@PathVariable UUID id, @RequestBody UserRequestDTO userRequest){
        log.info(String.format("PUT /user-registry/%s", id));
        log.info(String.format("Updating user: %s", util.obj2Json(userRequest)));
        UserEntity updated = iUserService.update(id, userRequest2Entity(userRequest));
        log.info(String.format("response: %s", util.obj2Json(updated)));
        return userEntity2DTO(updated);
    }

    @DeleteMapping("/{id}")
    public UserResponseDTO deleteUser(@PathVariable UUID id){
        log.info(String.format("DELETE /user-registry/%s", id));
        UserEntity deleted = iUserService.delete(id);
        log.info(String.format("response: %s", util.obj2Json(deleted)));
        return userEntity2DTO(deleted);
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
                .phones( entity.getPhones().stream()
                        .map( p -> {
                            return PhoneDTO.builder()
                                    .number(p.getNumber())
                                    .contrycode(p.getContrycode())
                                    .citycode(p.getCitycode())
                                    .build();
                        } ).collect(Collectors.toList()))
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
                .phones( userRequest.getPhones().stream()
                        .map(p -> {
                            return PhoneEntity.builder()
                                    .number(p.getNumber())
                                    .contrycode(p.getContrycode())
                                    .citycode(p.getCitycode())
                                    .build();
                        }).collect(Collectors.toList()))
                .build() ;
    }}
