package com.LIB.MessagingSystem.Service.Impl;

import com.LIB.MessagingSystem.Model.Users;
import com.LIB.MessagingSystem.Repository.UserRepository;
import com.LIB.MessagingSystem.exceptions.AccountBlockedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UserRepository usersRepository;

    public Users loadUserByUsername(String username) {
        return usersRepository.findByEmailAndIsActive(username,true).orElseThrow(()->new UsernameNotFoundException("Username " + username + " not found"));
    }

    public void saveUsers(String username, String name, String id){
        Users users = usersRepository.findByEmail(username).orElse(null);
        if(users == null){
            Users user = Users.builder().id(id).email(username).isActive(true).name(name).build();
            usersRepository.save(user);
        }
        else{
            if(users.isActive())
                return;
            else{
                throw new AccountBlockedException("your account is blocked");
            }
        }

    }
}
