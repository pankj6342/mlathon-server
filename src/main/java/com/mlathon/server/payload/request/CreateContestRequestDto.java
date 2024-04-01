package com.mlathon.server.payload.request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateContestRequestDto {
    private String title;
    private String description;
    private Integer adminId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
