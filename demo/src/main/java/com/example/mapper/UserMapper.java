package com.example.mapper;

import com.example.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {

    void insertUser(User user);
}
