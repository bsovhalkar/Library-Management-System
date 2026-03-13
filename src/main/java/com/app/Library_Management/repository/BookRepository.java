package com.app.Library_Management.repository;

import com.app.Library_Management.model.Book;
import com.app.Library_Management.payload.request.BookSearchRequest;
import com.app.Library_Management.payload.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findBookByIsbn(String isbn);

    boolean existsByIsbn(String isbn);

    @Query("""
    SELECT b FROM Book b
    WHERE
    (
        :searchTerm IS NULL OR
        LOWER(b.title) LIKE LOWER(CONCAT('%', :searchTerm,'%')) OR
        LOWER(b.author) LIKE LOWER(CONCAT('%', :searchTerm,'%')) OR
        LOWER(b.isbn) LIKE LOWER(CONCAT('%', :searchTerm,'%'))
    )
    AND (:genreId IS NULL OR b.genre.id = :genreId)
    AND (:availableOnly IS NULL OR :availableOnly = false OR b.availableCopies > 0)
    AND b.active = true
    """)
    Page<Book> searchBookWithFilters(
            @Param("searchTerm") String searchTerm,
            @Param("genreId") Long genreId,
            @Param("availableOnly") Boolean availableOnly,
            Pageable pageable
    );

    Long countByActiveTrue();

    Long countByAvailableCopiesGreaterThan(int copies);
}