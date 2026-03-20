package com.app.Library_Management.payload.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionDTO {
    private Long id;

    private Long subscriberId;

    @NotNull(message = "Subscription plan ID is required")
    private Long subscriptionPlanId;

    private String planName;

    private String planCode;

    private String planDescription;

    private Long price;

    private Integer maxBooksAllowed;


    private Integer maxDaysPerBook;

    private Integer maxBooksPerDay;


    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Boolean isActive;

    private Boolean autoRenew;

    private LocalDateTime cancelTime;

    private String cancelReason;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    // Calculated fields
    private Long daysRemaining;

    private Boolean isValid;

    private Boolean isExpired;
}

