package com.LIB.MessagingSystem.Repository;

import com.LIB.MessagingSystem.Model.Group;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends MongoRepository<Group, String> {
    Group findByName(String name);
}
