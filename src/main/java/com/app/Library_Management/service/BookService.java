package com.app.Library_Management.service;

import com.app.Library_Management.exception.BookAlreadyExistException;
import com.app.Library_Management.exception.BookNotFoundException;
import com.app.Library_Management.exception.GenreNotFoundException;
import com.app.Library_Management.payload.dto.BookDTO;
import com.app.Library_Management.payload.response.PageResponse;
import com.app.Library_Management.payload.request.BookSearchRequest;
import java.util.List;

public interface BookService {
    BookDTO createBook(BookDTO bookDTO) throws BookAlreadyExistException, GenreNotFoundException;
    BookDTO getBookById(Long bookId) throws BookNotFoundException;
    BookDTO getBookByISBN(String isbn) throws BookNotFoundException;
    BookDTO updateBook(Long id,BookDTO bookDTO) throws BookNotFoundException, GenreNotFoundException;
    void deleteBook(Long id) throws BookNotFoundException;
    void hardDeleteBook(Long id) throws BookNotFoundException;
    List<BookDTO> getAllBooks();
    List<BookDTO> getBookBulk();
    List<BookDTO> createBookBulk(List<BookDTO> listBookDTO) throws BookAlreadyExistException, GenreNotFoundException;

    PageResponse<BookDTO> searchBookWithFilters(BookSearchRequest bookSearchRequest);
    Long getTotalActiveCopies();
    Long getTotalAvailableCopies();
}
