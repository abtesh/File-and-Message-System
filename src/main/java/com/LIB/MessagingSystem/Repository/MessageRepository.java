package com.LIB.MessagingSystem.Repository;

import com.LIB.MessagingSystem.Model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findBySenderIdAndReceiverId(String senderId, String receiverId);
    //public Message findById(ObjectId id);
    //List<Message> findByUsername(String senderUsername, String receiverUsername);
    List<Message> findByReceiverId(String receiverId);
    //Optional<Message> findBydocumentId(String documentId);
    Message deleteMessageById(String id);
    Message findByAttachmentsContaining(String fileName);
}
