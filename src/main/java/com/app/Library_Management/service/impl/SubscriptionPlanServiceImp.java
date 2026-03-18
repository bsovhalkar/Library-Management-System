package com.app.Library_Management.service.impl;

import com.app.Library_Management.exception.PlanCodeAlreadyExist;
import com.app.Library_Management.exception.PlanNotFound;
import com.app.Library_Management.exception.UserNotFoundException;
import com.app.Library_Management.mapper.SubscriptionPlanMapper;
import com.app.Library_Management.model.SubscriptionPlan;
import com.app.Library_Management.model.User;
import com.app.Library_Management.payload.dto.SubscriptionPlanDTO;
import com.app.Library_Management.repository.SubscriptionPlanRepository;
import com.app.Library_Management.service.SubscriptionPlanService;
import com.app.Library_Management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionPlanServiceImp implements SubscriptionPlanService {
    private  final SubscriptionPlanRepository subscriptionPlanRepository;
    public final UserService userService;
    @Override
    public SubscriptionPlanDTO createSubscriptionPlan(SubscriptionPlanDTO subscriptionPlanDTO) throws PlanCodeAlreadyExist, UserNotFoundException {
        if(subscriptionPlanRepository.existsByPlanCode(subscriptionPlanDTO.getPlanCode())) {
            throw new PlanCodeAlreadyExist(subscriptionPlanDTO.getPlanCode());
        }
        SubscriptionPlan subscriptionPlan = SubscriptionPlanMapper.toEntity(subscriptionPlanDTO);
        User user = userService.getCurrentUser();
        subscriptionPlan.setCreatedBy(user.getFullName());
        subscriptionPlan.setUpdatedBy(user.getFullName());

        SubscriptionPlan savedPlan = subscriptionPlanRepository.save(subscriptionPlan);
        return SubscriptionPlanMapper.toDTO(savedPlan);
    }

    @Override
    public SubscriptionPlanDTO updateSubscriptionPlan(Long planId, SubscriptionPlanDTO subscriptionPlanDTO) throws PlanNotFound, UserNotFoundException {
        SubscriptionPlan existingPlan = subscriptionPlanRepository.findById(planId)
                .orElseThrow(() -> new PlanNotFound(planId));
        SubscriptionPlan updatedPlan = SubscriptionPlanMapper.updateEntityFromDTO(subscriptionPlanDTO, existingPlan);
        User user = userService.getCurrentUser();
        updatedPlan.setUpdatedBy(user.getFullName());
        SubscriptionPlan savedPlan = subscriptionPlanRepository.save(updatedPlan);
        return SubscriptionPlanMapper.toDTO(savedPlan);
    }

    @Override
    public void deleteSubscriptionPlan(Long planId) throws PlanNotFound {
        SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.findById(planId)
                .orElseThrow(() -> new PlanNotFound(planId));
        subscriptionPlanRepository.delete(subscriptionPlan);
    }

    @Override
    public List<SubscriptionPlanDTO> getAllSubscriptionPlan() {
        List<SubscriptionPlan> subscriptionPlans = subscriptionPlanRepository.findAll();
        return SubscriptionPlanMapper.toDTOList(subscriptionPlans);
    }

    @Override
    public SubscriptionPlanDTO getSubscriptionPlanById(Long planId) throws PlanNotFound {
        SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.findById(planId)
                .orElseThrow(() -> new PlanNotFound(planId));
        return SubscriptionPlanMapper.toDTO(subscriptionPlan);
    }
}
