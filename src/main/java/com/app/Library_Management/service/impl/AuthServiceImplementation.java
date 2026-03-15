package com.app.Library_Management.service.impl;

import com.app.Library_Management.configuration.JwtProvider;
import com.app.Library_Management.exception.PasswordDoesNotMatchExp;
import com.app.Library_Management.exception.UserAlreadyExistException;
import com.app.Library_Management.exception.UserNotFoundException;
import com.app.Library_Management.mapper.UserMapper;
import com.app.Library_Management.model.PasswordResetToken;
import com.app.Library_Management.model.User;
import com.app.Library_Management.payload.dto.UserDTO;
import com.app.Library_Management.payload.response.AuthResponse;
import com.app.Library_Management.repository.PasswordResetRepository;
import com.app.Library_Management.repository.UserRepository;
import com.app.Library_Management.service.AuthService;
import com.app.Library_Management.service.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

/**
 * Implementation of AuthService for user authentication and authorization
 * Handles login, signup, and password reset operations
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImplementation implements AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordResetRepository passwordResetRepository;
    private final EmailService emailService;

    /**
     * Authenticates user and returns JWT token with updated last login
     * @param username the user's email
     * @param password the user's plain-text password
     * @return AuthResponse containing JWT token and user details
     * @throws BadCredentialsException if credentials are invalid
     */
    @Override
    public AuthResponse login(String username, String password) throws UserNotFoundException, PasswordDoesNotMatchExp {
        // Authenticate user credentials
        Authentication authentication = authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT token
        String jwt = jwtProvider.generateToken(authentication);

        // Update last login timestamp
        User user = userRepository.findByEmail(username);
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        // Build response with token and user details
        AuthResponse authResponse = new AuthResponse();
        authResponse.setTitle("Logged in successfully");
        authResponse.setMessage("Welcome Back " + username);
        authResponse.setJwt(jwt);
        authResponse.setUserDto(UserMapper.toDTOWithoutPassword(user));

        return authResponse;
    }

    /**
     * Authenticates user credentials and returns Authentication object
     * Validates username and password against database records
     *
     * @param username the user's email
     * @param password the user's plain-text password
     * @return authenticated Authentication object with authorities
     * @throws BadCredentialsException if username not found or password invalid
     */
    private Authentication authenticate(String username, String password) throws UserNotFoundException, PasswordDoesNotMatchExp {
        // Find user in database
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UserNotFoundException("User not found with email: " + username);
        }

        // Validate password against encoded password hash
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new PasswordDoesNotMatchExp("Invalid password");
        }

        // Load authorities from user role
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole());
        Collection<GrantedAuthority> authorities = Collections.singleton(authority);

        // Return authenticated Authentication object with user details and authorities
        return new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                null,
                authorities
        );
    }

    /**
     * Registers a new user with the provided credentials
     * @param userDTO the user data transfer object containing user details
     * @return AuthResponse containing JWT token and user details
     * @throws UserAlreadyExistException if email already registered
     */
    @Override
    public AuthResponse signup(UserDTO userDTO) throws UserAlreadyExistException {
        // Check if user already exists
        User existingUser = userRepository.findByEmail(userDTO.getEmail());
        if (existingUser != null) {
            throw new UserAlreadyExistException("User with email " + userDTO.getEmail() + " already exists");
        }

        // Create new user entity from DTO
        User newUser = UserMapper.toEntity(userDTO);

        // Encode password before saving (CRITICAL: never store plain passwords)
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        User savedUser = userRepository.save(newUser);

        // Create authentication and store in security context
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                savedUser.getEmail(),
                savedUser.getPassword()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT token
        String jwt = jwtProvider.generateToken(authentication);

        // Build response with token and user details (WITHOUT password)
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setTitle("Welcome " + savedUser.getFullName());
        authResponse.setMessage("Registered Successfully");
        authResponse.setUserDto(UserMapper.toDTOWithoutPassword(savedUser));

        return authResponse;
    }


    @Transactional
    public void createPasswordResetToken(String email) throws UserNotFoundException {
        String frontrdUrl = "http://localhost:5173/";
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("User Not Found with email: " + email);
        }
        String token = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusMinutes(5))
                .build();
        passwordResetRepository.save(passwordResetToken);
        String resetLink = frontrdUrl + token;
        String subject = "Password Reset Request";
        String body = "You have requested password reset request valid for (5 minutes) from " + LocalDateTime.now() + " Click->" + resetLink;
        emailService.sendEmail(email, subject, body);

    }


    @Transactional
    public AuthResponse resetPassword(String token, String newPassword) throws Exception {
        // Find password reset token by token string
        PasswordResetToken passwordResetToken = passwordResetRepository.findByToken(token)
                .orElseThrow(() -> new Exception("Invalid password reset token"));
        
        // Check if token is expired
        if (passwordResetToken.isExpired()) {
            passwordResetRepository.delete(passwordResetToken);
            throw new Exception("Password reset token is expired");
        }
        
        // Get user from passwordResetToken (NOT from token string!)
        User user = passwordResetToken.getUser();
        
        // Encode and update password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        
        // Delete the used token
        passwordResetRepository.delete(passwordResetToken);

        // Build and return response
        AuthResponse authResponse = new AuthResponse();
        authResponse.setTitle("Password Reset Successfully");
        authResponse.setMessage("Your password has been reset. Please login with your new password.");
        authResponse.setUserDto(UserMapper.toDTOWithoutPassword(user));
        return authResponse;
    }
}
