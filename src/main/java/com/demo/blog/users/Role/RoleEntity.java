package com.demo.blog.users.Role;

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
}
