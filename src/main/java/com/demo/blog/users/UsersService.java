package com.demo.blog.users;

import com.demo.blog.exception.GlobalExceptionHandler;
import com.demo.blog.users.Role.RoleEntity;
import com.demo.blog.users.Role.RoleRepository;
import com.demo.blog.users.UserRole.UserRoleEntity;
import com.demo.blog.users.UserRole.UserRoleRepository;
import com.demo.blog.users.dtos.CreateUserRequestDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
public class UsersService {
    private final UsersRepository usersRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}") String sender;

    public UsersService(UsersRepository usersRepository, RoleRepository roleRepository, UserRoleRepository userRoleRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, JavaMailSender mailSender) {
        this.usersRepository = usersRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }


    //Role API's

    public RoleEntity createRole(String role){
        RoleEntity newRole = new RoleEntity();
        newRole.setRole(role);
        return roleRepository.save(newRole);
    }

    public RoleEntity getRole(Long id){
        var savedRole = roleRepository.findById(id).orElseThrow(() -> new GlobalExceptionHandler.RoleNotFoundException());
        return savedRole;
    }

    public RoleEntity getRole(String role){
        var savedRole = roleRepository.findByRole(role).orElseThrow(() -> new GlobalExceptionHandler.RoleNotFoundException());
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
    }

    public UserEntity getUser(String username){
        return usersRepository.findByUsername(username).orElseThrow(() -> new GlobalExceptionHandler.UserNotFoundException(username));
    }

    public UserEntity getUser(Long id){
        return usersRepository.findById(id).orElseThrow(() -> new GlobalExceptionHandler.UserNotFoundException(id));
    }

   public UserEntity loginUser(String username, String password){
        var user = getUser(username);
        var passMatch = passwordEncoder.matches(password, user.getPassword());
        if(!passMatch) throw new GlobalExceptionHandler.InvalidCredentialException();
       return user;
   }

    public List<String> getAuthorities(Long id) {

        List<UserRoleEntity> userRoles = userRoleRepository.findAll();

        Set<UserRoleEntity> roles = new HashSet<>();

        for(UserRoleEntity userRole : userRoles){
            if(userRole.getUser().getId() == id){
                roles.add(userRole);
            }
        }

        return roles.stream()
                .map(role -> getRole(role.getRole().getId()).getRole())
                .collect(Collectors.toList());
    }

    public UserEntity getUserByEmail(String email){
        return usersRepository.findByEmail(email).orElseThrow(() -> new GlobalExceptionHandler.EmailNotFound());
    }

    public boolean sendMail(String to, String subject, String body){
        try{

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(sender);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);
        } catch (Exception ex){
            return false;
        }
        return true;
    }

    public boolean passwordChange(String email, String newPassword){
        try{
            UserEntity user = getUserByEmail(email);
            user.setPassword(passwordEncoder.encode(newPassword));
            usersRepository.save(user);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    //User-Role API's

    public String createUserRole(UserEntity userEntity, RoleEntity roleEntity){
        UserRoleEntity newUserRole = new UserRoleEntity();
        newUserRole.setUser(userEntity);
        newUserRole.setRole(roleEntity);
        userRoleRepository.save(newUserRole);
        return "Role Updated !";
   }
}
