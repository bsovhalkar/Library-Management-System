package com.app.Library_Management.model;


import com.app.Library_Management.domain.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Book book;

    private ReservationStatus reservationStatus =  ReservationStatus.PENDING;

    private LocalDateTime reservedAt;
    private LocalDateTime availableAt;
    private LocalDateTime availableUntil;

    @CreationTimestamp
    @Column(nullable = false, updatable = false,name = "created_at")
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "fulfilled_at")
    private LocalDateTime fulfilledAt;
    @Column(name = "queue_position")
    private Long queuePosition;

    @Column(name = "notification_sent",nullable = false)
    private Boolean notificationSent = false;

    @Column(columnDefinition = "TEXT")
    private String notes;


    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    public boolean canBeCancelled() {
        return reservationStatus == ReservationStatus.PENDING ||
                reservationStatus == ReservationStatus.AVAILABLE;
    }

    public boolean hasExpired() {
        return reservationStatus==ReservationStatus.AVAILABLE && availableUntil != null && LocalDateTime.now().isAfter(availableUntil);
    }



}
