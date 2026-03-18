package com.app.Library_Management.controller;

import com.app.Library_Management.payload.dto.SubscriptionPlanDTO;
import com.app.Library_Management.service.SubscriptionPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscription-plans")
public class UserSubscriptionPlanController {
    private final SubscriptionPlanService subscriptionPlanService;

    @GetMapping
    public ResponseEntity<List<SubscriptionPlanDTO>> getAllSubscriptionPlan() {
        List<SubscriptionPlanDTO> subscriptionPlans = subscriptionPlanService.getAllSubscriptionPlan();
        return ResponseEntity.ok(subscriptionPlans);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionPlanDTO> getSubscriptionPlanById(@PathVariable Long id) {
        SubscriptionPlanDTO plan = subscriptionPlanService.getSubscriptionPlanById(id);
        return ResponseEntity.ok(plan);
    }
}

