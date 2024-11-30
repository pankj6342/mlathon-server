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

import java.util.List;


@RestController
@RequestMapping("/api/attachment")
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
        try{
            Attachment attachment = attachmentService.getAttachment(fileId);
        // Check if attachment has CSV data
        if (attachment.getCsvData() == null) {
            throw new Exception("Attachment does not contain parsed CSV data");
        }

        // Build the CSV content byte array
        StringBuilder csvContent = new StringBuilder();
        List<List<String>> csvData = attachmentService.convertByteArrayToCsvData(attachment.getCsvData());
            for (List<String> record : csvData) {
            csvContent.append(String.join(",", record));
            csvContent.append("\n"); // Add newline after each record
        }

        byte[] csvDataResponse = csvContent.toString().getBytes();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachment.getFileType())) // Can keep for original file type or use TEXT_PLAIN
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.getFileName() + ".csv\"")
                .body(new ByteArrayResource(csvDataResponse));
        } catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }
}
