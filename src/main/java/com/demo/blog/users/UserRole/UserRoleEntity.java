package com.demo.blog.users.UserRole;

import com.demo.blog.users.Role.RoleEntity;
import com.demo.blog.users.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "UserRole",
        uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "role_id"})
        }
)
@NoArgsConstructor(access = AccessLevel.NONE)
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserRoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private RoleEntity role;

}
