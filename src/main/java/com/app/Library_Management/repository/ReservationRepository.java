package com.app.Library_Management.repository;

import com.app.Library_Management.model.Reservation;
import com.app.Library_Management.domain.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsByUserIdAndBookIdAndReservationStatusIn(
            Long userId,
            Long bookId,
            List<ReservationStatus> statuses
    );

    long countByUserIdAndReservationStatusIn(
            Long userId,
            List<ReservationStatus> statuses
    );


    long countByBookIdAndReservationStatus(
            Long bookId,
            ReservationStatus status
    );

    Page<Reservation> findByUserId(Long userId, Pageable pageable);
}