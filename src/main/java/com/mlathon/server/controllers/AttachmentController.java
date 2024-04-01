package com.mlathon.server.controllers;

import com.mlathon.server.entities.Attachment;
import com.mlathon.server.payload.FileDto;
import com.mlathon.server.services.AttachmentService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@CrossOrigin
public class AttachmentController {
    private final AttachmentService attachmentService;

    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @PostMapping("/upload")
    public ResponseEntity<FileDto> uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        return new ResponseEntity<>(attachmentService.uploadFile(file), HttpStatus.OK);
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) throws Exception {
        Attachment attachment = attachmentService.getAttachment(fileId);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(attachment.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.getFileName() + "\"").body(new ByteArrayResource(attachment.getData()));
    }
}
