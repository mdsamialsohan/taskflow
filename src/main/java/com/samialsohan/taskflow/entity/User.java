package com.samialsohan.taskflow.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate(){
        this.createdAt = Instant.now();
    }
    public User(){}
    public User(String name, String email){
        this.name = name;
        this.email = email;
    }

    public Long getId()
    {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName(){
        return name;
    }
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public Instant getCreatedAt(){
        return createdAt;
    }
}
