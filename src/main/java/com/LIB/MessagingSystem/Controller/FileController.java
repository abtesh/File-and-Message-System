package com.LIB.MessagingSystem.Controller;


import com.LIB.MessagingSystem.Model.FilePrivilege;
import com.LIB.MessagingSystem.Model.Message;
import com.LIB.MessagingSystem.Repository.FilePrivilegeRepository;
import com.LIB.MessagingSystem.Repository.MessageRepository;
import com.LIB.MessagingSystem.Service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@RestController
@RequestMapping("/downloadFiles")
@RequiredArgsConstructor
public class FileController {
    @Value("${file.storage-path}")
    private String storagePath;
    private final MessageService messageService;
    private final MessageRepository messageRepository;
    private final FilePrivilegeRepository filePrivilegeRepository;

    @GetMapping("/viewAttachment")
    public ResponseEntity<Resource> viewAttachment(@RequestParam String fileName, @RequestParam String username) throws IOException {
        // Retrieve the message by the attachment file name
        Message message = messageRepository.findByAttachmentsContaining(fileName);
        if (message == null) {
            throw new RuntimeException("Message not found for the given attachment");
        }

        // Validate user's access to the file
        FilePrivilege privilege = filePrivilegeRepository.findByAttachmentIdAndUserId(fileName, username)
                .orElseThrow(() -> new RuntimeException("Access Denied"));

        if (!privilege.isCanView() && !privilege.isCanDownload()) {
            throw new RuntimeException("You don't have permission to view or download this file");
        }

        Path path = Paths.get(storagePath).resolve(fileName);
        Resource resource = new UrlResource(path.toUri());

        if (resource.exists() || resource.isReadable()) {
            String contentType = Files.probeContentType(path);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            // Determine if the file should be viewed inline or downloaded
            String contentDisposition = privilege.isCanDownload()
                    ? "attachment; filename=\"" + path.getFileName().toString() + "\""
                    : "inline; filename=\"" + path.getFileName().toString() + "\"";

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                    .body(resource);
        } else {
            throw new RuntimeException("File not found or not readable");
        }
    }

    @PostMapping("/setFilePrivilege")
    public ResponseEntity<?> setFilePrivilege(@RequestParam String messageId, @RequestParam String attachmentId,
                                              @RequestParam String userId, @RequestParam boolean canView,
                                              @RequestParam boolean canDownload) {
        // Debugging logs
        System.out.println("Received Request: messageId=" + messageId + ", attachmentId=" + attachmentId + ", userId=" + userId);
        System.out.println("Privileges: canView=" + canView + ", canDownload=" + canDownload);

        // Check if the query returns something
        Optional<FilePrivilege> optionalPrivilege = filePrivilegeRepository.findByMessageIdAndAttachmentIdAndUserId(messageId, attachmentId, userId);
        if (optionalPrivilege.isPresent()) {
            System.out.println("Privilege found: Updating existing privilege");
        } else {
            System.out.println("Privilege not found: Creating new privilege");
        }

        FilePrivilege privilege;
        if (optionalPrivilege.isPresent()) {
            privilege = optionalPrivilege.get();
        } else {
            privilege = new FilePrivilege();
            privilege.setMessageId(messageId);
            privilege.setAttachmentId(attachmentId);
            privilege.setUserId(userId);
        }

        privilege.setCanView(canView);
        privilege.setCanDownload(canDownload);

        // Save privilege and log the operation
        filePrivilegeRepository.save(privilege);
        System.out.println("Privileges updated successfully");

        return ResponseEntity.ok("Privileges updated successfully");
    }



}
