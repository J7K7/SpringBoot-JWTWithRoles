package com.demo.blog.users.dtos;

import lombok.Data;

import java.util.List;

@Data
public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
    private String token;
    private List<String> roles;
}
