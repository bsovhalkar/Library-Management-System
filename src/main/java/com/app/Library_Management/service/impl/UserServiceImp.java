package com.app.Library_Management.service.impl;

import com.app.Library_Management.exception.UserNotFoundException;
import com.app.Library_Management.mapper.UserMapper;
import com.app.Library_Management.model.User;
import com.app.Library_Management.payload.dto.UserDTO;
import com.app.Library_Management.repository.UserRepository;
import com.app.Library_Management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public User getCurrentUser() throws UserNotFoundException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(email);
        if (currentUser == null) {
            throw new UserNotFoundException("User not found");
        }
        return currentUser;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOs = userMapper.toDTOList(users);
        return userDTOs;
    }
}
