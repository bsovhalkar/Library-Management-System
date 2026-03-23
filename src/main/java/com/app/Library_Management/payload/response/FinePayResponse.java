package com.app.Library_Management.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinePayResponse {
    private Long fineId;
    private Long userId;
    private Long bookLoanId;
    private Long amount;
    private String reason;
    private String transactionId;
    private LocalDate paidAt;
    private String message;
    private Boolean status;
}

