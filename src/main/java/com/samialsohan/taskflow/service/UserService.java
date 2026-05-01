package com.samialsohan.taskflow.service;

import com.samialsohan.taskflow.dto.UserDto;
import com.samialsohan.taskflow.entity.Project;
import com.samialsohan.taskflow.entity.User;
import com.samialsohan.taskflow.exception.ResourceNotFoundException;
import com.samialsohan.taskflow.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<UserDto.Response>getAllUser(){
        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }
    public UserDto.Response createUser(UserDto.CreateRequest dto){
        User user = new User(dto.name(), dto.email());
        User saved = userRepository.save(user);
        return toResponse(saved);
    }
    public UserDto.Response getUserById(Long id){
        User user = findUserOrThrow(id);
        return  toResponse(user);
    }
    public void deleteUser (Long id){
        if(!userRepository.existsById(id)){
            throw new ResourceNotFoundException("User",id);
        }
        userRepository.deleteById(id);
    }
    public User findUserOrThrow(Long id)
    {
        return userRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("User",id));
    }
    private UserDto.Response toResponse(User user) {
        return new UserDto.Response(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }

}