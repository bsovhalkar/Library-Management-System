package com.app.Library_Management.model;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User subscriber;

    @ManyToOne
    @JoinColumn(nullable = false)
    private SubscriptionPlan subscriptionPlan;

    private String planName;
    private String planCode;
    private String planDescription;
    private Long price;
    @Column(nullable = false)
    private Integer maxBooksAllowed;
    @Column(nullable = false)
    private Integer maxDaysPerBook;
    private Integer maxBooksPerDay;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    @Column(nullable = false)
    private Boolean isActive = true;

    private Boolean autoRenew;
    private LocalDateTime cancelTime;

    private String cancelReason;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createTime;
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updateTime;


    public Boolean isActive() {
        if (!isActive || startTime == null || endTime == null) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(startTime) && now.isBefore(endTime);
    }

    public Boolean isExpired() {
        if (endTime == null) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(endTime);
    }

    public Long getRemainingDays() {
        if (isExpired() || startTime == null || endTime == null) {
            return 0L;
        }
        LocalDateTime now = LocalDateTime.now();
        return Duration.between(now, endTime).toDays();
    }

    public void calculatePlanEndTime() {
        if (subscriptionPlan != null && startTime != null) {
            this.endTime = startTime.plusDays(subscriptionPlan.getDurationInDays());
        }
    }

    public void initializeFromPlan(SubscriptionPlan  subscriptionPlan) {
        if (subscriptionPlan != null) {
            this.planName = subscriptionPlan.getPlanName();
            this.planCode = subscriptionPlan.getPlanCode();
            this.price = subscriptionPlan.getPrice();
            this.maxBooksAllowed = subscriptionPlan.getMaxBooksAllowed();
            this.maxDaysPerBook = subscriptionPlan.getMaxDaysPerBook();
            this.planDescription = subscriptionPlan.getPlanDescription();

        }
    }
}
