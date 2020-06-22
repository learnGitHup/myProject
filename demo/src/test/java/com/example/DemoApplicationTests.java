package com.example;

import com.example.entity.User;
import com.example.mapper.UserMapper;
import com.example.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

    @Autowired
    UserMapper userMapper;

    @Test
    public void contextLoads() {
        userMapper.insertUser(new User("33", "悟空", "2000", "男", "561518181818"));
    }

}
