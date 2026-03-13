package com.app.Library_Management.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String isbn;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;



    @ManyToOne
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;
    private String publisher;
    private LocalDate publishedDate;
    private String language;
    private Integer numberOfPages;
    private String description;
    @Column(nullable = false)
    private Integer totalCopies;
    @Column(nullable = false)
    private Integer availableCopies;
    private String coverImgUrl;
    private BigDecimal price;
    @Column(nullable = false)
    private Boolean active = true;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdDate;
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedDate;

    @AssertTrue(message = "Available copies cannot exceed total copies")
    public Boolean isAvailableCopiesValid() {
        if(availableCopies == null || totalCopies == null){
            return true;
        }
        return availableCopies<= totalCopies;
    }

}
