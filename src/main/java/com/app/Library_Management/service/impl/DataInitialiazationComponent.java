package com.app.Library_Management.service.impl;


import com.app.Library_Management.domain.UserRole;
import com.app.Library_Management.model.User;
import com.app.Library_Management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitialiazationComponent implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void run(String... args){
        initializeAdminUser();
    }
    private void initializeAdminUser(){
        String adminEmail = "bsovhalkar@gmail.com";
        String adminPassword = "Bhavanesh@@7498";

        if(userRepository.findByEmail(adminEmail)==null){
            User user = new User();
            user.setEmail(adminEmail);
            user.setPassword(passwordEncoder.encode(adminPassword));
            user.setFullName("Bhavanesh S Ovhalkar");
            user.setRole(String.valueOf(UserRole.ROLE_ADMIN));
            userRepository.save(user);

        }

    }
}
