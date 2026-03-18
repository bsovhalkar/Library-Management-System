package com.app.Library_Management.controller.admin;

import com.app.Library_Management.exception.PlanCodeAlreadyExist;
import com.app.Library_Management.exception.PlanNotFound;
import com.app.Library_Management.exception.UserNotFoundException;
import com.app.Library_Management.payload.dto.SubscriptionPlanDTO;
import com.app.Library_Management.payload.response.ApiResponse;
import com.app.Library_Management.service.SubscriptionPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/subscription-plans")
public class AdminSubscriptionPlanController {
    private final SubscriptionPlanService subscriptionPlanService;

    @GetMapping
    public ResponseEntity<List<SubscriptionPlanDTO>> getAllSubscriptionPlan() {
        List<SubscriptionPlanDTO> subscriptionPlans = subscriptionPlanService.getAllSubscriptionPlan();
        return ResponseEntity.ok(subscriptionPlans);
    }

    @PostMapping("/create")
    public ResponseEntity<SubscriptionPlanDTO> createSubscriptionPlan(@Valid @RequestBody SubscriptionPlanDTO subscriptionPlanDTO) throws UserNotFoundException, PlanCodeAlreadyExist {
        SubscriptionPlanDTO createdPlan = subscriptionPlanService.createSubscriptionPlan(subscriptionPlanDTO);
        return ResponseEntity.ok(createdPlan);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<SubscriptionPlanDTO> updateSubscriptionPlan(@PathVariable Long id, @RequestBody SubscriptionPlanDTO subscriptionPlanDTO) throws UserNotFoundException, PlanNotFound {
        SubscriptionPlanDTO updatedPlan = subscriptionPlanService.updateSubscriptionPlan(id, subscriptionPlanDTO);
        return ResponseEntity.ok(updatedPlan);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteSubscriptionPlan(@PathVariable Long id) throws PlanNotFound {
        subscriptionPlanService.deleteSubscriptionPlan(id);
        return ResponseEntity.ok(new ApiResponse ("Delete subscription plan successfully!",true));
    }
}

