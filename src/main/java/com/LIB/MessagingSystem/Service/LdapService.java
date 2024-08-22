package com.LIB.MessagingSystem.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LdapService {
    private final LdapContextSource contextSource;


}
