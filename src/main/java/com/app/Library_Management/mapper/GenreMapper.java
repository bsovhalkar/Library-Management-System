package com.app.Library_Management.mapper;

import com.app.Library_Management.model.Genre;
import com.app.Library_Management.payload.dto.GenreDTO;

public class GenreMapper {

    public static GenreDTO toGenreDTO(Genre genre) {
        if (genre == null) return null;

        GenreDTO dto = new GenreDTO();

        dto.setId(genre.getId());
        dto.setCode(genre.getCode());
        dto.setName(genre.getName());
        dto.setDescription(genre.getDescription());
        dto.setDisplayOrder(genre.getDisplayOrder());
        dto.setActive(genre.getActive());
        dto.setCreationAt(genre.getCreationAt());
        dto.setUpdateAt(genre.getUpdateAt());
        if (genre.getParentGenre() != null) {
            dto.setParentGenreId(genre.getParentGenre().getId());
            dto.setParentGenreName(genre.getParentGenre().getName());
        }

        if (genre.getSubGenres() != null) {
            dto.setSubGenres(
                    genre.getSubGenres()
                            .stream()
                            .map(g -> {
                                GenreDTO subDto = new GenreDTO();
                                subDto.setId(g.getId());
                                subDto.setName(g.getName());
                                subDto.setCode(g.getCode());
                                subDto.setDescription(g.getDescription());
                                subDto.setDisplayOrder(g.getDisplayOrder());
                                subDto.setActive(g.getActive());
                                subDto.setCreationAt(g.getCreationAt());
                                subDto.setUpdateAt(g.getUpdateAt());
                                return subDto;
                            })
                            .toList()
            );
        }

        return dto;
    }

    public static Genre toGenreEntity(GenreDTO dto) {

        Genre genre = new Genre();

        genre.setId(dto.getId());
        genre.setCode(dto.getCode());
        genre.setName(dto.getName());
        genre.setDescription(dto.getDescription());
        genre.setDisplayOrder(dto.getDisplayOrder());
        genre.setActive(dto.getActive());
        genre.setCreationAt(dto.getCreationAt());
        genre.setUpdateAt(dto.getUpdateAt());
        return genre;
    }

    public static Genre updateEntityFromDTO(GenreDTO dto, Genre genre){
        if(genre == null || dto == null) {
            return null;
        }
        if(dto.getCode()!=null && !dto.getCode().isBlank()){
            genre.setCode(dto.getCode());
        }
        if(dto.getName() != null){
            genre.setName(dto.getName());
        }

        if(dto.getDescription() != null){
            genre.setDescription(dto.getDescription());
        }
        if(dto.getDisplayOrder() != null){
            genre.setDisplayOrder(dto.getDisplayOrder());
        }
        if(dto.getActive() != null){
            genre.setActive(dto.getActive());
        }
        if(dto.getCreationAt() != null){
            genre.setCreationAt(dto.getCreationAt());
        }

        return genre;
    }
}