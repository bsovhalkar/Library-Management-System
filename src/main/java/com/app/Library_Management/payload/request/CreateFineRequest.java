package com.app.Library_Management.payload.request;


import com.app.Library_Management.domain.FineType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateFineRequest {

    @NotNull(message = "Book loan id is mandatory")
    private Long bookLoanId;

    @NotNull(message = "Fine Type is mandatory")
    private FineType fineType;

    @NotNull(message = "Amount is mandatory")
    @Positive(message = "Amount must be greater than zero")
    private Long amount;

    private String reason;

    private String notes;

}
