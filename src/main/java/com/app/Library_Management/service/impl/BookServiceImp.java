package com.app.Library_Management.service.impl;

import com.app.Library_Management.exception.BookAlreadyExistException;
import com.app.Library_Management.exception.BookNotFoundException;
import com.app.Library_Management.exception.GenreNotFoundException;
import com.app.Library_Management.mapper.BookMapper;
import com.app.Library_Management.model.Book;
import com.app.Library_Management.model.Genre;
import com.app.Library_Management.payload.dto.BookDTO;
import com.app.Library_Management.payload.request.BookSearchRequest;
import com.app.Library_Management.payload.response.PageResponse;
import com.app.Library_Management.repository.BookRepository;
import com.app.Library_Management.repository.GenreRepository;
import com.app.Library_Management.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImp implements BookService {
    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    @Override
    public BookDTO createBook(BookDTO bookDTO) throws BookAlreadyExistException,GenreNotFoundException {
        if(bookDTO == null) return null;
        if(bookRepository.existsByIsbn(bookDTO.getIsbn())) throw new BookAlreadyExistException("Book with ISBN code " + bookDTO.getIsbn()+ " already exists");
        Genre genre = null;
        if (bookDTO.getGenreId() != null) {
            genre = genreRepository.findById(bookDTO.getGenreId())
                    .orElseThrow(() ->
                            new GenreNotFoundException("Genre with ID " + bookDTO.getGenreId() + " not found"));
        }
        Book book = BookMapper.toEntity(bookDTO, genre);
        book.isAvailableCopiesValid();
        book = bookRepository.save(book);
        return BookMapper.toDTO(book);
    }

    @Override
    public List<BookDTO> createBookBulk(List<BookDTO> listBookDTO) throws BookAlreadyExistException, GenreNotFoundException {
        List<BookDTO> res = new ArrayList<>();
        for(BookDTO bookDTO : listBookDTO){
            if(bookDTO == null) return null;
            res.add(createBook(bookDTO));
        }
        return res;

    }
    @Override
    public BookDTO getBookById(Long bookId) throws BookNotFoundException {
        Book book = bookRepository.findById(bookId).orElseThrow(()-> new BookNotFoundException("Book with ID " + bookId + " not found"));
        return BookMapper.toDTO(book);
    }

    @Override
    public BookDTO getBookByISBN(String isbn) throws BookNotFoundException {
        Book book = bookRepository.findBookByIsbn(isbn).orElseThrow(()-> new BookNotFoundException("Book with ISBN " + isbn + " not found"));
        return BookMapper.toDTO(book);
    }

    @Override
    public BookDTO updateBook(Long id, BookDTO bookDTO) throws BookNotFoundException, GenreNotFoundException {
        Book bookTuBeUpdated = bookRepository.findById(id).orElseThrow(()-> new BookNotFoundException("Book with ID " + id + " not found"));
        Genre genre = null;
        if (bookDTO.getGenreId() != null) {
            genre = genreRepository.findById(bookDTO.getGenreId())
                    .orElseThrow(() ->
                            new GenreNotFoundException("Genre with ID " + bookDTO.getGenreId() + " not found"));
        }

        BookMapper.updateEntityFromDTO(bookDTO,bookTuBeUpdated,genre);
        bookTuBeUpdated.isAvailableCopiesValid();
        bookTuBeUpdated = bookRepository.save(bookTuBeUpdated);
        return BookMapper.toDTO(bookTuBeUpdated);
    }

    @Override
    public void deleteBook(Long id) throws BookNotFoundException {
        Book bookToBeDeleted = bookRepository.findById(id).orElseThrow(()-> new BookNotFoundException("Book with ID " + id + " not found"));
        bookToBeDeleted.setActive(false);
        bookRepository.save(bookToBeDeleted);

    }

    @Override
    public void hardDeleteBook(Long id) throws BookNotFoundException {
        Book bookToBeDeleted = bookRepository.findById(id).orElseThrow(()-> new BookNotFoundException("Book with ID " + id + " not found"));
        bookRepository.delete(bookToBeDeleted);
    }

    @Override
    public List<BookDTO> getAllBooks() {
        List<Book> res = bookRepository.findAll();
        return BookMapper.toDTOList(res);
    }

    @Override
    public List<BookDTO> getBookBulk() {
        return List.of();
    }


    @Override
    public PageResponse<BookDTO> searchBookWithFilters(BookSearchRequest request) {

        int page = Math.max(request.getPage(), 0);
        int size = Math.min(Math.max(request.getPageSize(), 1), 100);

        String sortBy = Optional.ofNullable(request.getSortBy()).orElse("id");

        Sort sort = "ASC".equalsIgnoreCase(request.getSortDirection())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Book> bookPage = bookRepository.searchBookWithFilters(
                request.getSearchTerm(),
                request.getGenreId(),
                request.getAvailableOnly(),
                pageable
        );

        List<BookDTO> content = bookPage.getContent()
                .stream()
                .map(BookMapper::toDTO)
                .toList();

        return PageResponse.<BookDTO>builder()
                .content(content)
                .pageNumber(bookPage.getNumber())
                .pageSize(bookPage.getSize())
                .totalElements(bookPage.getTotalElements())
                .totalPages(bookPage.getTotalPages())
                .first(bookPage.isFirst())
                .last(bookPage.isLast())
                .empty(bookPage.isEmpty())
                .build();
    }

    @Override
    public Long getTotalActiveCopies() {
        return bookRepository.countByActiveTrue();
    }

    @Override
    public Long getTotalAvailableCopies() {
        return bookRepository.countByAvailableCopiesGreaterThan(0);
    }
}
