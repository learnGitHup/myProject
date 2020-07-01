package com.example.controller;

import com.example.entity.User;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;


@RestController
public class UserController {
    @Autowired
    UserService userService;


    @RequestMapping("/findTest1")
    public List<User> findAll1() {
        return userService.findAll1();
    }

    @RequestMapping("/findTest2")
    public List<User> findAll2() {
        return userService.findAll2();
    }

    @RequestMapping("/insertUser")
    public String insertUser(){
        try {
            String age = String.valueOf(System.currentTimeMillis()).substring(0, 3);
            String phone = String.valueOf(System.currentTimeMillis()).substring(1, 10);
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            userService.insertUser1(new User(uuid, "悟空", age, "男", phone));
        } catch (Exception e) {
            return  "数据插入失败";
        }

        return  "数据插入成功";
    }



}
