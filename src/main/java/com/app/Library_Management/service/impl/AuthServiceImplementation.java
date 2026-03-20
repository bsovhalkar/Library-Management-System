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

@Service
@RequiredArgsConstructor
public class AuthServiceImplementation implements AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordResetRepository passwordResetRepository;
    private final EmailService emailService;
    private final UserMapper userMapper;

    @Override
    public AuthResponse login(String username, String password) throws UserNotFoundException, PasswordDoesNotMatchExp {
        Authentication authentication = authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication);

        User user = userRepository.findByEmail(username);
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setTitle("Logged in successfully");
        authResponse.setMessage("Welcome Back " + username);
        authResponse.setJwt(jwt);
        authResponse.setUserDto(userMapper.toDTOWithoutPassword(user));

        return authResponse;
    }

    private Authentication authenticate(String username, String password) throws UserNotFoundException, PasswordDoesNotMatchExp {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UserNotFoundException("User not found with email: " + username);
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new PasswordDoesNotMatchExp("Invalid password");
        }

        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole());
        Collection<GrantedAuthority> authorities = Collections.singleton(authority);

        return new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                null,
                authorities
        );
    }

    @Override
    public AuthResponse signup(UserDTO userDTO) throws UserAlreadyExistException {
        User existingUser = userRepository.findByEmail(userDTO.getEmail());
        if (existingUser != null) {
            throw new UserAlreadyExistException("User with email " + userDTO.getEmail() + " already exists");
        }

        User newUser = userMapper.toEntity(userDTO);

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        User savedUser = userRepository.save(newUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                savedUser.getEmail(),
                savedUser.getPassword()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setTitle("Welcome " + savedUser.getFullName());
        authResponse.setMessage("Registered Successfully");
        authResponse.setUserDto(userMapper.toDTOWithoutPassword(savedUser));

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
        PasswordResetToken passwordResetToken = passwordResetRepository.findByToken(token)
                .orElseThrow(() -> new Exception("Invalid password reset token"));
        
        if (passwordResetToken.isExpired()) {
            passwordResetRepository.delete(passwordResetToken);
            throw new Exception("Password reset token is expired");
        }
        
        User user = passwordResetToken.getUser();
        
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        
        passwordResetRepository.delete(passwordResetToken);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setTitle("Password Reset Successfully");
        authResponse.setMessage("Your password has been reset. Please login with your new password.");
        authResponse.setUserDto(userMapper.toDTOWithoutPassword(user));
        return authResponse;
    }
}
