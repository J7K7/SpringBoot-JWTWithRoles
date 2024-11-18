package com.demo.blog.users;

import com.demo.blog.conmon.dtos.ErrorResponseDTO;
import com.demo.blog.security.JWTService;
import com.demo.blog.users.Role.RoleEntity;
import com.demo.blog.users.dtos.CreateUserRequestDTO;
import com.demo.blog.users.dtos.LoginUserRequestDTO;
import com.demo.blog.users.dtos.UserResponseDTO;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/users")
public class UsersController {
    private final JWTService jwtService;
    private final ModelMapper modelMapper;
    private final UsersService usersService;


    public UsersController(JWTService jwtService, ModelMapper modelMapper, UsersService usersService) {
        this.jwtService = jwtService;
        this.modelMapper = modelMapper;
        this.usersService = usersService;
    }

    @PreAuthorize("hasRole('Admin')")
    @PostMapping("/role")
    ResponseEntity<RoleEntity> createRole(@RequestParam(name = "role") String role){
        RoleEntity savedRole = usersService.createRole(role);
        URI savedUserUri = URI.create("/users/role/" + savedRole.getId());
        return ResponseEntity.created(savedUserUri).body(savedRole);
    }

    @PreAuthorize("hasRole('Admin')")
    @PostMapping("/role/{id}")
    ResponseEntity<RoleEntity> getRole(@RequestParam(name = "id") Long role){
        RoleEntity roleEntity = usersService.getRole(role);
        return ResponseEntity.ok(roleEntity);
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('Admin')")
    ResponseEntity<UserResponseDTO> signupAdmin(@RequestBody CreateUserRequestDTO request){
        UserEntity savedUser = usersService.createUser(request, 1L);
        URI savedUserUri = URI.create("/users/" + savedUser.getId());
        var userResponse = modelMapper.map(savedUser, UserResponseDTO.class);
        userResponse.setToken(jwtService.createJWT(savedUser.getId()));
        return ResponseEntity.created(savedUserUri).body(userResponse);
    }

    @PostMapping("")
    ResponseEntity<UserResponseDTO> signupUser(@RequestBody CreateUserRequestDTO request){
        UserEntity savedUser = usersService.createUser(request, 2L);
        URI savedUserUri = URI.create("/users/" + savedUser.getId());
        var userResponse = modelMapper.map(savedUser, UserResponseDTO.class);
        userResponse.setToken(jwtService.createJWT(savedUser.getId()));
        return ResponseEntity.created(savedUserUri).body(userResponse);
    }

    @PostMapping("/login")
    ResponseEntity<UserResponseDTO> loginUser(@RequestBody LoginUserRequestDTO request){
        UserEntity savedUser = usersService.loginUser(request.getUsername(), request.getPassword());
        var userResponse = modelMapper.map(savedUser, UserResponseDTO.class);
        userResponse.setToken(jwtService.createJWT(savedUser.getId()));
        return ResponseEntity.ok(userResponse);
    }

    @ExceptionHandler({
            UsersService.UserNotFoundException.class,
            UsersService.InvalidCredentialException.class
    })
    ResponseEntity<ErrorResponseDTO> handleUserExceptions(Exception ex){
        String message;
        HttpStatus status;

        if(ex instanceof UsersService.UserNotFoundException){
            message = ex.getMessage();
            status = HttpStatus.NOT_FOUND;
        } else if(ex instanceof UsersService.InvalidCredentialException) {
            message = ex.getMessage();
            status = HttpStatus.UNAUTHORIZED;
        } else{
            message = "Something went wrong!";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        ErrorResponseDTO response = ErrorResponseDTO.builder().message(message).build();
        return ResponseEntity.status(status).body(response);
    }
}
