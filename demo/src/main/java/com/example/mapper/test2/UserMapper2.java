package com.example.mapper.test2;

import com.example.entity.User;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Repository
public interface UserMapper2 {

    @Select("select * from user")
    List<User> findAll();

    void insertUser(User user);
}
