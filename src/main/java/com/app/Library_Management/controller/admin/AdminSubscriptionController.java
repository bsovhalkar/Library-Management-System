package com.app.Library_Management.controller.admin;

import com.app.Library_Management.exception.SubscriptionException;
import com.app.Library_Management.exception.PaymentIdInvalid;
import com.app.Library_Management.exception.UserNotFoundException;
import com.app.Library_Management.payload.dto.SubscriptionDTO;
import com.app.Library_Management.payload.response.ApiResponse;
import com.app.Library_Management.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/subscriptions")
public class AdminSubscriptionController {
    private final SubscriptionService subscriptionService;

    @GetMapping
    public ResponseEntity<List<SubscriptionDTO>> getAllSubscriptions() {
        Pageable pageable = PageRequest.of(0, 10);
        List<SubscriptionDTO> subscriptions = subscriptionService.getAllSubscriptions(pageable);
        return ResponseEntity.ok(subscriptions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionDTO> getSubscriptionById(@PathVariable Long id) throws SubscriptionException {
        SubscriptionDTO subscription = subscriptionService.getSubscriptionById(id);
        return ResponseEntity.ok(subscription);
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<SubscriptionDTO> activateSubscription(
            @PathVariable Long id,
            @RequestParam Long paymentId) throws SubscriptionException, UserNotFoundException, PaymentIdInvalid {
        SubscriptionDTO subscription = subscriptionService.activateSubscription(id, paymentId);
        return ResponseEntity.ok(subscription);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<SubscriptionDTO> cancelSubscription(
            @PathVariable Long id,
            @RequestParam String cancelReason) throws SubscriptionException {
        SubscriptionDTO subscription = subscriptionService.cancelSubscription(id, cancelReason);
        return ResponseEntity.ok(subscription);
    }

    @PostMapping("/deactivate-expired")
    public ResponseEntity<ApiResponse> deactivateExpiredSubscriptions() throws SubscriptionException {
        subscriptionService.deactivateSubscription();
        return ResponseEntity.ok(new ApiResponse("Expired subscriptions deactivated successfully!", true));
    }
}

