package com.LIB.MessagingSystem.Repository;

import com.LIB.MessagingSystem.Model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends MongoRepository<User, String> {
//    public User getUserByEmail(String email);
//    public User getUserByUsername(String username);
   // public void saveUser(User user);
    User findByUsername(String username);
}
