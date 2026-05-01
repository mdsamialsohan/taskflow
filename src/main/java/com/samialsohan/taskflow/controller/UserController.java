package com.samialsohan.taskflow.controller;

import com.samialsohan.taskflow.dto.UserDto;
import com.samialsohan.taskflow.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }
    @GetMapping
    public List<UserDto.Response>getAllUsers(){
        return userService.getAllUser();
    }

    @PostMapping
    public ResponseEntity<UserDto.Response>createUser(@Valid @RequestBody UserDto.CreateRequest dto){
        UserDto.Response response = userService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public UserDto.Response getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
    }

    @PutMapping("/{id}") // replace the resource with new data, UPDATE
    public UserDto.Response updateUser(@PathVariable Long id,
                                       @Valid @RequestBody UserDto.UpdateRequest dto){
        return userService.updateUser(id,dto);
    }
}
