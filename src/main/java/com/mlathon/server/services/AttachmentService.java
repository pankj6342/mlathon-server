package com.mlathon.server.services;

import com.mlathon.server.entities.Attachment;
import com.mlathon.server.payload.FileDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AttachmentService {
    Attachment saveAttachment(MultipartFile file) throws Exception;

    Attachment getAttachment(String fileId) throws Exception;

    FileDto uploadFile(MultipartFile file) throws Exception;
    List<List<String>> convertByteArrayToCsvData(byte[] data) throws IOException;
    byte[] convertCsvDataToByteArray(List<List<String>> csvData) throws IOException;
}
