package com.app.Library_Management.repository;

import com.app.Library_Management.model.SubscriptionPlan;
//import com.app.Library_Management.payload.dto.SubscriptionPlanDTO;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Long> {
    Boolean existsByPlanCode(@NotBlank String code);
}
