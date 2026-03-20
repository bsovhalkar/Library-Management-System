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

    @NotNull(message = "Subscriber ID is required")
    private Long subscriberId;

    @NotNull(message = "Subscription plan ID is required")
    private Long subscriptionPlanId;

    private String planName;

    @NotBlank(message = "Plan code is required")
    private String planCode;

    private String planDescription;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Long price;

    @NotNull(message = "Max books allowed is required")
    @Positive(message = "Max books allowed must be positive")
    private Integer maxBooksAllowed;

    @NotNull(message = "Max days per book is required")
    @Positive(message = "Max days per book must be positive")
    private Integer maxDaysPerBook;

    private Integer maxBooksPerDay;

    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    private LocalDateTime endTime;

    @NotNull(message = "Active status is required")
    private Boolean isActive;

    private Boolean autoReview;

    private LocalDateTime cancelTime;

    private String cancelReason;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}

