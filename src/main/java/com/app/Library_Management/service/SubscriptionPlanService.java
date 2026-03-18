package com.app.Library_Management.service;

import com.app.Library_Management.exception.PlanCodeAlreadyExist;
import com.app.Library_Management.exception.PlanNotFound;
import com.app.Library_Management.exception.UserNotFoundException;
import com.app.Library_Management.model.SubscriptionPlan;
import com.app.Library_Management.payload.dto.SubscriptionPlanDTO;

import java.util.List;

public interface SubscriptionPlanService {
    SubscriptionPlanDTO createSubscriptionPlan(SubscriptionPlanDTO subscriptionPlanDTO) throws PlanCodeAlreadyExist, UserNotFoundException;
    SubscriptionPlanDTO updateSubscriptionPlan(Long planId,SubscriptionPlanDTO subscriptionPlanDTO) throws PlanNotFound, UserNotFoundException;
    void deleteSubscriptionPlan(Long planId) throws PlanNotFound;
    List<SubscriptionPlanDTO> getAllSubscriptionPlan();
    SubscriptionPlanDTO getSubscriptionPlanById(Long planId) throws PlanNotFound;
}
