package com.LIB.MessagingSystem.Repository;

import com.LIB.MessagingSystem.Model.FilePrivilege;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 *
 *  @author Abenezer Teshome  - Date 17/aug/2024
 */

public interface FilePrivilegeRepository extends MongoRepository <FilePrivilege, String> {
    Optional<FilePrivilege> findByMessageIdAndAttachmentIdAndUserId(String messageId, String attachmentId, String userId);
  // Optional <FilePrivilege> findByMessageIdAndUserId(String messageId, String userId);
   // Optional<FilePrivilege> findByFileNameAndUserId(String fileName, String userId);
  Optional<FilePrivilege> findByAttachmentIdAndUserId(String attachmentId, String userId);
    Optional<FilePrivilege> findByGroupIdAndAttachmentIdAndUserId(String groupId, String attachmentId, String userId);
    Optional<FilePrivilege> findByAttachmentIdAndUserIdAndGroupId(String attachmentId, String userId, String groupId);

}
