package com.demo.blog.users;

import com.demo.blog.users.dtos.CreateUserRequestDTO;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsersService {
    private final UsersRepository usersRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;


    public UsersService(UsersRepository usersRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity createUser(CreateUserRequestDTO u){
        UserEntity newUser = modelMapper.map(u, UserEntity.class);
        newUser.setPassword(passwordEncoder.encode(u.getPassword()));
        return usersRepository.save(newUser);
    }

    public UserEntity getUser(String username){
        return usersRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
    }

    public UserEntity getUser(Long id){
        return usersRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

   public UserEntity loginUser(String username, String password){
        var user = getUser(username);
        var passMatch = passwordEncoder.matches(password, user.getPassword());
        if(!passMatch) throw new InvalidCredentialException();
        return user;
   }

    public static class UserNotFoundException extends IllegalArgumentException{

        public UserNotFoundException(String username){
            super("user with username: " + username + " not found!");
        }

        public UserNotFoundException(Long id){
            super("user with userId: " + id + " not found!");
        }
    }

    public static class InvalidCredentialException extends IllegalArgumentException{
        public InvalidCredentialException(){
            super("Invalid username or password combination!");
        }
    }
}
