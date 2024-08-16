package com.LIB.MessagingSystem.Dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageSearchRequestDto {
    private String messageId;
    private String username;
}
