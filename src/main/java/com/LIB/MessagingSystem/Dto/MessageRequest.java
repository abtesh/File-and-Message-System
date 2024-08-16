package com.LIB.MessagingSystem.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest {
    private String senderUsername;
    private String receiverUsername;
    private String content;
    private List<MultipartFile> attachments;
}
