package com.app.Library_Management.service;

import com.app.Library_Management.exception.SubscriptionException;
import com.app.Library_Management.exception.UserNotFoundException;
import com.app.Library_Management.exception.PaymentIdInvalid;
import com.app.Library_Management.payload.dto.SubscriptionDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SubscriptionService{
    SubscriptionDTO subscribe(SubscriptionDTO dto) throws UserNotFoundException, SubscriptionException;
    List<SubscriptionDTO> getUsersAllActiveSubscriptions() throws UserNotFoundException, SubscriptionException;
    SubscriptionDTO getSubscriptionById(Long subscriptionId) throws SubscriptionException;
    SubscriptionDTO cancelSubscription(Long subscriptionId, String cancelReason) throws SubscriptionException;
    SubscriptionDTO activateSubscription(Long subscriptionId,Long paymentId) throws SubscriptionException, UserNotFoundException, PaymentIdInvalid;
    List<SubscriptionDTO> getAllSubscriptions(Pageable pageable);
    void deactivateSubscription() throws SubscriptionException;

}
