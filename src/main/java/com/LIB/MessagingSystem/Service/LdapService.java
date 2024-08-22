package com.LIB.MessagingSystem.Service;

import com.LIB.MessagingSystem.Dto.SecurityDtos.GenericResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LdapService {
    private final LdapFetchGroupService ldapFetchGroupService;

    public GenericResponseDto<Void> fetchGroupAndTheirMembersFromLdap() {

        ldapFetchGroupService.getAllGroupsAndMembers();
        return new GenericResponseDto<>(true, 200, "We are processing your request", null);

    }


}
