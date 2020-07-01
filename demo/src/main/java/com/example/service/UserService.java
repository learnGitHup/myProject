package com.example.service;

import com.example.entity.User;
import com.example.mapper.test1.UserMapper;
import com.example.mapper.test2.UserMapper2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserMapper2 userMapper2;


    public List<User> findAll1(){
        return userMapper.findAll();
    }
    public List<User> findAll2(){
        return userMapper2.findAll();
    }

    public void insertUser1(User user) {
        userMapper.insertUser(user);
        userMapper2.insertUser(user);
    }

}
