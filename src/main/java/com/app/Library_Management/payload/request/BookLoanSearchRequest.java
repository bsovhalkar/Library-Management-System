package com.app.Library_Management.payload.request;

import com.app.Library_Management.domain.BookLoanStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
