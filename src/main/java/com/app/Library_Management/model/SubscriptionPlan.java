package com.app.Library_Management.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String planCode;
    @Column(nullable = false, length = 100)
    private String planName;
    @Column(nullable = false, length = 100)
    private String planDescription;

    @Column(nullable = false)
    private Integer durationInDays;
    @Column(nullable = false)
    private Long price;


    private String currency = "INR";
    @Column(nullable = false)
    @Positive(message = "Max books Should be Positive")
    private Integer maxBooksAllowed;

    @Column(nullable = false)
    @Positive(message = "Max days Should be Positive")
    private Integer maxDaysPerBook;

//    @Column(nullable = false)
    private Integer displayOrder;
    private Boolean active = true;
    private Boolean isFeatured = false;


    private String badgeText;
    private String adminNotes;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private String createdBy;
    private String updatedBy;
}
