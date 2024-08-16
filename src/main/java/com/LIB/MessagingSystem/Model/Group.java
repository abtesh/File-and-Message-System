package com.LIB.MessagingSystem.Model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "groups")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Group {
    @Id
    private String id;
    private String name;
    private String makerId;
    private String checkerId;
    private String description;
    private String title;
    private Date creationDate;
    private List<String> members = new ArrayList<>();
}
