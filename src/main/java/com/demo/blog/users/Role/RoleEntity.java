package com.demo.blog.users.Role;

<<<<<<< HEAD
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Role")
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.NONE)
@Getter
@Setter
@Builder
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    private String role;
=======
public class RoleEntity {
>>>>>>> 5f1df2d0576ba6683e8c0efc4dff5fb4abde8dbd
}
