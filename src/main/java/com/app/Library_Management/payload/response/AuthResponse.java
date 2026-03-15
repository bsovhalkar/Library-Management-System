package com.app.Library_Management.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.app.Library_Management.payload.dto.UserDTO;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthResponse {
    private String jwt;
    private String message;
    private String title;
    private UserDTO userDto;
}
