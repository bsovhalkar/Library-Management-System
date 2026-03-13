package com.app.Library_Management.service;

import com.app.Library_Management.exception.GenreNotFoundException;
import com.app.Library_Management.exception.ParentAndChildCantBeSame;
import com.app.Library_Management.exception.ParentNotFoundException;
import com.app.Library_Management.payload.dto.GenreDTO;

import java.util.List;

public interface GenreService {

    GenreDTO createGenre(GenreDTO genre);
    GenreDTO updateGenre(Long id,GenreDTO genre) throws GenreNotFoundException , ParentNotFoundException, ParentAndChildCantBeSame;
    void deleteGenreById(Long id) throws GenreNotFoundException;
    List<GenreDTO> getAllGenre();
    GenreDTO getGenreById (Long id) throws GenreNotFoundException;
    List<GenreDTO> getAllActiveGenresWithSubGenres(Long parentGenreId) throws ParentNotFoundException;
    void hardDeleteGenreById(Long id) throws GenreNotFoundException;

    List<GenreDTO> getTopLevelGenres();

//    Page<GenreDTO> searchGenre(GenreDTO genre, Pageable pageable);

    Long getTotalActiveGenres();

    Long getBookCountByGenreId(Long id);
}
