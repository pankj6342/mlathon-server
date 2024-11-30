package com.mlathon.server.payload;
import lombok.*;
import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ContestInfoDto implements Serializable {
    private Integer contestId;
    private String title;
}
