package com.auth_bot.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "telegram_user_table")
public class TelegramUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false, name = "user_id")
    private Long user_id;

    @Column(unique = true, nullable = false, name = "chat_id")
    private Long chat_id;

    @Column(unique = true, nullable = false, name = "auth_date")
    private String auth_date;

    @Column(unique = true, nullable = false, name = "hash")
    private String hash;

    public TelegramUser() {
    }

    public TelegramUser(Long id, String name, String username, Long chat_id, String auth_date, String hash, Long user_id) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.chat_id = chat_id;
        this.auth_date = auth_date;
        this.hash = hash;
        this.user_id = user_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getChat_id() {
        return chat_id;
    }

    public void setChat_id(Long chat_id) {
        this.chat_id = chat_id;
    }

    public String getAuth_date() {
        return auth_date;
    }

    public void setAuth_date(String auth_date) {
        this.auth_date = auth_date;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
