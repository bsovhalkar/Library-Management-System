package com.app.Library_Management.model;

import com.app.Library_Management.domain.FineStatus;
import com.app.Library_Management.domain.FineType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private  User user;

    @ManyToOne
    @JoinColumn(nullable = false)
    private BookLoan  bookLoan;

    private FineType fineType;

    @Column(nullable = false)
    private Long amount;



    private FineStatus fineStatus;

    @Column(length = 500)
    private String reason;

    @Column(length = 1000)
    private String notes;


    @ManyToOne
    private User waivedBy;

    @Column(name = "waived_at")
    private LocalDate waivedAt;

    @Column(name = "paid_at")
    private LocalDate paidAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "processed_by_user_id")
    private  User processedBy;

    @Column(name = "transaction_id", length = 100)
    private String transactionId;


    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDate createdAt;
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDate updatedAt;


}
