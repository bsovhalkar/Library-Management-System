package com.app.Library_Management.payload.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishlistDto {
    private Long id;
    private Long userId;
    private String userFullName;
    private BookDTO bookDTO;
    private LocalDateTime addedAt;
    private String notes;
}
