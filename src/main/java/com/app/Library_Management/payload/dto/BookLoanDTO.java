package com.app.Library_Management.payload.dto;
import com.app.Library_Management.domain.BookLoanStatus;
import com.app.Library_Management.domain.BookLoanType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookLoanDTO {

    private Long id;

    @NotNull(message = "User id is required")
    private Long userId;

    private String userName;

    private  String userEmail;

    @NotNull(message = "Book id is required")
    private Long bookId;

    private String bookTitle;

    private String bookAuthor;

    private String bookISBN;
    private String bookCoverPageURL;

    private BookLoanType bookLoanType;

    private BookLoanStatus bookLoanStatus;

    @NotNull(message = "Checkout date is required")
    private LocalDate checkoutDate;

    @NotNull(message = "Due date is required")
    private LocalDate dueDate;

    private Long remainingDays;
    private Long overdueDays;

    @NotNull(message = "Return date is required")
    private LocalDate returnDate;

    private Long renewalCount;

    private Long fineAmount;
    private Boolean finePaid;

    private Long maxRenewalAllowed;

    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    private String notes;

    private Boolean isOverdue;


    private LocalDate createdAt;

    private LocalDate updatedAt;
}
