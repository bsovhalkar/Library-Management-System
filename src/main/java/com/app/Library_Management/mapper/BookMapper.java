package com.app.Library_Management.mapper;

import com.app.Library_Management.exception.BookNotFoundException;
import com.app.Library_Management.payload.dto.BookDTO;
import com.app.Library_Management.model.Book;
import com.app.Library_Management.model.Genre;
import com.app.Library_Management.payload.response.PageResponse;
import org.springframework.data.domain.Page;
import java.util.List;

public class BookMapper {

    public static BookDTO toDTO(Book book) {

        if (book == null) {
            return null;
        }

        return BookDTO.builder()
                .id(book.getId())
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .author(book.getAuthor())
                .genreId(book.getGenre() != null ? book.getGenre().getId() : null)
                .genreCode(book.getGenre() != null ? book.getGenre().getCode() : null)
                .genreName(book.getGenre() != null ? book.getGenre().getName() : null)
                .publisher(book.getPublisher())
                .publishedDate(book.getPublishedDate())
                .language(book.getLanguage())
                .numberOfPages(book.getNumberOfPages())
                .description(book.getDescription())
                .totalCopies(book.getTotalCopies())
                .availableCopies(book.getAvailableCopies())
                .coverImgUrl(book.getCoverImgUrl())
                .price(book.getPrice())
                .active(book.getActive())
                .createdDate(book.getCreatedDate())
                .updatedDate(book.getUpdatedDate())
                .build();
    }
    public static Book toEntity(BookDTO dto, Genre genre){

        if (dto == null) {
            return null;
        }

        return Book.builder()
                .id(dto.getId())
                .isbn(dto.getIsbn())
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .genre(genre)
                .publisher(dto.getPublisher())
                .publishedDate(dto.getPublishedDate())
                .language(dto.getLanguage())
                .numberOfPages(dto.getNumberOfPages())
                .description(dto.getDescription())
                .totalCopies(dto.getTotalCopies())
                .availableCopies(dto.getAvailableCopies())
                .coverImgUrl(dto.getCoverImgUrl())
                .price(dto.getPrice())
                .active(dto.getActive())
                .build();
    }
    public static List<BookDTO> toDTOList(List<Book> books) {

        if (books == null) {
            return List.of();
        }

        return books.stream()
                .map(BookMapper::toDTO)
                .toList();
    }
    public static void updateEntityFromDTO(BookDTO dto, Book book, Genre genre) {

        if (dto == null || book == null) {
            return;
        }


        if (dto.getTitle() != null) {
            book.setTitle(dto.getTitle());
        }

        if (dto.getAuthor() != null) {
            book.setAuthor(dto.getAuthor());
        }

        if (genre != null) {
            book.setGenre(genre);
        }

        if (dto.getPublisher() != null) {
            book.setPublisher(dto.getPublisher());
        }

        if (dto.getPublishedDate() != null) {
            book.setPublishedDate(dto.getPublishedDate());
        }

        if (dto.getLanguage() != null) {
            book.setLanguage(dto.getLanguage());
        }

        if (dto.getNumberOfPages() != null) {
            book.setNumberOfPages(dto.getNumberOfPages());
        }

        if (dto.getDescription() != null) {
            book.setDescription(dto.getDescription());
        }

        if (dto.getTotalCopies() != null) {
            book.setTotalCopies(dto.getTotalCopies());
        }

        if (dto.getAvailableCopies() != null) {
            book.setAvailableCopies(dto.getAvailableCopies());
        }

        if (dto.getCoverImgUrl() != null) {
            book.setCoverImgUrl(dto.getCoverImgUrl());
        }

        if (dto.getPrice() != null) {
            book.setPrice(dto.getPrice());
        }

        if (dto.getActive() != null) {
            book.setActive(dto.getActive());
        }

    }
}