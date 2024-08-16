package com.LIB.MessagingSystem.Controller;

import com.LIB.MessagingSystem.Dto.GroupMessageDto;
import com.LIB.MessagingSystem.Model.Group;
import com.LIB.MessagingSystem.Model.Message;
import com.LIB.MessagingSystem.Service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @PostMapping("/create")
    public ResponseEntity<?> createGroup(@RequestBody Group group) {
        try {
            Group createdGroup = groupService.createGroup(group);
            return ResponseEntity.ok(createdGroup);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{groupId}/addMembers")
    public Group addMembers(@PathVariable String groupId, @RequestBody List<String> memberIds) {
        return groupService.addMembers(groupId, memberIds);
    }
    @PostMapping("/createMessage")
    public Message createGroupMessage(@ModelAttribute GroupMessageDto message) {
       return groupService.createGroupMessage(message.getSenderUsername(),
                message.getGroupId(),
                message.getContent(),
                message.getAttachments());

    }
    @PutMapping("/send/{groupId}")
    public ResponseEntity<?> sendMessage(@PathVariable("groupId") String groupId) {
        try {
            Message sentMessage = groupService.sendMessage(groupId);
            return ResponseEntity.ok(sentMessage);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/search/{groupId}")
    public ResponseEntity<Group> getGroupById(@PathVariable String groupId) {
        Group group = groupService.findGroupById(groupId);
        return ResponseEntity.ok(group);
    }
//    @DeleteMapping
//    public ResponseEntity<String> deleteGroupById(@RequestParam String groupId) {
//        Group group = groupService.findGroupById(groupId);
//        return ResponseEntity.ok("group Deleted Successfully");
//    }
}

