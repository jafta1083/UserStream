package com.userstream.service;

import java.time.LocalDateTime;

public class UserReport {
    private int id;
    private String name;
    private String surname;
    private String email;
    private LocalDateTime createdAt;

    public UserReport() {
    }

    public UserReport(int id, String name, String surname, String email) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.createdAt = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String toCSVRow() {
        return String.format("%s,%s,%s,%s,%s",
                id,
                name,
                surname,
                email,
                createdAt
        );
    }

    public static String getCSVHeader() {
        return "ID,Name,Surname,Email,Created At";
    }
}
