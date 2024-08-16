package com.LIB.MessagingSystem.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private String id;
    private String userId= UUID.randomUUID().toString();
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
