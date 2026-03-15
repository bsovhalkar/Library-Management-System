package com.app.Library_Management.service;

import com.app.Library_Management.exception.PasswordDoesNotMatchExp;
import com.app.Library_Management.exception.UserAlreadyExistException;
import com.app.Library_Management.exception.UserNotFoundException;
import com.app.Library_Management.payload.dto.UserDTO;
import com.app.Library_Management.payload.response.AuthResponse;

public interface AuthService {
    AuthResponse login(String username, String password) throws UserNotFoundException, PasswordDoesNotMatchExp;
    AuthResponse signup(UserDTO userDTO) throws UserAlreadyExistException;
    void createPasswordResetToken(String email) throws UserNotFoundException;
    AuthResponse resetPassword(String token, String newPassword) throws Exception;


}
