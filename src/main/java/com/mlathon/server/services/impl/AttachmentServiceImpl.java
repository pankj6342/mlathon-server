package com.mlathon.server.services.impl;

import com.mlathon.server.entities.Attachment;
import com.mlathon.server.payload.FileDto;
import com.mlathon.server.repositories.AttachmentRepository;
import com.mlathon.server.services.AttachmentService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class AttachmentServiceImpl implements AttachmentService {
    private final AttachmentRepository attachmentRepository;

    public AttachmentServiceImpl(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    @Override
    public Attachment saveAttachment(MultipartFile file) throws Exception {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (fileName.contains("..")) {
                throw new Exception("Filename contains invalid path sequence: " + fileName);
            }

            Attachment attachment = new Attachment(fileName, file.getContentType(), file.getBytes());
            return attachmentRepository.save(attachment);
        } catch (Exception e) {
            throw new Exception("could not save file: " + fileName);
        }
    }

    @Override
    public Attachment getAttachment(String fileId) throws Exception {
        return attachmentRepository.findById(fileId).orElseThrow(() -> new Exception("File not found with this id: " + fileId));
    }

    @Override
    public FileDto uploadFile(MultipartFile file) throws Exception {
        Attachment attachment = saveAttachment(file);
        String downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath().path("/download/").path(attachment.getId()).toUriString();
        return new FileDto(file.getName(), downloadUrl, file.getContentType(), file.getSize());
    }
}
