package com.LIB.MessagingSystem.Repository;

import com.LIB.MessagingSystem.Model.FilePrivilege;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface FilePrivilegeRepository extends MongoRepository <FilePrivilege, String> {
    Optional<FilePrivilege> findByMessageIdAndAttachmentIdAndUserId(String messageId, String attachmentId, String userId);

}
