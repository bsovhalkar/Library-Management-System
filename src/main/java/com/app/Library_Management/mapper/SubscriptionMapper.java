package com.app.Library_Management.mapper;

import com.app.Library_Management.exception.SubscriptionException;
import com.app.Library_Management.exception.UserNotFoundException;
import com.app.Library_Management.model.Subscription;
import com.app.Library_Management.model.SubscriptionPlan;
import com.app.Library_Management.model.User;
import com.app.Library_Management.payload.dto.SubscriptionDTO;
import com.app.Library_Management.repository.SubscriptionPlanRepository;
import com.app.Library_Management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SubscriptionMapper {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SubscriptionPlanRepository  subscriptionPlanRepository;

    public SubscriptionDTO toDTO(Subscription subscription) {
        if (subscription == null) {
            return null;
        }

        SubscriptionDTO dto = new SubscriptionDTO();
        dto.setId(subscription.getId());
        dto.setSubscriberId(subscription.getSubscriber() != null ? subscription.getSubscriber().getId() : null);
        dto.setSubscriptionPlanId(subscription.getSubscriptionPlan() != null ? subscription.getSubscriptionPlan().getId() : null);
        dto.setPlanName(subscription.getPlanName());
        dto.setPlanCode(subscription.getPlanCode());
        dto.setPlanDescription(subscription.getPlanDescription());
        dto.setPrice(subscription.getPrice());
        dto.setMaxBooksAllowed(subscription.getMaxBooksAllowed());
        dto.setMaxDaysPerBook(subscription.getMaxDaysPerBook());
        dto.setMaxBooksPerDay(subscription.getMaxBooksPerDay());
        dto.setStartTime(subscription.getStartTime());
        dto.setEndTime(subscription.getEndTime());
        dto.setIsActive(subscription.getIsActive());
        dto.setAutoRenew(subscription.getAutoRenew());
        dto.setCancelTime(subscription.getCancelTime());
        dto.setCancelReason(subscription.getCancelReason());
        dto.setCreateTime(subscription.getCreateTime());
        dto.setUpdateTime(subscription.getUpdateTime());
        dto.setDaysRemaining(subscription.getRemainingDays());
        dto.setIsValid(subscription.isActive());
        dto.setIsExpired(subscription.isExpired());

        return dto;
    }

    public Subscription toEntity(SubscriptionDTO subscriptionDTO) throws UserNotFoundException, SubscriptionException {
        if (subscriptionDTO == null) {
            return null;
        }


        SubscriptionPlan subscriptionPlan = null;
        if (subscriptionDTO.getSubscriptionPlanId() != null) {
            subscriptionPlan = subscriptionPlanRepository.findById(subscriptionDTO.getSubscriptionPlanId())
                    .orElseThrow(() -> new SubscriptionException("SubscriptionPlan not found with id: " + subscriptionDTO.getSubscriptionPlanId()));
        }

        Subscription subscription = Subscription.builder()
                .subscriptionPlan(subscriptionPlan)
                .startTime(subscriptionDTO.getStartTime())
                .endTime(subscriptionDTO.getEndTime())
                .isActive(subscriptionDTO.getIsActive())
                .autoRenew(subscriptionDTO.getAutoRenew())
                .cancelTime(subscriptionDTO.getCancelTime())
                .cancelReason(subscriptionDTO.getCancelReason())
                .build();

        if (subscriptionPlan != null) {
            subscription.setPlanName(subscriptionPlan.getPlanName());
            subscription.setPlanCode(subscriptionPlan.getPlanCode());
            subscription.setPlanDescription(subscriptionPlan.getPlanDescription());
            subscription.setPrice(subscriptionPlan.getPrice());
            subscription.setMaxBooksAllowed(subscriptionPlan.getMaxBooksAllowed());
            subscription.setMaxDaysPerBook(subscriptionPlan.getMaxDaysPerBook());
            subscription.setMaxBooksPerDay(subscriptionPlan.getDisplayOrder());
        } else {
            subscription.setPlanName(subscriptionDTO.getPlanName());
            subscription.setPlanCode(subscriptionDTO.getPlanCode());
            subscription.setPlanDescription(subscriptionDTO.getPlanDescription());
            subscription.setPrice(subscriptionDTO.getPrice());
            subscription.setMaxBooksAllowed(subscriptionDTO.getMaxBooksAllowed());
            subscription.setMaxDaysPerBook(subscriptionDTO.getMaxDaysPerBook());
            subscription.setMaxBooksPerDay(subscriptionDTO.getMaxBooksPerDay());
        }

        return subscription;
    }


    public Subscription updateEntityFromDto(SubscriptionDTO subscriptionDTO, Subscription subscription) throws UserNotFoundException, SubscriptionException {
        if (subscriptionDTO == null || subscription == null) {
            return null;
        }



        if (subscriptionDTO.getSubscriptionPlanId() != null) {
            SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.findById(subscriptionDTO.getSubscriptionPlanId())
                    .orElseThrow(() -> new SubscriptionException("SubscriptionPlan not found with id: " + subscriptionDTO.getSubscriptionPlanId()));
            subscription.setSubscriptionPlan(subscriptionPlan);
            subscription.setPlanName(subscriptionPlan.getPlanName());
            subscription.setPlanCode(subscriptionPlan.getPlanCode());
            subscription.setPlanDescription(subscriptionPlan.getPlanDescription());
            subscription.setPrice(subscriptionPlan.getPrice());
            subscription.setMaxBooksAllowed(subscriptionPlan.getMaxBooksAllowed());
            subscription.setMaxDaysPerBook(subscriptionPlan.getMaxDaysPerBook());
            subscription.setMaxBooksPerDay(subscriptionPlan.getDisplayOrder());
        } else {
            if (subscriptionDTO.getPlanName() != null) {
                subscription.setPlanName(subscriptionDTO.getPlanName());
            }
            if (subscriptionDTO.getPlanCode() != null) {
                subscription.setPlanCode(subscriptionDTO.getPlanCode());
            }
            if (subscriptionDTO.getPlanDescription() != null) {
                subscription.setPlanDescription(subscriptionDTO.getPlanDescription());
            }
            if (subscriptionDTO.getPrice() != null) {
                subscription.setPrice(subscriptionDTO.getPrice());
            }
            if (subscriptionDTO.getMaxBooksAllowed() != null) {
                subscription.setMaxBooksAllowed(subscriptionDTO.getMaxBooksAllowed());
            }
            if (subscriptionDTO.getMaxDaysPerBook() != null) {
                subscription.setMaxDaysPerBook(subscriptionDTO.getMaxDaysPerBook());
            }
            if (subscriptionDTO.getMaxBooksPerDay() != null) {
                subscription.setMaxBooksPerDay(subscriptionDTO.getMaxBooksPerDay());
            }
        }

        if (subscriptionDTO.getIsActive() != null) {
            subscription.setIsActive(subscriptionDTO.getIsActive());
        }
        if (subscriptionDTO.getAutoRenew() != null) {
            subscription.setAutoRenew(subscriptionDTO.getAutoRenew());
        }
        if (subscriptionDTO.getCancelTime() != null) {
            subscription.setCancelTime(subscriptionDTO.getCancelTime());
        }
        if (subscriptionDTO.getCancelReason() != null) {
            subscription.setCancelReason(subscriptionDTO.getCancelReason());
        }

        return subscription;
    }

    public List<SubscriptionDTO> toDTOList(List<Subscription> subscriptions) {
        if (subscriptions == null) {
            return null;
        }
        return subscriptions.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<Subscription> toEntityList(List<SubscriptionDTO> subscriptionDTOs) throws UserNotFoundException {
        if (subscriptionDTOs == null) {
            return null;
        }
        return subscriptionDTOs.stream()
                .map(dto -> {
                    try {
                        return this.toEntity(dto);
                    } catch (UserNotFoundException | SubscriptionException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }
}

