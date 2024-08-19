package com.LIB.MessagingSystem.Controller;

import com.LIB.MessagingSystem.Dto.MessageRequest;
import com.LIB.MessagingSystem.Dto.MessageSearchRequestDto;
import com.LIB.MessagingSystem.Model.Message;
import com.LIB.MessagingSystem.Model.User;
import com.LIB.MessagingSystem.Repository.UserRepository;
import com.LIB.MessagingSystem.Service.Impl.MessageServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 *
 *  @author Abenezer Teshome  - Date 17/aug/2024
 */


@RestController
@RequestMapping("/messenger")
@RequiredArgsConstructor
public class MessageController {
    private final MessageServiceImpl messageServiceImpl;
    private final UserRepository userRepository;


    @PostMapping("/createUser")
    public String createUser(@RequestBody User user) {
        userRepository.save(user);
        return "User created";
    }

    @PostMapping("/create")
    public Message createMessage(@ModelAttribute MessageRequest message) {
        System.out.println("Received message request: " + message);
        return messageServiceImpl.createMessage(message.getSenderUsername(),
                message.getReceiverUsername(),
                message.getContent(),
                message.getAttachments());
    }

    @PostMapping("/send/{objectId}")
    public ResponseEntity<?> sendMessage(@PathVariable("objectId") String objectId) {
        try {
            Message sentMessage = messageServiceImpl.sendMessage(objectId);
            return ResponseEntity.ok(sentMessage);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<Message> getMessage(@PathVariable("id") String id) {
        Message message = messageServiceImpl.getMessage(id);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/{senderId}/{receiverId}")
    public List<Message> getMessageBetweenUsers(@PathVariable("senderId") String senderId, @PathVariable("receiverId") String receiverId) {
        return messageServiceImpl.getMessagesBetweenUsers(senderId, receiverId);
    }

    @GetMapping("/inbox/{receiverId}")
    public List<Message> getInboxMessages(@PathVariable("receiverId") String receiverId) {
        return messageServiceImpl.getMessagesForUser(receiverId);
    }

    @PostMapping("/search")
    public ResponseEntity<Message> getMessageById(@RequestBody MessageSearchRequestDto searchRequest) {
        Message message = messageServiceImpl.findMessageByIdForUser(
                searchRequest.getMessageId(),
                searchRequest.getUsername()
        );
        return ResponseEntity.ok(message);
    }
    @GetMapping("/{messageId}/attachments/{attachmentId}")
    public Message getMessageWithAttachmentCheck(@PathVariable String messageId,
                                                 @PathVariable String attachmentId,
                                                 @RequestParam String userId) {
        return messageServiceImpl.getMessageWithAttachmentCheck(messageId, attachmentId, userId);

    }
    @DeleteMapping("/delete/{messageId}")
    public String deleteMessage(@PathVariable("messageId") String messageId) {
        messageServiceImpl.deleteMessageById(messageId);
        return "Message deleted";
    }
}
