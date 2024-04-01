package com.mlathon.server.controllers;

import java.util.List;

import com.mlathon.server.entities.User;
import com.mlathon.server.payload.LoginBody;
import com.mlathon.server.payload.UserDto;
import com.mlathon.server.payload.response.GetUserResponse;
import com.mlathon.server.payload.response.LoginResponseDto;
import com.mlathon.server.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@CrossOrigin
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    //create
    @PostMapping("/register")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto user) {
        UserDto userDto = this.userService.createUser(user);
        return ResponseEntity.ok(userDto);
    }

    //update
    @PostMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto user, @PathVariable Integer userId) {
        UserDto userDto = this.userService.updateUser(user, userId);
        return ResponseEntity.ok(userDto);
    }

    //delete
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer userId) {
        this.userService.deleteUser(userId);
        return ResponseEntity.ok("User deleted successfully");
    }

    //get
    @GetMapping("/{userId}")
    public ResponseEntity<GetUserResponse> getUser(@PathVariable Integer userId) {
        User user = this.userService.getUser(userId);
        return ResponseEntity.ok(new GetUserResponse(user.getId(), user.getName(), user.getRole(), user.getEmail(), user.getCreatedContests(), user.getEnrolledContests()));
    }

    //get all
    @GetMapping("/")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> userDtos = this.userService.getAllUser();
        return ResponseEntity.ok(userDtos);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> userLogin(@RequestBody LoginBody loginBody) {
        LoginResponseDto apiRes = new LoginResponseDto();
        try {
            UserDto userDto = this.userService.userLogin(loginBody.getEmail(), loginBody.getPassword(), loginBody.getRole());
            apiRes = new LoginResponseDto("", userDto.getId(), userDto.getEmail(), userDto.getName());
            return ResponseEntity.ok(apiRes);
        } catch (Exception e) {
            System.out.println("Error in logging in: " + e.getMessage());
            return new ResponseEntity<LoginResponseDto>(apiRes, HttpStatus.UNAUTHORIZED);
        }
    }


}
