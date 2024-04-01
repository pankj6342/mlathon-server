package com.mlathon.server.entities;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
@NoArgsConstructor
@Getter
@Setter
public class Attachment {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name="uuid", strategy = "uuid2")
    private String id;
    private String fileName;
    private String fileType;
    @Lob
    private byte[] data;

    public Attachment(String fileName, String fileType, byte[] data){
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
    }
}

