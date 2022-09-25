package com.nav.ChatApi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupInviteDTO {
    private Long id;
    private Long userId;
    private Long groupId;
    private String receiverEmail;
    private boolean active;
}
