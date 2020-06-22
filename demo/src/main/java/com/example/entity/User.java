package com.example.entity;

import lombok.Data;


@Data
public class User {
    private String userId;
    private String userName;
    private String age;
    private String sex;
    private String phone;


    public User(String userId, String userName, String age, String sex, String phone) {
        this.userId = userId;
        this.userName = userName;
        this.age = age;
        this.sex = sex;
        this.phone = phone;
    }

    public User() {
    }
}
