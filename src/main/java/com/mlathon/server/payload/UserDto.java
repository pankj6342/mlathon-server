package com.mlathon.server.payload;

import com.mlathon.server.enums.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {
    private Integer id;
    private String name;
    private String email;
    private String password;
    private UserRole role;
}
