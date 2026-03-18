package com.app.Library_Management.controller.user;

import com.app.Library_Management.exception.PasswordDoesNotMatchExp;
import com.app.Library_Management.exception.UserAlreadyExistException;
import com.app.Library_Management.exception.UserNotFoundException;
import com.app.Library_Management.payload.dto.UserDTO;
import com.app.Library_Management.payload.request.LoginRequest;
import com.app.Library_Management.payload.request.PasswordResetRequest;
import com.app.Library_Management.payload.request.PasswordResetTokenRequest;
import com.app.Library_Management.payload.request.SignupRequest;
import com.app.Library_Management.payload.response.ApiResponse;
import com.app.Library_Management.payload.response.AuthResponse;
import com.app.Library_Management.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:5173", "http://bs.com"}, allowCredentials = "true")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) throws UserNotFoundException , PasswordDoesNotMatchExp {
        AuthResponse authResponse = authService.login(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.status(HttpStatus.OK).body(authResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody SignupRequest signupRequest) throws UserAlreadyExistException {

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(signupRequest.getEmail());
        userDTO.setPassword(signupRequest.getPassword());
        userDTO.setFullName(signupRequest.getFullName());
        userDTO.setPhoneNumber(signupRequest.getPhoneNumber());
        userDTO.setRole(signupRequest.getRole() != null ? signupRequest.getRole() : "USER");
        userDTO.setUsername(signupRequest.getEmail());

        AuthResponse authResponse = authService.signup(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword(@Valid @RequestBody PasswordResetTokenRequest passwordResetTokenRequest) throws UserNotFoundException {
        authService.createPasswordResetToken(passwordResetTokenRequest.getEmail());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse("Password reset link sent to your email", true));
    }


    @PostMapping("/reset-password")
    public ResponseEntity<AuthResponse> resetPassword(@Valid @RequestBody PasswordResetRequest passwordResetRequest) throws Exception {
        AuthResponse authResponse = authService.resetPassword(passwordResetRequest.getToken(), passwordResetRequest.getNewPassword());
        return ResponseEntity.status(HttpStatus.OK).body(authResponse);
    }

    @GetMapping("/verify-token")
    public ResponseEntity<ApiResponse> verifyToken(@RequestParam String token) {
        try {
            if (token == null || token.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse("Token is required", false));
            }
            return ResponseEntity.ok(new ApiResponse("Token is valid", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("Token is invalid or expired", false));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<ApiResponse> health() {
        return ResponseEntity.ok(new ApiResponse("Auth service is running", true));
    }
}

