package com.app.Library_Management.mapper;

import com.app.Library_Management.model.SubscriptionPlan;
import com.app.Library_Management.payload.dto.SubscriptionPlanDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriptionPlanMapper {

    public static SubscriptionPlanDTO toDTO(SubscriptionPlan subscriptionPlan) {
        if (subscriptionPlan == null) {
            return null;
        }

        return new SubscriptionPlanDTO(
                subscriptionPlan.getId(),
                subscriptionPlan.getPlanCode(),
                subscriptionPlan.getPlanName(),
                subscriptionPlan.getPlanDescription(),
                subscriptionPlan.getDurationInDays(),
                subscriptionPlan.getPrice(),
                subscriptionPlan.getCurrency(),
                subscriptionPlan.getMaxBooksAllowed(),
                subscriptionPlan.getMaxDaysPerBook(),
                subscriptionPlan.getDisplayOrder(),
                subscriptionPlan.getActive(),
                subscriptionPlan.getIsFeatured(),
                subscriptionPlan.getBadgeText(),
                subscriptionPlan.getAdminNotes(),
                subscriptionPlan.getCreatedAt(),
                subscriptionPlan.getUpdatedAt(),
                subscriptionPlan.getCreatedBy(),
                subscriptionPlan.getUpdatedBy()
        );
    }

    public static SubscriptionPlan toEntity(SubscriptionPlanDTO subscriptionPlanDTO) {
        if (subscriptionPlanDTO == null) {
            return null;
        }

        return SubscriptionPlan.builder()
                .id(subscriptionPlanDTO.getId())
                .planCode(subscriptionPlanDTO.getPlanCode())
                .planName(subscriptionPlanDTO.getPlanName())
                .planDescription(subscriptionPlanDTO.getPlanDescription())
                .durationInDays(subscriptionPlanDTO.getDurationInDays())
                .price(subscriptionPlanDTO.getPrice())
                .currency(subscriptionPlanDTO.getCurrency())
                .maxBooksAllowed(subscriptionPlanDTO.getMaxBooksAllowed())
                .maxDaysPerBook(subscriptionPlanDTO.getMaxDaysPerBook())
                .displayOrder(subscriptionPlanDTO.getDisplayOrder())
                .active(subscriptionPlanDTO.getActive())
                .isFeatured(subscriptionPlanDTO.getIsFeatured())
                .badgeText(subscriptionPlanDTO.getBadgeText())
                .adminNotes(subscriptionPlanDTO.getAdminNotes())
                .createdAt(subscriptionPlanDTO.getCreatedAt())
                .updatedAt(subscriptionPlanDTO.getUpdatedAt())
                .createdBy(subscriptionPlanDTO.getCreatedBy())
                .updatedBy(subscriptionPlanDTO.getUpdatedBy())
                .build();
    }

    public static List<SubscriptionPlanDTO> toDTOList(List<SubscriptionPlan> subscriptionPlans) {
        if (subscriptionPlans == null) {
            return null;
        }
        return subscriptionPlans.stream()
                .map(SubscriptionPlanMapper::toDTO)
                .collect(Collectors.toList());
    }

    public static List<SubscriptionPlan> toEntityList(List<SubscriptionPlanDTO> subscriptionPlanDTOs) {
        if (subscriptionPlanDTOs == null) {
            return null;
        }
        return subscriptionPlanDTOs.stream()
                .map(SubscriptionPlanMapper::toEntity)
                .collect(Collectors.toList());
    }
}

