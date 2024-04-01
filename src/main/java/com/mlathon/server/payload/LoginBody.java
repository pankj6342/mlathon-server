package com.mlathon.server.payload;

import com.mlathon.server.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginBody {
    private String email;
    private String password;
    private UserRole role;
}
