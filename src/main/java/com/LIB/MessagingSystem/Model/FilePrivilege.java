package com.LIB.MessagingSystem.Model;

import com.LIB.MessagingSystem.Model.Enums.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "file_privileges")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilePrivilege {

    @Id
    private String id;
    private String messageId;
    private String attachmentId;
    private String userId;
    private AccessLevel accessLevel;
}
