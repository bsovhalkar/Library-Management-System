package com.app.Library_Management.repository;

import com.app.Library_Management.domain.FineStatus;
import com.app.Library_Management.domain.FineType;
import com.app.Library_Management.model.Fine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FineRepository extends JpaRepository<Fine, Long> {


    @Query("""
    SELECT f FROM Fine f
            WHERE (:userId IS NULL OR f.user.id = :userId)
            AND (:status IS NULL OR f.fineStatus = :status)
            AND (:type IS NULL OR f.fineType = :type)
            ORDER BY f.createdAt DESC
    """)
    Page<Fine> findAllWithFilters(
            @Param("userId") Long userId,
            @Param("status") FineStatus status,
            @Param("type") FineType type,
            Pageable pageable);

}
