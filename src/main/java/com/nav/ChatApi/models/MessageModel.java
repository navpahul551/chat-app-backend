package com.nav.ChatApi.models;

import lombok.Data;

@Data
public class MessageModel {
    private String content;
    private Long id;
    private Long groupId;
    private String senderEmail;
}
