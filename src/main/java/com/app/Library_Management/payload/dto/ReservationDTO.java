package com.app.Library_Management.payload.dto;

import com.app.Library_Management.domain.ReservationStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDTO {

    private Long id;

    @NotNull(message = "User id is required")
    private Long userId;

    private String userName;

    private String userEmail;

    @NotNull(message = "Book id is required")
    private Long bookId;

    private String bookTitle;

    private String bookAuthor;

    private String bookISBN;

    private String bookCoverPageURL;

    private ReservationStatus reservationStatus;

    private LocalDateTime reservedAt;

    private LocalDateTime availableAt;

    private LocalDateTime availableUntil;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime fulfilledAt;

    @NotNull(message = "Queue position is required")
    private Long queuePosition;

    private Boolean notificationSent;

    @Size(max = 1000, message = "Notes should not exceed 1000 characters")
    private String notes;

    private LocalDateTime cancelledAt;

    private Boolean isExpired;

    private Boolean canBeCancelled;

    private Long hoursUntilExpiry;
}
