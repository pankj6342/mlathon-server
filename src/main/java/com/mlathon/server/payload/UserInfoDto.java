package com.mlathon.server.payload;
import com.mlathon.server.enums.UserRole;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
//@Serializable // Make UserInfoDto Serializable
public class UserInfoDto implements Serializable {
    private Integer id;
    private String name;
    private String email;
}
