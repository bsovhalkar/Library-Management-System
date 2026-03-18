package com.app.Library_Management.controller.user;

import com.app.Library_Management.exception.UserNotFoundException;
import com.app.Library_Management.model.User;
import com.app.Library_Management.payload.dto.UserDTO;
import com.app.Library_Management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<User> getMe() throws UserNotFoundException {
       User user = userService.getCurrentUser();
        return ResponseEntity.ok(user);
    }

    @GetMapping("/list")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        List<UserDTO> userDTOs = userService.getAllUsers();
        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }
}

