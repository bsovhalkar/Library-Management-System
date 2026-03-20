package com.app.Library_Management.mapper;

import com.app.Library_Management.model.Subscription;
import com.app.Library_Management.payload.dto.SubscriptionDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SubscriptionMapper {

    public SubscriptionDTO toDTO(Subscription subscription) {
        if (subscription == null) {
            return null;
        }

        return new SubscriptionDTO(
                subscription.getId(),
                subscription.getSubscriber() != null ? subscription.getSubscriber().getId() : null,
                subscription.getSubscriptionPlan() != null ? subscription.getSubscriptionPlan().getId() : null,
                subscription.getPlanName(),
                subscription.getPlanCode(),
                subscription.getPlanDescription(),
                subscription.getPrice(),
                subscription.getMaxBooksAllowed(),
                subscription.getMaxDaysPerBook(),
                subscription.getMaxBooksPerDay(),
                subscription.getStartTime(),
                subscription.getEndTime(),
                subscription.getIsActive(),
                subscription.getAutoReview(),
                subscription.getCancelTime(),
                subscription.getCancelReason(),
                subscription.getCreateTime(),
                subscription.getUpdateTime()
        );
    }

    public Subscription toEntity(SubscriptionDTO subscriptionDTO) {
        if (subscriptionDTO == null) {
            return null;
        }

        return Subscription.builder()
                .planName(subscriptionDTO.getPlanName())
                .planCode(subscriptionDTO.getPlanCode())
                .planDescription(subscriptionDTO.getPlanDescription())
                .price(subscriptionDTO.getPrice())
                .maxBooksAllowed(subscriptionDTO.getMaxBooksAllowed())
                .maxDaysPerBook(subscriptionDTO.getMaxDaysPerBook())
                .maxBooksPerDay(subscriptionDTO.getMaxBooksPerDay())
                .startTime(subscriptionDTO.getStartTime())
                .endTime(subscriptionDTO.getEndTime())
                .isActive(subscriptionDTO.getIsActive())
                .autoReview(subscriptionDTO.getAutoReview())
                .cancelTime(subscriptionDTO.getCancelTime())
                .cancelReason(subscriptionDTO.getCancelReason())
                .build();
    }

    public Subscription toEntityForUpdate(SubscriptionDTO subscriptionDTO) {
        if (subscriptionDTO == null) {
            return null;
        }

        return Subscription.builder()
                .id(subscriptionDTO.getId())
                .planName(subscriptionDTO.getPlanName())
                .planCode(subscriptionDTO.getPlanCode())
                .planDescription(subscriptionDTO.getPlanDescription())
                .price(subscriptionDTO.getPrice())
                .maxBooksAllowed(subscriptionDTO.getMaxBooksAllowed())
                .maxDaysPerBook(subscriptionDTO.getMaxDaysPerBook())
                .maxBooksPerDay(subscriptionDTO.getMaxBooksPerDay())
                .startTime(subscriptionDTO.getStartTime())
                .endTime(subscriptionDTO.getEndTime())
                .isActive(subscriptionDTO.getIsActive())
                .autoReview(subscriptionDTO.getAutoReview())
                .cancelTime(subscriptionDTO.getCancelTime())
                .cancelReason(subscriptionDTO.getCancelReason())
                .build();
    }

    public List<SubscriptionDTO> toDTOList(List<Subscription> subscriptions) {
        if (subscriptions == null) {
            return null;
        }
        return subscriptions.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<Subscription> toEntityList(List<SubscriptionDTO> subscriptionDTOs) {
        if (subscriptionDTOs == null) {
            return null;
        }
        return subscriptionDTOs.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}

