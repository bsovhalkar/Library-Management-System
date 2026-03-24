package com.app.Library_Management.controller.user;

import com.app.Library_Management.exception.ReservationException;
import com.app.Library_Management.exception.UserNotFoundException;
import com.app.Library_Management.payload.dto.ReservationDTO;
import com.app.Library_Management.payload.request.ReservationRequest;
import com.app.Library_Management.payload.request.ReservationSearchRequest;
import com.app.Library_Management.payload.response.ApiResponse;
import com.app.Library_Management.payload.response.PageResponse;
import com.app.Library_Management.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class UserReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationDTO> createReservation(@RequestBody ReservationRequest request) throws UserNotFoundException, ReservationException {
        ReservationDTO reservation = reservationService.createReservation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
    }

    @GetMapping("/my")
    public ResponseEntity<PageResponse<ReservationDTO>> getMyReservations(@RequestParam(defaultValue = "0") int page,
                                                                          @RequestParam(defaultValue = "20") int pageSize,
                                                                          @RequestParam(defaultValue = "reservedAt") String sortBy,
                                                                          @RequestParam(defaultValue = "DESC") String sortDirection) throws UserNotFoundException {
        ReservationSearchRequest request = ReservationSearchRequest.builder()
                .page(page)
                .pageSize(pageSize)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .build();
        PageResponse<ReservationDTO> response = reservationService.getMyReservations(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationDTO> getReservationById(@PathVariable Long reservationId) throws ReservationException {
        ReservationDTO reservation = reservationService.getReservationById(reservationId);
        return ResponseEntity.ok(reservation);
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<ApiResponse> cancelReservation(@PathVariable Long reservationId) throws ReservationException, UserNotFoundException {
        reservationService.cancelReservation(reservationId);
        return ResponseEntity.ok(new ApiResponse("Reservation cancelled successfully", true));
    }
}

