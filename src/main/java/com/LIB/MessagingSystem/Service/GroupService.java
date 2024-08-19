package com.LIB.MessagingSystem.Service;

import com.LIB.MessagingSystem.Model.Group;
import com.LIB.MessagingSystem.Model.Message;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 *
 *  @author Abenezer Teshome  - Date 17/aug/2024
 */


public interface GroupService {
    Group createGroup(Group group);

    Group addMembers(String groupId, List<String> memberIds);

    Message createGroupMessage(String senderUsername, String groupId, String content, List<MultipartFile> attachments);

    Message sendMessage(String id);

    Group findGroupById(String groupId);
}
