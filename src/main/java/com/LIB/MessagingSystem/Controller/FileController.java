package com.LIB.MessagingSystem.Controller;


import com.LIB.MessagingSystem.Service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/downloadFiles")
@RequiredArgsConstructor
public class FileController {
    @Value("${file.storage-path}")
    private String storagePath;
    private final MessageService messageService;

    @GetMapping("/viewAttachment")
    public ResponseEntity<Resource> viewAttachment(@RequestParam String fileName, @RequestParam String username) throws IOException {
        // Validate user's access to the file
        if (!messageService.isUserAuthorizedForFile(username, fileName)) {
            throw new RuntimeException("Access Denied");
        }

        Path path = Paths.get(storagePath).resolve(fileName);
        Resource resource = new UrlResource(path.toUri());

        if (resource.exists() || resource.isReadable()) {
            String contentType = Files.probeContentType(path);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + path.getFileName().toString() + "\"")
                    .body(resource);
        } else {
            throw new RuntimeException("File not found or not readable");
        }
    }


}
