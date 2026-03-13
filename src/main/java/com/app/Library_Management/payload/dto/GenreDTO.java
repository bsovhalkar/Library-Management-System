package com.app.Library_Management.payload.dto;


import com.app.Library_Management.model.Genre;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenreDTO {
    private Long id;
    @NotBlank(message = "Genre code is mandatory")
    private String code;
    @NotBlank(message = "Genre name is mandatory")
    private String name;

    @Size(max = 500,message = "Description must not exceed 500 characters")
    private String description;

    @Min(value = 0,message = "display cannot be negative")
    private Integer displayOrder;

    private Boolean active;

    private Long parentGenreId;
    private String parentGenreName;



    private List<GenreDTO> subGenres ;

    private Long bookCount;

    @CreationTimestamp
    private LocalDateTime creationAt;
    @UpdateTimestamp
    private LocalDateTime updateAt;
}
