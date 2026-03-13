package com.app.Library_Management.payload.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDTO {

    private Long id;

    @NotBlank(message = "ISBN is mandatory")
    @Size(max = 20, message = "ISBN must not exceed 20 characters")
    private String isbn;

    @NotBlank(message = "Book title is mandatory")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @NotBlank(message = "Author name is mandatory")
    @Size(max = 255, message = "Author name must not exceed 255 characters")
    private String author;

    @NotNull(message = "Genre id is mandatory")
    private Long genreId;

    @NotBlank(message = "Genre code is mandatory")
    @Size(max = 50, message = "Genre code must not exceed 50 characters")
    private String genreCode;

    private String genreName;

    @Size(max = 255, message = "Publisher name must not exceed 255 characters")
    private String publisher;

    private LocalDate publishedDate;

    @Size(max = 50, message = "Language must not exceed 50 characters")
    private String language;

    @Min(value = 1, message = "Number of pages must be at least 1")
    private Integer numberOfPages;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @NotNull(message = "Total copies is mandatory")
    @Min(value = 0, message = "Total copies cannot be negative")
    private Integer totalCopies;

    @NotNull(message = "Available copies is mandatory")
    @Min(value = 0, message = "Available copies cannot be negative")
    private Integer availableCopies;

    @Size(max = 500, message = "Cover image URL must not exceed 500 characters")
    private String coverImgUrl;

    @DecimalMin(value = "0.0", inclusive = true, message = "Price cannot be negative")
    private BigDecimal price;

    @NotNull(message = "Active cannot be NULL")
    private Boolean active = true;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;
}