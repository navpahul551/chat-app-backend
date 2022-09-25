package com.nav.ChatApi.models;

import lombok.Data;

@Data
public class GroupModel {
    private Long userId;
    private String groupName;
    private Long groupId;

    public GroupModel(){}

    public GroupModel(Long userId, String groupName, Long groupId){
        this.userId = userId;
        this.groupName = groupName;
        this.groupId = groupId;
    }
}
