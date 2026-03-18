package com.app.Library_Management.service;

import com.app.Library_Management.exception.UserNotFoundException;
import com.app.Library_Management.model.User;
import com.app.Library_Management.payload.dto.UserDTO;

import java.util.List;

public interface UserService {
    User getCurrentUser() throws UserNotFoundException;
    List<UserDTO> getAllUsers();

}
