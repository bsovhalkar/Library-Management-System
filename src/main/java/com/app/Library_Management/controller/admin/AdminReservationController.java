package com.app.Library_Management.controller.admin;

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
@RequestMapping("/api/admin/reservations")
@RequiredArgsConstructor
public class AdminReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationDTO> createReservationForUser(@RequestParam Long userId,
                                                                   @RequestBody ReservationRequest request) throws ReservationException, UserNotFoundException {
        ReservationDTO reservation = reservationService.createReservationForUser(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
    }

    @GetMapping
    public ResponseEntity<PageResponse<ReservationDTO>> searchReservations(@RequestParam(required = false) Long userId,
                                                                           @RequestParam(required = false) Long bookId,
                                                                           @RequestParam(required = false) Boolean activeOnly,
                                                                           @RequestParam(defaultValue = "0") int page,
                                                                           @RequestParam(defaultValue = "20") int pageSize,
                                                                           @RequestParam(defaultValue = "reservedAt") String sortBy,
                                                                           @RequestParam(defaultValue = "DESC") String sortDirection) {
        ReservationSearchRequest request = ReservationSearchRequest.builder()
                .userId(userId)
                .bookId(bookId)
                .activeOnly(activeOnly)
                .page(page)
                .pageSize(pageSize)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .build();
        PageResponse<ReservationDTO> response = reservationService.searchReservations(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{reservationId}/fulfill")
    public ResponseEntity<ReservationDTO> fulfillReservation(@PathVariable Long reservationId) throws Exception {
        ReservationDTO reservation = reservationService.fulfillReservation(reservationId);
        return ResponseEntity.ok(reservation);
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<ApiResponse> cancelReservation(@PathVariable Long reservationId) throws ReservationException, UserNotFoundException {
        reservationService.cancelReservation(reservationId);
        return ResponseEntity.ok(new ApiResponse("Reservation cancelled successfully", true));
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationDTO> getReservationById(@PathVariable Long reservationId) throws ReservationException {
        ReservationDTO reservation = reservationService.getReservationById(reservationId);
        return ResponseEntity.ok(reservation);
    }
}

