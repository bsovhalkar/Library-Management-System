package com.app.Library_Management.payload.dto;

import com.app.Library_Management.domain.FineStatus;
import com.app.Library_Management.domain.FineType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FineDTO {

    private Long id;
    @NotNull(message = "Book loan id is mandatory")
    private Long bookLoanId;
    private String bookTitle;
    private String bookIsbn;

    @NotNull(message = "User id is mandatory")
    private Long userId;
    private String userName;
    private String userEmail;

    @NotNull(message = "Fine type is mandatory")
    private FineType fineType;

    @NotNull(message = "Amount is mandatory")
    @PositiveOrZero(message = "Amount cannot be negative")
    private Long amount;

    @PositiveOrZero(message = "Amount paid cannot be negative")
    private Long amountPaid;

    private Long amountOutstanding;

    @NotNull(message = "Fine status is mandatory")
    private FineStatus fineStatus;

    private String reason;
    private String notes;
    private Long waivedByUserId;
    private String waivedByUserName;
    private LocalDate waivedAt;
    private LocalDate paidAt;
    private Long processedByUserId;
    private String transactionId;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
