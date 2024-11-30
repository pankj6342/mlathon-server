package com.mlathon.server.payload.response;

import com.mlathon.server.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginResponseDto {
    private String AuthenticationToken;
    private Integer userId;
    private String email;
    private String name;
    private UserRole role;
}
