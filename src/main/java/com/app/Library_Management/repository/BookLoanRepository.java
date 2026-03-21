package com.app.Library_Management.repository;

import com.app.Library_Management.domain.BookLoanStatus;
import com.app.Library_Management.model.BookLoan;
import com.app.Library_Management.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BookLoanRepository extends JpaRepository<BookLoan,Long>, JpaSpecificationExecutor<BookLoan> {

    Page<BookLoan> findByUserId(Long userId, Pageable pageable);
    Page<BookLoan> findByBookLoanStatus(BookLoanStatus status, Pageable pageable);
    Page<BookLoan> findByBookId(Long bookId, Pageable pageable);
    Page<BookLoan> findByUserIdAndBookLoanStatus(Long userId, BookLoanStatus status, Pageable pageable);

    @Query("""
    SELECT CASE WHEN COUNT(bl) > 0 THEN true ELSE false END
        FROM BookLoan bl
        WHERE bl.user.id = :userId
          AND bl.book.id = :bookId
          AND (bl.bookLoanStatus = 'CHECKED_OUT' OR bl.bookLoanStatus = 'OVERDUE')
    """)
    boolean hasActiveCheckout(
            @Param("userId") Long userId,
            @Param("bookId") Long bookId
    );

    @Query("""
        SELECT COUNT(bl) FROM BookLoan bl
        WHERE bl.user.id = :userId
          AND (bl.bookLoanStatus = 'CHECKED_OUT' OR bl.bookLoanStatus = 'OVERDUE')
    """)
    Long countActiveBookLoansByUserId(
            @Param("userId") Long userId);

    @Query("""
        SELECT COUNT(bl) FROM BookLoan bl
                WHERE bl.user.id = :userId
                  AND bl.bookLoanStatus = 'OVERDUE'
    """)
    Long countOverdueBookLoansByUser(
            @Param("userId") Long userId);

    @Query("SELECT bl FROM BookLoan bl WHERE bl.bookLoanStatus = 'CHECKED_OUT' AND bl.dueDate < :currentDate")
    List<BookLoan> findOverdueBookLoans(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT bl FROM BookLoan bl WHERE bl.bookLoanStatus = 'CHECKED_OUT' AND bl.dueDate < :currentDate")
    Page<BookLoan> findOverdueBookLoansPage(@Param("currentDate") LocalDate currentDate, Pageable pageable);

    @Query("SELECT bl FROM BookLoan bl WHERE bl.bookLoanStatus = 'OVERDUE'")
    Page<BookLoan> findUnpaidBookLoans(Pageable pageable);

    @Query("SELECT bl FROM BookLoan bl WHERE bl.checkoutDate >= :startDate AND bl.checkoutDate <= :endDate")
    Page<BookLoan> findBookLoansByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, Pageable pageable);
}


