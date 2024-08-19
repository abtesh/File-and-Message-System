package com.LIB.MessagingSystem.Service.Impl;

import com.LIB.MessagingSystem.Model.Enums.RecipientTypes;
import com.LIB.MessagingSystem.Model.Group;
import com.LIB.MessagingSystem.Model.Message;
import com.LIB.MessagingSystem.Model.User;
import com.LIB.MessagingSystem.Repository.GroupRepository;
import com.LIB.MessagingSystem.Repository.MessageRepository;
import com.LIB.MessagingSystem.Repository.UserRepository;
import com.LIB.MessagingSystem.Service.GroupService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 *  @author Abenezer Teshome  - Date 17/aug/2024
 *  This service implementation class creates new groups, add members to the groups,
 *  create messages to groups and send messages
 */


@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private static final Logger logger = LoggerFactory.getLogger(GroupServiceImpl.class);

    private final GroupRepository groupRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;


    public Group createGroup(Group group) {
       return groupRepository.save(group);
    }

    public Group addMembers(String groupId, List<String> memberIds) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        List<String> existingMembers = group.getMembers();
        List<String> newMembers = new ArrayList<>();

        for (String memberId : memberIds) {
            // Check if the user exists in the system
            if (!userRepository.existsById(memberId)) {
                throw new RuntimeException("User with ID " + memberId + " does not exist");
            }

            // Check if the user is already a member of the group
            if (existingMembers.contains(memberId)) {
                throw new RuntimeException("User with ID " + memberId + " is already a member of the group");
            } else {
                newMembers.add(memberId);
            }
        }

        group.getMembers().addAll(newMembers);
        return groupRepository.save(group);
    }


    public Message createGroupMessage(String senderUsername, String groupId, String content, List<MultipartFile> attachments) {
        User sender = userRepository.findByUsername(senderUsername);
       Group group = groupRepository.findByName(groupId);

        List<String> attachmentPaths = new ArrayList<>();
        if (attachments != null) {
            for (MultipartFile attachment : attachments) {
                String filePath = saveAttachment(attachment);
                attachmentPaths.add(filePath);
            }
        }
        Message message = new Message();
        message.setSenderId(sender.getId());
        message.setGroupId(groupId);
        message.setContent(content);
        message.setDraft(true);
        message.setRecipientType(RecipientTypes.GROUP);
        message.setAttachments(attachmentPaths);
        message.setDate(new Date());

       return messageRepository.save(message);
    }
    public String saveAttachment(MultipartFile file) {
        String uploadDir = "C:\\Users\\abenezert\\Desktop\\Files for test";  // Specify the correct path here
        File directory = new File(uploadDir);

        // Check if the directory exists, if not, create it
        if (!directory.exists()) {
            directory.mkdirs();  // Create the directory and any missing parent directories
            logger.info("Directory created: " + directory.getAbsolutePath());
        }

        try {
            // Save the file to the specified directory
            File dest = new File(directory, file.getOriginalFilename());
            file.transferTo(dest);
            logger.info("File saved at: " + dest.getAbsolutePath());
        } catch (IOException e) {
            logger.error("Failed to save file", e);
            throw new RuntimeException("Failed to save file", e);
        }
        return file.getOriginalFilename();
    }
    public Message sendMessage(String id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        if(!message.isDraft()){
            throw new RuntimeException("Message is Already sent and can't be sent Again");
        }
        message.setDraft(false);  // Mark the message as sent
        message.setDate(new Date());
       return messageRepository.save(message);
    }
    public Group findGroupById(String groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found with id: " + groupId));
    }
}
