package com.mlathon.server.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileDto {
   private String fileName;
   private String downloadUrl;
   private String fileType;
   private long fileSize;
}
