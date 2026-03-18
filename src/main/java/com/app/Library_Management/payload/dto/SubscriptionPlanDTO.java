package com.app.Library_Management.payload.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionPlanDTO {
    private Long id;
    
    @NotBlank(message = "Plan code is required")
    private String planCode;
    
    @NotBlank(message = "Plan name is required")
    @Size(max = 100, message = "Plan name must not exceed 100 characters")
    private String planName;
    
    @NotBlank(message = "Plan description is required")
    @Size(max = 100, message = "Plan description must not exceed 100 characters")
    private String planDescription;
    
    @NotNull(message = "Duration in days is required")
    @Positive(message = "Duration in days must be positive")
    private Integer durationInDays;
    
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Long price;
    
    private String currency;
    
    @NotNull(message = "Max books allowed is required")
    @Positive(message = "Max books should be positive")
    private Integer maxBooksAllowed;
    
    @NotNull(message = "Max days per book is required")
    @Positive(message = "Max days should be positive")
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

