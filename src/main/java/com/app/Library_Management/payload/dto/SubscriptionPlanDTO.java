package com.app.Library_Management.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionPlanDTO {
    private Long id;
    private String planCode;
    private String planName;
    private String planDescription;
    private Integer durationInDays;
    private Long price;
    private String currency;
    private Integer maxBooksAllowed;
    private Integer maxDaysPerBook;
    private Integer displayOrder;
    private Boolean active;
    private Boolean isFeatured;
    private String badgeText;
    private String adminNotes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}

