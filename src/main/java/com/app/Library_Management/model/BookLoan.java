package com.app.Library_Management.model;


import com.app.Library_Management.domain.BookLoanStatus;
import com.app.Library_Management.domain.BookLoanType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Entity
@Table(name = "book_loan", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_book_id", columnList = "book_id"),
        @Index(name = "idx_status", columnList = "book_loan_status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookLoan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(nullable = false)
    @ManyToOne
    private  User user;
    @JoinColumn(nullable = false)
    @ManyToOne
    private  Book book;

    private BookLoanType bookLoanType;

    @Enumerated(EnumType.STRING)
    private BookLoanStatus bookLoanStatus;
    @Column(nullable = false)
    private LocalDate checkoutDate;
    @Column(nullable = false)
    private LocalDate dueDate;
    @Column(nullable = true)
    private LocalDate returnDate;

    @Column(nullable = false)
    private Long renewalCount = 0L;

    @Column(nullable = false)
    private Long maxRenewalAllowed = 2L;

    @Size(max = 500,message = "Notes cannot exceed 500 characters")
    private String notes;

    @Column(nullable = false)
    private Boolean isOverdue = false;

    @Column(nullable = false)
    private Long overdueDays = 0L;

    @CreationTimestamp
    private LocalDate createdAt;
    @UpdateTimestamp
    private LocalDate updatedAt;


    public Boolean isActive() {
        return bookLoanStatus==BookLoanStatus.CHECKED_OUT || bookLoanStatus==BookLoanStatus.OVERDUE;
    }

    public boolean canRenew() {
        return bookLoanStatus==BookLoanStatus.CHECKED_OUT  && renewalCount < maxRenewalAllowed && !isOverdue;
    }
}
