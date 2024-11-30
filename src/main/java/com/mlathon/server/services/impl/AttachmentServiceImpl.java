package com.mlathon.server.services.impl;

import com.mlathon.server.entities.Attachment;
import com.mlathon.server.payload.FileDto;
import com.mlathon.server.repositories.AttachmentRepository;
import com.mlathon.server.services.AttachmentService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
            try (Reader reader = new InputStreamReader(new ByteArrayInputStream(attachment.getData()))) {
                Iterable<CSVRecord> records = new CSVParser(reader, CSVFormat.DEFAULT);  // Treat parser as Iterable
                List<List<String>> parsedRecords = new ArrayList<>();
                for (CSVRecord record : records) {
                    parsedRecords.add(record.toList());
                }
                byte[] csvDataAsBytes = convertCsvDataToByteArray(parsedRecords);
                attachment.setCsvData(csvDataAsBytes);
            } catch (IOException e) {
                throw new Exception("Error parsing CSV data: " + e.getMessage());
            }

            return attachmentRepository.save(attachment);
        } catch (Exception e) {
            throw new Exception("could not save file: " + fileName);
        }
    }

    public byte[] convertCsvDataToByteArray(List<List<String>> csvData) throws IOException {
        if (csvData == null || csvData.isEmpty()) {
            return new byte[0]; // Empty byte array for no data
        }

        StringBuilder csvContent = new StringBuilder();
        for (List<String> record : csvData) {
            csvContent.append(String.join(",", record));
            csvContent.append("\n"); // Add newline after each record
        }

        return csvContent.toString().getBytes();
    }

    public List<List<String>> convertByteArrayToCsvData(byte[] data) throws IOException {
        if (data == null || data.length == 0) {
            return new ArrayList<>(); // Empty list for no data
        }
        String csvContent = new String(data);
        Iterable<CSVRecord> records = new CSVParser(new StringReader(csvContent), CSVFormat.DEFAULT);  // Treat parser as Iterable
        List<List<String>> parsedRecords = new ArrayList<>();
        for (CSVRecord record : records) {
            parsedRecords.add(record.toList());
        }
        return parsedRecords;
    }



    @Override
    public Attachment getAttachment(String fileId) throws Exception {
        return attachmentRepository.findById(fileId).orElseThrow(() -> new Exception("File not found with this id: " + fileId));
    }

    @Override
    public FileDto uploadFile(MultipartFile file) throws Exception {
        Attachment attachment = saveAttachment(file);
        String downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/attachment/download/").path(attachment.getId()).toUriString();
        return new FileDto(file.getName(), downloadUrl, file.getContentType(), file.getSize());
    }
}
