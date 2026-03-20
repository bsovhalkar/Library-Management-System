package com.app.Library_Management.service.impl;

import com.app.Library_Management.exception.SubscriptionException;
import com.app.Library_Management.exception.UserNotFoundException;
import com.app.Library_Management.mapper.SubscriptionMapper;
import com.app.Library_Management.exception.PaymentIdInvalid;
import com.app.Library_Management.model.Subscription;
import com.app.Library_Management.model.SubscriptionPlan;
import com.app.Library_Management.model.User;
import com.app.Library_Management.payload.dto.SubscriptionDTO;
import com.app.Library_Management.repository.SubscriptionPlanRepository;
import com.app.Library_Management.repository.SubscriptionRepository;
import com.app.Library_Management.service.SubscriptionService;
import com.app.Library_Management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImp implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper  subscriptionMapper;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final UserService  userService;
    @Override
    public SubscriptionDTO subscribe(SubscriptionDTO dto) throws UserNotFoundException, SubscriptionException {
        User currentUser = userService.getCurrentUser();
        SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.findById(dto.getSubscriptionPlanId())
                .orElseThrow(() -> new SubscriptionException("Subscription plan with id " + dto.getSubscriptionPlanId() + " not found"));

        Subscription subscription = subscriptionMapper.toEntity(dto);
        subscription.initializeFromPlan(subscriptionPlan);
        subscription.setSubscriber(currentUser);
        subscription.setSubscriptionPlan(subscriptionPlan);
        subscription.setIsActive(false);
        Subscription savedSubscription = subscriptionRepository.save(subscription);
        return subscriptionMapper.toDTO(savedSubscription);
    }

    @Override
    public List<SubscriptionDTO> getUsersAllActiveSubscriptions() throws UserNotFoundException, SubscriptionException {
        User currentUser = userService.getCurrentUser();
        List<Subscription> subscriptions = subscriptionRepository.findActiveSubscriptionsByUserIdAndCurrentDate(currentUser.getId(), LocalDateTime.now());
        if (subscriptions.isEmpty()) {
            throw new SubscriptionException("No active subscriptions found for user id: " + currentUser.getId());
        }
        return subscriptionMapper.toDTOList(subscriptions);
    }

    @Override
    public SubscriptionDTO getSubscriptionById(Long subscriptionId) throws SubscriptionException {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new SubscriptionException("Subscription with id " + subscriptionId + " not found"));
        return subscriptionMapper.toDTO(subscription);
    }

    @Override
    public SubscriptionDTO cancelSubscription(Long subscriptionId, String cancelReason) throws SubscriptionException {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new SubscriptionException("Subscription with id " + subscriptionId + " not found"));
        
        subscription.setIsActive(false);
        subscription.setCancelTime(LocalDateTime.now());
        subscription.setCancelReason(cancelReason);
        
        Subscription updatedSubscription = subscriptionRepository.save(subscription);
        return subscriptionMapper.toDTO(updatedSubscription);
    }

    @Override
    public SubscriptionDTO activateSubscription(Long subscriptionId, Long paymentId) throws SubscriptionException, UserNotFoundException, PaymentIdInvalid {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new SubscriptionException("Subscription with id " + subscriptionId + " not found"));
        
        if(subscription.getIsActive()) {
            throw new SubscriptionException("Subscription with id " + subscriptionId + " is already active.");
        }
        
        subscription.setIsActive(true);
        subscription.setStartTime(LocalDateTime.now());
        
        SubscriptionPlan subscriptionPlan = subscription.getSubscriptionPlan();
        subscription.setEndTime(subscription.getStartTime().plusDays(subscriptionPlan.getDurationInDays()));
        
        Subscription updatedSubscription = subscriptionRepository.save(subscription);
        return subscriptionMapper.toDTO(updatedSubscription);
    }

    @Override
    public List<SubscriptionDTO> getAllSubscriptions(Pageable pageable) {
        return subscriptionRepository.findAll(pageable)
                .map(subscriptionMapper::toDTO)
                .toList();
    }

    @Override
    public void deactivateSubscription() throws SubscriptionException {
        List<Subscription> subscriptionList = subscriptionRepository.findExpiredActiveSubscriptions(LocalDateTime.now());
        if (subscriptionList.isEmpty())
            throw new SubscriptionException("No active subscriptions found to deactivate");
        for(Subscription subscription : subscriptionList) {
            subscription.setIsActive(false);
            subscriptionRepository.save(subscription);
        }
    }

}
