package com.app.Library_Management.mapper;

import com.app.Library_Management.model.Book;
import com.app.Library_Management.model.Reservation;
import com.app.Library_Management.model.User;
import com.app.Library_Management.payload.dto.ReservationDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class ReservationMapper {

    public ReservationDTO toDTO(Reservation reservation) {
        if (reservation == null) {
            return null;
        }

        boolean isExpired = reservation.hasExpired();
        boolean canBeCancelled = reservation.canBeCancelled();

        Long hoursUntilExpiry = null;
        if (reservation.getAvailableUntil() != null && !isExpired) {
            hoursUntilExpiry = ChronoUnit.HOURS.between(LocalDateTime.now(), reservation.getAvailableUntil());
        }

        ReservationDTO.ReservationDTOBuilder builder = ReservationDTO.builder()
                .id(reservation.getId())
                .reservationStatus(reservation.getReservationStatus())
                .reservedAt(reservation.getReservedAt())
                .availableAt(reservation.getAvailableAt())
                .availableUntil(reservation.getAvailableUntil())
                .createdAt(reservation.getCreatedAt())
                .updatedAt(reservation.getUpdatedAt())
                .fulfilledAt(reservation.getFulfilledAt())
                .queuePosition(reservation.getQueuePosition())
                .notificationSent(reservation.getNotificationSent())
                .notes(reservation.getNotes())
                .cancelledAt(reservation.getCancelledAt())
                .isExpired(isExpired)
                .canBeCancelled(canBeCancelled)
                .hoursUntilExpiry(hoursUntilExpiry);

        User user = reservation.getUser();
        if (user != null) {
            builder.userId(user.getId())
                    .userName(user.getFullName())
                    .userEmail(user.getEmail());
        }

        Book book = reservation.getBook();
        if (book != null) {
            builder.bookId(book.getId())
                    .bookTitle(book.getTitle())
                    .bookAuthor(book.getAuthor())
                    .bookISBN(book.getIsbn())
                    .bookCoverPageURL(book.getCoverImgUrl());
        }

        return builder.build();
    }

    public Reservation toEntity(ReservationDTO dto, User user, Book book) {
        if (dto == null) {
            return null;
        }

        return Reservation.builder()
                .user(user)
                .book(book)
                .reservationStatus(dto.getReservationStatus())
                .reservedAt(dto.getReservedAt())
                .availableAt(dto.getAvailableAt())
                .availableUntil(dto.getAvailableUntil())
                .fulfilledAt(dto.getFulfilledAt())
                .queuePosition(dto.getQueuePosition())
                .notificationSent(dto.getNotificationSent())
                .notes(dto.getNotes())
                .cancelledAt(dto.getCancelledAt())
                .build();
    }

    public List<ReservationDTO> toDTOList(List<Reservation> reservations) {
        if (reservations == null) {
            return List.of();
        }
        return reservations.stream().map(this::toDTO).toList();
    }

    public List<Reservation> toEntityList(List<ReservationDTO> dtos, User user, Book book) {
        if (dtos == null) {
            return List.of();
        }
        return dtos.stream().map(dto -> toEntity(dto, user, book)).toList();
    }

    public void updateEntityFromDTO(ReservationDTO dto, Reservation reservation, User user, Book book) {
        if (dto == null || reservation == null) {
            return;
        }

        if (user != null) {
            reservation.setUser(user);
        }
        if (book != null) {
            reservation.setBook(book);
        }
        if (dto.getReservationStatus() != null) {
            reservation.setReservationStatus(dto.getReservationStatus());
        }
        if (dto.getReservedAt() != null) {
            reservation.setReservedAt(dto.getReservedAt());
        }
        if (dto.getAvailableAt() != null) {
            reservation.setAvailableAt(dto.getAvailableAt());
        }
        if (dto.getAvailableUntil() != null) {
            reservation.setAvailableUntil(dto.getAvailableUntil());
        }
        if (dto.getFulfilledAt() != null) {
            reservation.setFulfilledAt(dto.getFulfilledAt());
        }
        if (dto.getQueuePosition() != null) {
            reservation.setQueuePosition(dto.getQueuePosition());
        }
        if (dto.getNotificationSent() != null) {
            reservation.setNotificationSent(dto.getNotificationSent());
        }
        if (dto.getNotes() != null) {
            reservation.setNotes(dto.getNotes());
        }
        if (dto.getCancelledAt() != null) {
            reservation.setCancelledAt(dto.getCancelledAt());
        }
    }
}

