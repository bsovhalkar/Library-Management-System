package com.app.Library_Management.mapper;

import com.app.Library_Management.domain.UserRole;
import com.app.Library_Management.model.User;
import com.app.Library_Management.payload.dto.UserDTO;
import org.springframework.stereotype.Service;

/**
 * Mapper for converting between User entity and UserDTO
 * Provides bidirectional mapping for API requests/responses
 */
@Service
public class UserMapper {

    /**
     * Converts User entity to UserDTO
     * @param user the User entity
     * @return UserDTO with all relevant fields mapped
     */
    public static UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

        return new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getPhoneNumber(),
                user.getFullName(),
                user.getRole(),
                user.getEmail(), // username same as email
                user.getLastLogin(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    /**
     * Converts UserDTO to User entity
     * @param userDTO the UserDTO object
     * @return User entity with all relevant fields mapped
     */
    public static User toEntity(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }

        return User.builder()
                .id(userDTO.getId())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .phoneNumber(userDTO.getPhoneNumber())
                .fullName(userDTO.getFullName())
                .role(String.valueOf(UserRole.ROLE_USER))
                .lastLogin(userDTO.getLastLogin())
                .createdAt(userDTO.getCreatedAt())
                .updatedAt(userDTO.getUpdatedAt())
                .build();
    }

    /**
     * Converts User entity to UserDTO excluding password (safe for API responses)
     * @param user the User entity
     * @return UserDTO without password field
     */
    public static UserDTO toDTOWithoutPassword(User user) {
        if (user == null) {
            return null;
        }

        UserDTO dto = toDTO(user);
        dto.setPassword(null); // Exclude password from response
        return dto;
    }

    /**
     * Updates an existing User entity with data from UserDTO
     * @param userDTO the source UserDTO
     * @param user the target User entity to update
     * @return the updated User entity
     */
    public static User updateEntityFromDTO(UserDTO userDTO, User user) {
        if (userDTO == null || user == null) {
            return user;
        }

        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail());
        }
        if (userDTO.getPassword() != null) {
            user.setPassword(userDTO.getPassword());
        }
        if (userDTO.getPhoneNumber() != null) {
            user.setPhoneNumber(userDTO.getPhoneNumber());
        }
        if (userDTO.getFullName() != null) {
            user.setFullName(userDTO.getFullName());
        }
        if (userDTO.getRole() != null) {
            user.setRole(userDTO.getRole());
        }

        return user;
    }
}
