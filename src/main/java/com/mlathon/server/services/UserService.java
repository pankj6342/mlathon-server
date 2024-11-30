package com.mlathon.server.services;

import java.util.List;

import com.mlathon.server.enums.UserRole;
import com.mlathon.server.payload.UserDto;
import com.mlathon.server.entities.User;

public interface UserService {
    //create
    UserDto createUser(UserDto userDto);
    //update
    UserDto updateUser(UserDto userDto,Integer userId);
    //delete
    void deleteUser(Integer userId);
    //get
    UserDto getUserDto(Integer userId);
    User getUser(Integer userId);
    //getAll
    List<UserDto> getAllUser();

    //user login
    UserDto userLogin(String email, String password);
    
}
