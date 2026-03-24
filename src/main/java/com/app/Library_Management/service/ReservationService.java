package com.app.Library_Management.service;

import com.app.Library_Management.exception.ReservationException;
import com.app.Library_Management.exception.UserNotFoundException;
import com.app.Library_Management.model.Reservation;
import com.app.Library_Management.payload.dto.ReservationDTO;
import com.app.Library_Management.payload.request.ReservationRequest;
import com.app.Library_Management.payload.request.ReservationSearchRequest;
import com.app.Library_Management.payload.response.PageResponse;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ReservationService {
    ReservationDTO createReservation(ReservationRequest request) throws UserNotFoundException, ReservationException;
    ReservationDTO createReservationForUser(Long userId, ReservationRequest request) throws ReservationException, UserNotFoundException;
    ReservationDTO cancelReservation (Long reservationId) throws ReservationException, UserNotFoundException;
    ReservationDTO getReservationById(Long reservationId) throws ReservationException;
    ReservationDTO fulfillReservation(Long reservationId) throws Exception;
    PageResponse<ReservationDTO> getMyReservations(ReservationSearchRequest request) throws UserNotFoundException;

    PageResponse<ReservationDTO> searchReservations(ReservationSearchRequest request);
}
