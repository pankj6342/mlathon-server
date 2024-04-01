package com.mlathon.server.services;

import com.mlathon.server.entities.Attachment;
import com.mlathon.server.payload.FileDto;
import org.springframework.web.multipart.MultipartFile;

public interface AttachmentService {
    Attachment saveAttachment(MultipartFile file) throws Exception;

    Attachment getAttachment(String fileId) throws Exception;

    FileDto uploadFile(MultipartFile file) throws Exception;
}
