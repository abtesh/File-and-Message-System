package com.LIB.MessagingSystem.Service;

import com.LIB.MessagingSystem.Model.Message;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 *
 *  @author Abenezer Teshome  - Date 17/aug/2024
 */


public interface MessageService {
    Message createMessage(String senderUsername, String receiverUsername, String content, List<MultipartFile> attachments);
    Message sendMessage(String id);
    Message getMessage(String id);
    List<Message> getMessagesBetweenUsers(String senderId, String receiverId);
    List<Message> getMessagesForUser(String receiverId);
    Message findMessageByIdForUser(String messageId, String username);
    String saveAttachment(MultipartFile file); // Consider if this should be in the interface
    Message getMessageWithAttachmentCheck(String messageId, String attachmentId, String userId);
    Message deleteMessageById(String id);
    boolean isUserAuthorizedForFile(String username, String fileName);
}

