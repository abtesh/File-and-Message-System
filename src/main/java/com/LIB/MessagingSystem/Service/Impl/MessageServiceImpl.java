package com.LIB.MessagingSystem.Service.Impl;

import com.LIB.MessagingSystem.Model.Enums.RecipientTypes;
import com.LIB.MessagingSystem.Model.FilePrivilege;
import com.LIB.MessagingSystem.Model.Message;
import com.LIB.MessagingSystem.Model.Users;
import com.LIB.MessagingSystem.Repository.FilePrivilegeRepository;
import com.LIB.MessagingSystem.Repository.MessageRepository;
import com.LIB.MessagingSystem.Repository.UserRepository;
import com.LIB.MessagingSystem.Service.MessageService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 *
 *  @author Abenezer Teshome  - Date 17/aug/2024
 *  This service Implementation class creates messages for Individuals, sends messages,
 *  Creates and Updates privilages for users and deletes messages
 */



@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final FilePrivilegeRepository filePrivilegeRepository;
    @Value("${file.storage-path}")
    private String storagePath;

    public Message createMessage(String senderEmail, String receiverEmail, String content, List<MultipartFile> attachments) {
        // Find sender and receiver
        Optional<Users> sender = userRepository.findByEmail(senderEmail);
        Optional<Users> receiver = userRepository.findByEmail(receiverEmail);

        if (sender.isEmpty() || receiver.isEmpty()) {
            throw new RuntimeException("Sender or receiver not found");
        }

        // Handle attachments
        List<String> attachmentPaths = new ArrayList<>();
        if (attachments != null && !attachments.isEmpty()) {
            for (MultipartFile attachment : attachments) {
                String filePath = saveAttachment(attachment);
                attachmentPaths.add(filePath);
            }
        }
        // Create message
        Message message = new Message();
        message.setSenderId(sender.get().getId());
        message.setReceiverId(receiver.get().getId());
        message.setContent(content);
        message.setDraft(true);
        message.setRecipientType(RecipientTypes.INDIVIDUAL);
        message.setAttachments(attachmentPaths);
        message.setDate(new Date());

        // Save message and return
        Message savedMessage = messageRepository.save(message);
        System.out.println("Message saved with ID: " + savedMessage.getId());
        return savedMessage;
    }
    public Message sendMessage(String id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        if (!message.isDraft()) {
            throw new RuntimeException("Message is Already Sent and cannot be sent again");
        }
        // Mark the message as sent
        message.setDraft(false);
        message.setDate(new Date());
        // Save file privileges
        saveFilePrivilegesForMessage(message);
        return messageRepository.save(message);
    }

    private void saveFilePrivilegesForMessage(Message message) {
        List<FilePrivilege> filePrivileges = new ArrayList<>();
        String messageId = message.getId();

        for (String attachment : message.getAttachments()) {
            FilePrivilege privilege = new FilePrivilege();
            privilege.setMessageId(messageId);
            privilege.setAttachmentId(attachment); // Assuming attachment is the file name
            privilege.setUserId(message.getReceiverId());// Assign the receiver as a user with privileges
            privilege.setCanView(true);
            privilege.setCanDownload(true); // Default to not allowing download
            filePrivileges.add(privilege);
        }

        // Save all privileges to the repository
        filePrivilegeRepository.saveAll(filePrivileges);
    }

    public Message getMessage(String id) {
        return messageRepository.findById(id).orElseThrow(() -> new RuntimeException("Message not found"));
    }
    public List<Message> getMessagesBetweenUsers(String senderId, String receiverId){
        return messageRepository.findBySenderIdAndReceiverId(senderId, receiverId);
    }
    public List<Message> getMessagesForUser(String receiverId){
        return messageRepository.findByReceiverId(receiverId);
    }
    public Message findMessageByIdForUser(String messageId, String username) {
        Users user = userRepository.findByUsername(username);

        return messageRepository.findById(messageId)
                .filter(message -> message.getSenderId().equals(user.getId()) ||
                        message.getReceiverId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Message not found or access denied"));
    }
    public String saveAttachment(MultipartFile file) {
        String uploadDir = storagePath;  // Specify the correct path here
        File directory = new File(uploadDir);
        // Check if the directory exists, if not, create it
        if (!directory.exists()) {
            directory.mkdirs();  // Create the directory and any missing parent directories
            logger.info("Directory created: " + directory.getAbsolutePath());
        }

        try {
            // Generate a unique file name by concatenating a UUID with the original file name
            String originalFilename = file.getOriginalFilename();
            String uniqueFileName = UUID.randomUUID() + "_" + originalFilename;

            // Save the file to the specified directory with the unique name
            File dest = new File(directory, uniqueFileName);
            file.transferTo(dest);
            logger.info("File saved at: " + dest.getAbsolutePath());

            return uniqueFileName;  // Return the unique file name

        } catch (IOException e) {
            logger.error("Failed to save file", e);
            throw new RuntimeException("Failed to save file", e);
        }
    }
    public Message getMessageWithAttachmentCheck(String messageId, String attachmentId, String userId) {
        Optional<FilePrivilege> privilege = filePrivilegeRepository.findByMessageIdAndAttachmentIdAndUserId(messageId, attachmentId, userId);
        if (privilege.isPresent()) {
            return messageRepository.findById(messageId)
                    .orElseThrow(() -> new RuntimeException("Message not found"));
        } else {
            throw new RuntimeException("Access Denied");
        }
    }
    public Message deleteMessageById(String id){
        return messageRepository.deleteMessageById(id);
    }
    public boolean isUserAuthorizedForFile(String username, String fileName) {
        logger.info("Checking authorization for user: {} and file: {}", username, fileName);
        // Fetch message by attachment file name
        Message message = messageRepository.findByAttachmentsContaining(fileName);
        if (message == null) {
            logger.warn("No message found containing the file: {}", fileName);
            return false;
        }
        logger.info("Message found: {}", message.getId());
        logger.info("Sender ID: {}, Receiver ID: {}", message.getSenderId(), message.getReceiverId());

        // Fetch user details based on username
        Users user = userRepository.findByUsername(username); // Adjust this method according to your User repository
        if (user == null) {
            logger.warn("User not found: {}", username);
            return false;
        }

        // Check if the user is the sender or the receiver
        if (message.getSenderId().equals(user.getId()) || message.getReceiverId().equals(user.getId())) {
            logger.info("User is authorized to download the file.");
            return true;
        } else {
            logger.warn("User is not authorized. Access Denied.");
            return false;
        }
    }

}
