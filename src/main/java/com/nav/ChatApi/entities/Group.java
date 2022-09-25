package com.nav.ChatApi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="message_groups")
@Data
public class Group implements Serializable {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="name", nullable = false, unique = true)
    private String name;

    @JsonIgnore
    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name="user_group", joinColumns=@JoinColumn(name="group_id", referencedColumnName="id"),
            inverseJoinColumns=@JoinColumn(name="user_id", referencedColumnName="id"))
    private List<User> users;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    private List<Message> messages;

    public Group() { }

    public Group(String name, List<User> users) {
        this.name = name;
        this.users = users;
    }

    public Group(Long id, String name, List<User> users){
        this.id = id;
        this.name = name;
        this.users = users;
    }
}
