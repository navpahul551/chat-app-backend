package com.nav.ChatApi.models;

import lombok.Data;

@Data
public class CreateGroupResponse {
    public int userId;
    public String groupName;
    public int groupId;

    public CreateGroupResponse() {}

    public CreateGroupResponse(int userId, String groupName, int groupId) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.userId = userId;
    }
}
