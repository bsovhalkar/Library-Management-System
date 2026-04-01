package com.app.Library_Management.repository;

import com.app.Library_Management.model.Wishlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface WishlistRepository extends JpaRepository<Wishlist,Long> {
    Page<Wishlist> findByUserId(Long userId, Pageable pageable);
    boolean existsByBookIdAndUserId(Long bookId, Long userId);
    void deleteByUserIdAndBookId(Long userId, Long bookId);
}
