package com.app.Library_Management.service.impl;

import com.app.Library_Management.exception.GenreNotFoundException;
import com.app.Library_Management.exception.ParentAndChildCantBeSame;
import com.app.Library_Management.exception.ParentNotFoundException;
import com.app.Library_Management.mapper.GenreMapper;
import com.app.Library_Management.model.Genre;
import com.app.Library_Management.payload.dto.GenreDTO;
import com.app.Library_Management.repository.GenreRepository;
import com.app.Library_Management.service.GenreService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImp implements GenreService {
    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;
    
    @Override
    public GenreDTO createGenre(GenreDTO dto) {
        Genre genre = genreMapper.toGenreEntity(dto);

        if (dto.getParentGenreId() != null) {
            Genre parent = genreRepository.findById(dto.getParentGenreId())
                    .orElseThrow(() -> new RuntimeException("Parent genre not found"));
            genre.setParentGenre(parent);
            if (parent.getSubGenres() == null) {
                parent.setSubGenres(new ArrayList<>());
            }

            parent.getSubGenres().add(genre);

        }


        Genre savedGenre = genreRepository.save(genre);

        return genreMapper.toGenreDTO(savedGenre);

    }

    @Override
    public GenreDTO updateGenre(Long id, GenreDTO dto)
            throws GenreNotFoundException, ParentNotFoundException, ParentAndChildCantBeSame {

        Genre genreToUpdate = genreRepository.findById(id)
                .orElseThrow(() -> new GenreNotFoundException("Genre with id " + id + " not found"));

        Genre updatedGenre = genreMapper.updateEntityFromDTO(dto, genreToUpdate);

        if (dto.getParentGenreId() != null) {
            if (dto.getParentGenreId().equals(id)) {
                throw new ParentAndChildCantBeSame("Parent and child cannot both be same");
            }

            Genre parent = genreRepository.findById(dto.getParentGenreId())
                    .orElseThrow(() -> new ParentNotFoundException("Parent genre not found"));

            updatedGenre.setParentGenre(parent);
        }

        genreRepository.save(updatedGenre);

        return genreMapper.toGenreDTO(updatedGenre);
    }

    @Override
    public void deleteGenreById(Long id) throws GenreNotFoundException{
        Genre genreToDelete  = genreRepository.findById(id).orElseThrow(()->new GenreNotFoundException("Genre with id "+ id +" not found"));
        genreToDelete.setActive(false);
        genreRepository.save(genreToDelete);
    }

    @Override
    public List<GenreDTO> getAllGenre() {

        return genreRepository.findAll()
                .stream()
                .map(genreMapper::toGenreDTO)
                .toList();
    }

    @Override
    public GenreDTO getGenreById(Long id) throws  GenreNotFoundException {
        Genre genre = genreRepository.findById(id).orElseThrow(() -> new GenreNotFoundException("Genre with id "+ id +" not found"));
        return genreMapper.toGenreDTO(genre);
    }

    @Transactional
    @Override
    public void hardDeleteGenreById(Long genreId) throws GenreNotFoundException {
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new GenreNotFoundException("Genre not found"));
        genreRepository.delete(genre);
    }
    @Override
    public List<GenreDTO> getAllActiveGenresWithSubGenres(Long parentGenreId) throws ParentNotFoundException {
        return genreRepository.findByParentGenreIdAndActiveTrueOrderByDisplayOrderAsc(parentGenreId)
                .stream()
                .map(genreMapper::toGenreDTO)
                .toList();
    }


    @Override
    public List<GenreDTO> getTopLevelGenres(){
        return genreRepository
                .findByParentGenreIsNullAndActiveTrueOrderByDisplayOrderAsc()
                .stream()
                .map(genreMapper::toGenreDTO)
                .toList();
    }

    @Override
    public Long getTotalActiveGenres() {
        return genreRepository.countByActiveTrue();
    }

    @Override
    public Long getBookCountByGenreId(Long id) {
        return 0L;
    }

//    @Override
//    public Page<GenreDTO> searchGenre(GenreDTO genre, Pageable pageable) {
//        return null;
//    }

}
