package com.app.Library_Management.mapper;

import com.app.Library_Management.model.SubscriptionPlan;
import com.app.Library_Management.payload.dto.SubscriptionPlanDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SubscriptionPlanMapper {

    public SubscriptionPlanDTO toDTO(SubscriptionPlan subscriptionPlan) {
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

    public SubscriptionPlan toEntity(SubscriptionPlanDTO subscriptionPlanDTO) {
        if (subscriptionPlanDTO == null) {
            return null;
        }

        return SubscriptionPlan.builder()
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
                .build();
    }

    public SubscriptionPlan toEntityForUpdate(SubscriptionPlanDTO subscriptionPlanDTO) {
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

    public List<SubscriptionPlanDTO> toDTOList(List<SubscriptionPlan> subscriptionPlans) {
        if (subscriptionPlans == null) {
            return null;
        }
        return subscriptionPlans.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<SubscriptionPlan> toEntityList(List<SubscriptionPlanDTO> subscriptionPlanDTOs) {
        if (subscriptionPlanDTOs == null) {
            return null;
        }
        return subscriptionPlanDTOs.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    public SubscriptionPlan updateEntityFromDTO(SubscriptionPlanDTO subscriptionPlanDTO, SubscriptionPlan subscriptionPlan) {
        if (subscriptionPlanDTO == null || subscriptionPlan == null) {
            return subscriptionPlan;
        }

        if (subscriptionPlanDTO.getPlanCode() != null) {
            subscriptionPlan.setPlanCode(subscriptionPlanDTO.getPlanCode());
        }
        if (subscriptionPlanDTO.getPlanName() != null) {
            subscriptionPlan.setPlanName(subscriptionPlanDTO.getPlanName());
        }
        if (subscriptionPlanDTO.getPlanDescription() != null) {
            subscriptionPlan.setPlanDescription(subscriptionPlanDTO.getPlanDescription());
        }
        if (subscriptionPlanDTO.getDurationInDays() != null) {
            subscriptionPlan.setDurationInDays(subscriptionPlanDTO.getDurationInDays());
        }
        if (subscriptionPlanDTO.getPrice() != null) {
            subscriptionPlan.setPrice(subscriptionPlanDTO.getPrice());
        }
        if (subscriptionPlanDTO.getCurrency() != null) {
            subscriptionPlan.setCurrency(subscriptionPlanDTO.getCurrency());
        }
        if (subscriptionPlanDTO.getMaxBooksAllowed() != null) {
            subscriptionPlan.setMaxBooksAllowed(subscriptionPlanDTO.getMaxBooksAllowed());
        }
        if (subscriptionPlanDTO.getMaxDaysPerBook() != null) {
            subscriptionPlan.setMaxDaysPerBook(subscriptionPlanDTO.getMaxDaysPerBook());
        }
        if (subscriptionPlanDTO.getDisplayOrder() != null) {
            subscriptionPlan.setDisplayOrder(subscriptionPlanDTO.getDisplayOrder());
        }
        if (subscriptionPlanDTO.getActive() != null) {
            subscriptionPlan.setActive(subscriptionPlanDTO.getActive());
        }
        if (subscriptionPlanDTO.getIsFeatured() != null) {
            subscriptionPlan.setIsFeatured(subscriptionPlanDTO.getIsFeatured());
        }
        if (subscriptionPlanDTO.getBadgeText() != null) {
            subscriptionPlan.setBadgeText(subscriptionPlanDTO.getBadgeText());
        }
        if (subscriptionPlanDTO.getAdminNotes() != null) {
            subscriptionPlan.setAdminNotes(subscriptionPlanDTO.getAdminNotes());
        }


        return subscriptionPlan;
    }
}

