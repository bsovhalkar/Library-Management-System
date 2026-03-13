package com.app.Library_Management.model;

import jakarta.persistence.*;
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

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Genre code is mandatory")
    private String code;
    @NotBlank(message = "Genre name is mandatory")
    private String name;

    @Size(max = 500,message = "Description must not exceed 500 characters")
    private String description;

    @Min(value = 0,message = "display cannot be negative")
    private Integer displayOrder;

    @Column(nullable = false)
    private Boolean active = true;
    @ManyToOne
    @JoinColumn(name = "parent_genre_id")
    private Genre parentGenre;

    @OneToMany(mappedBy = "parentGenre")
    private List<Genre> subGenres = new ArrayList<>();

//    @OneToMany(mappedBy = "genre", cascade = CascadeType.PERSIST)
//    private List<Book> books = new ArrayList<Book>();

    @CreationTimestamp
    private LocalDateTime creationAt;

    @UpdateTimestamp
    private LocalDateTime updateAt;


}
