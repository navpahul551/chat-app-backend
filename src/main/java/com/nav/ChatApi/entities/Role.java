package com.nav.ChatApi.entities;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Entity
@Table(name="roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(name="name")
    private Rolename name;

    public Role() { }

    public Role(Rolename name){
        this.name = name;
    }

    public Rolename getName(){
        return this.name;
    }

    @Override
    public String toString(){
        return name.name();
    }

    public enum Rolename {
        ROLE_ADMIN,
        ROLE_USER
    }
}

