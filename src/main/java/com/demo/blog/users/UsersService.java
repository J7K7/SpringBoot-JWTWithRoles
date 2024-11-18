package com.demo.blog.users;

<<<<<<< HEAD
import com.demo.blog.users.Role.RoleEntity;
import com.demo.blog.users.Role.RoleRepository;
import com.demo.blog.users.UserRole.UserRoleEntity;
import com.demo.blog.users.UserRole.UserRoleRepository;
import com.demo.blog.users.dtos.CreateUserRequestDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Set;
import java.util.Collection;

@Service
public class UsersService implements UserDetailsService {
    private final UsersRepository usersRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UsersService(UsersRepository usersRepository, RoleRepository roleRepository, UserRoleRepository userRoleRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
=======
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
>>>>>>> 5f1df2d0576ba6683e8c0efc4dff5fb4abde8dbd
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

<<<<<<< HEAD
    //Role API's

    public RoleEntity createRole(String role){
        RoleEntity newRole = new RoleEntity();
        newRole.setRole(role);
        return roleRepository.save(newRole);
    }

    public RoleEntity getRole(Long id){
        var savedRole = roleRepository.findById(id).orElseThrow(() -> new RoleNotFoundException());
        return savedRole;
    }

    // User API's
    @Transactional
    public UserEntity createUser(CreateUserRequestDTO u, Long id){
        UserEntity newUser = modelMapper.map(u, UserEntity.class);
        newUser.setPassword(passwordEncoder.encode(u.getPassword()));
        UserEntity savedUser = usersRepository.save(newUser);
        RoleEntity role = getRole(id);
        role = roleRepository.save(role);
        createUserRole(savedUser, role);
        return savedUser;
=======
    public UserEntity createUser(CreateUserRequestDTO u){
        UserEntity newUser = modelMapper.map(u, UserEntity.class);
        newUser.setPassword(passwordEncoder.encode(u.getPassword()));
        return usersRepository.save(newUser);
>>>>>>> 5f1df2d0576ba6683e8c0efc4dff5fb4abde8dbd
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
<<<<<<< HEAD
//        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        return user;
   }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = usersRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                true,
                true,
                true,
                true,
                getAuthorities(user.getId())
        );
    }

    public Collection<? extends GrantedAuthority> getAuthorities(Long id) {

        List<UserRoleEntity> userRoles = userRoleRepository.findAll();

        Set<UserRoleEntity> roles = new HashSet<>();

        for(UserRoleEntity userRole : userRoles){
            if(userRole.getUser().getId() == id){
                roles.add(userRole);
            }
        }

        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(getRole(role.getRole().getId()).getRole()))
                .collect(Collectors.toList());
    }

    //User-Role API's

    public String createUserRole(UserEntity userEntity, RoleEntity roleEntity){
        UserRoleEntity newUserRole = new UserRoleEntity();
        newUserRole.setUser(userEntity);
        newUserRole.setRole(roleEntity);
        userRoleRepository.save(newUserRole);
        return "Role Updated !";
    }

    //Excpetions
=======
        return user;
   }

>>>>>>> 5f1df2d0576ba6683e8c0efc4dff5fb4abde8dbd
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
<<<<<<< HEAD

    public static class RoleNotFoundException extends IllegalArgumentException{
        public RoleNotFoundException(){
            super("Role Not Found!");
        }
    }
=======
>>>>>>> 5f1df2d0576ba6683e8c0efc4dff5fb4abde8dbd
}
