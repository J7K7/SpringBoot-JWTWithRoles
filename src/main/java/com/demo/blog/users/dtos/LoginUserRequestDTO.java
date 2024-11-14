package com.demo.blog.users.dtos;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.Setter;

@Data
@Setter(AccessLevel.NONE)
public class LoginUserRequestDTO {
    @NonNull
    private String username;
    @NonNull
    private String password;
}
