package com.app.Library_Management.controller.user;

import com.app.Library_Management.exception.SubscriptionException;
import com.app.Library_Management.exception.UserNotFoundException;
import com.app.Library_Management.payload.dto.SubscriptionDTO;
import com.app.Library_Management.payload.response.ApiResponse;
import com.app.Library_Management.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscriptions")
public class UserSubscriptionController {
    private final SubscriptionService subscriptionService;

    @PostMapping("/subscribe")
    public ResponseEntity<SubscriptionDTO> subscribe(@RequestBody SubscriptionDTO subscriptionDTO)
            throws UserNotFoundException, SubscriptionException {
        SubscriptionDTO subscription = subscriptionService.subscribe(subscriptionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(subscription);
    }

    @GetMapping("/active")
    public ResponseEntity<List<SubscriptionDTO>> getActiveSubscriptions()
            throws UserNotFoundException, SubscriptionException {
        List<SubscriptionDTO> subscriptions = subscriptionService.getUsersAllActiveSubscriptions();
        return ResponseEntity.ok(subscriptions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionDTO> getSubscriptionById(@PathVariable Long id) throws SubscriptionException {
        SubscriptionDTO subscription = subscriptionService.getSubscriptionById(id);
        return ResponseEntity.ok(subscription);
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<SubscriptionDTO> activateSubscription(
            @PathVariable Long id) throws SubscriptionException, UserNotFoundException {
        SubscriptionDTO subscription = subscriptionService.activateSubscription(id);
        return ResponseEntity.ok(subscription);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<SubscriptionDTO> cancelSubscription(
            @PathVariable Long id,
            @RequestParam String cancelReason) throws SubscriptionException {
        SubscriptionDTO subscription = subscriptionService.cancelSubscription(id, cancelReason);
        return ResponseEntity.ok(subscription);
    }



}

