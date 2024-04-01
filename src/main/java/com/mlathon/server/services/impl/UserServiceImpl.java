package com.mlathon.server.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.mlathon.server.enums.UserRole;
import com.mlathon.server.payload.UserDto;
import com.mlathon.server.repositories.UserRepository;
import org.springframework.stereotype.Service;

import com.mlathon.server.entities.User;
import com.mlathon.server.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //create
    @Override
    public UserDto createUser(UserDto userDto) {
        User user = this.DtoToUser(userDto);
//        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return this.UserToDto(this.userRepository.save(user));
    }

    //update
    @Override
    public UserDto updateUser(UserDto userDto, Integer userId) {
        User user = this.userRepository.findById(userId).orElseThrow();
        return this.UserToDto(user);
    }

    //delete
    @Override
    public void deleteUser(Integer userId) {
        User user = this.userRepository.findById(userId).orElseThrow();
        this.userRepository.delete(user);
    }

    //get
    @Override
    public UserDto getUserDto(Integer userId) {
        User user = this.userRepository.findById(userId).orElseThrow();
        return this.UserToDto(user);
    }

    @Override
    public User getUser(Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) throw new RuntimeException("User not found with id" + userId);
        return user.get();
    }

    //get all

    public List<UserDto> getAllUser() {
        List<User> users = this.userRepository.findAll();
        List<UserDto> allNotes = users.stream().map((user) -> this.UserToDto(user)).collect(Collectors.toList());
        return allNotes;
    }

    //login
    public UserDto userLogin(String email, String password, UserRole role) {
        User user = this.userRepository.findByEmailAndPasswordAndRole(email, password, role);
        return this.UserToDto(user);
    }

    public User DtoToUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setRole(userDto.getRole());
        return user;
    }

    public UserDto UserToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setRole(user.getRole());
        return userDto;
    }

}
    

