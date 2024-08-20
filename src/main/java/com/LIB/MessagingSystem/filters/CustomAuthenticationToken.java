package com.LIB.MessagingSystem.filters;

import com.LIB.MessagingSystem.Dto.SecurityDtos.LdapUserDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
@Getter
@Setter

public class CustomAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private LdapUserDTO ldapUserDTO;

    public CustomAuthenticationToken(Object principal, Object credentials, LdapUserDTO ldapUserDTO) {
        super(principal, credentials);
    }


}
