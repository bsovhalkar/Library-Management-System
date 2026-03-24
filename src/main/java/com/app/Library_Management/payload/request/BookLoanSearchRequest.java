package com.app.Library_Management.payload.request;

import com.app.Library_Management.domain.BookLoanStatus;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookLoanSearchRequest {
    private Long userId;
    private Long bookId;
    private BookLoanStatus bookLoanStatus;
    private Boolean overdueOnly;
    private Boolean unpaidOnly;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer page;
    private Integer pageSize;
    private String sortBy = "createdAt";
    private String sortDirection = "DESC";
}
