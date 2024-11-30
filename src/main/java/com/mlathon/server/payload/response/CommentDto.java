package com.mlathon.server.payload.response;
import com.mlathon.server.payload.UserInfoDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CommentDto {
    private Integer id;
    private String description;
    private LocalDateTime date;
    private Integer contestId;
    private UserInfoDto user;
}
