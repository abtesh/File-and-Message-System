package com.LIB.MessagingSystem.Repository;

import com.LIB.MessagingSystem.Model.Users;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 *
 *  @author Abenezer Teshome  - Date 17/aug/2024
 */

@Repository
public interface UserRepository extends MongoRepository<Users, String> {
//    public User getUserByEmail(String email);
//    public User getUserByUsername(String username);
   // public void saveUser(User user);
    Users findByUsername(String username);
    @Query("{ 'email': ?0, 'isActive': ?1 }")
    Optional<Users> findByEmailAndIsActive(String username, boolean active);

    Optional<Users> findByEmail(String username);
}
