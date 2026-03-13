package com.app.Library_Management.repository;

import com.app.Library_Management.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre,Long> {

    List<Genre> findByActiveTrueOrderByDisplayOrderAsc();
    List<Genre> findByParentGenreIsNullAndActiveTrueOrderByDisplayOrderAsc();
    List<Genre> findByParentGenreIdAndActiveTrueOrderByDisplayOrderAsc(Long parentId);
    Long countByActiveTrue();
    Long countById(Long genreId);
}
