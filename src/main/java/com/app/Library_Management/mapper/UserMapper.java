package com.app.Library_Management.mapper;

import com.app.Library_Management.domain.UserRole;
import com.app.Library_Management.model.User;
import com.app.Library_Management.payload.dto.UserDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
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
                user.getEmail(),
                user.getLastLogin(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    public User toEntity(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }

        return User.builder()
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .phoneNumber(userDTO.getPhoneNumber())
                .fullName(userDTO.getFullName())
                .role(String.valueOf(UserRole.ROLE_USER))
                .build();
    }

    public User toEntityForUpdate(UserDTO userDTO) {
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

    public UserDTO toDTOWithoutPassword(User user) {
        if (user == null) {
            return null;
        }

        UserDTO dto = toDTO(user);
        dto.setPassword(null);
        return dto;
    }

    public User updateEntityFromDTO(UserDTO userDTO, User user) {
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

    public List<UserDTO> toDTOList(List<User> users) {
        if (users == null) {
            return null;
        }
        return users.stream()
                .map(this::toDTOWithoutPassword)
                .collect(Collectors.toList());
    }
}
