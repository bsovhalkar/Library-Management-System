package com.app.Library_Management.payload.request;

import com.app.Library_Management.domain.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationSearchRequest {
    private ReservationStatus reservationStatus;
    private Long userId;
    private Long bookId;
    private Boolean activeOnly;
    private int page = 0;
    private int pageSize = 20;

    private String sortBy = "reservedAt";
    private String sortDirection = "DESC";
}
