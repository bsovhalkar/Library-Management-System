package com.app.Library_Management.repository;

import com.app.Library_Management.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    @Query("""
        SELECT s FROM Subscription s 
        WHERE s.subscriber.id = :userId 
        AND s.isActive = true
    """)
    List<Subscription> findActiveSubscriptionsByUserId(@Param("userId") Long userId);

    @Query("""
        SELECT s FROM Subscription s 
        WHERE s.subscriber.id = :userId 
        AND s.startTime <= :currentDateTime 
        AND s.endTime >= :currentDateTime 
        AND s.isActive = true
    """)
    List<Subscription> findActiveSubscriptionsByUserIdAndCurrentDate(
            @Param("userId") Long userId,
            @Param("currentDateTime") LocalDateTime currentDateTime
    );

    @Query("""
        SELECT s FROM Subscription s 
        WHERE s.subscriber.id = :userId
    """)
    List<Subscription> findAllSubscriptionsByUserId(@Param("userId") Long userId);

    @Query("""
        SELECT s FROM Subscription s 
        WHERE s.subscriber.id = :userId 
        AND s.startTime <= :currentDateTime 
        AND s.endTime >= :currentDateTime
    """)
    Optional<Subscription> findCurrentActiveSubscription(
            @Param("userId") Long userId,
            @Param("currentDateTime") LocalDateTime currentDateTime
    );

    @Query("""
        SELECT s FROM Subscription s 
            WHERE s.endTime < :currentDateTime 
            AND s.isActive = true
    """)
    List<Subscription> findExpiredActiveSubscriptions(
            @Param("currentDateTime") LocalDateTime currentDateTime

    );
}



