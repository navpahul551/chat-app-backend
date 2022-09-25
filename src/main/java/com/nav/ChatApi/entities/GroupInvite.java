package com.nav.ChatApi.entities;


import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "group_invites")
@Data
@AllArgsConstructor
public class GroupInvite {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User sender;

    @Column(name = "receiver_email")
    private String receiverEmail;

    @Column(name = "active")
    private boolean active;

    @Column(name = "accepted")
    private boolean accepted;

    public GroupInvite() { }

    public GroupInvite(Group group, User sender, String receiverEmail, boolean active, boolean accepted) {
        this.group = group;
        this.sender = sender;
        this.receiverEmail = receiverEmail;
        this.active = active;
        this.accepted = accepted;
    }
}
