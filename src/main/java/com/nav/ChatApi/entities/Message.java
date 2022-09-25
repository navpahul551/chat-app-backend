package com.nav.ChatApi.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="messages")
@Data
public class Message {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @Column(name="content", nullable = false)
    private String content;

    public Message() { }

    public Message(Long id, String content, User user, Group group) {
        this.id = id;
        this.content = content;
        this.user = user;
        this.group = group;
    }
}
