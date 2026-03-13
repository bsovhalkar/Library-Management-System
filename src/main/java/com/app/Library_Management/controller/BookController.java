package com.app.Library_Management.controller;

import com.app.Library_Management.exception.BookAlreadyExistException;
import com.app.Library_Management.exception.BookNotFoundException;
import com.app.Library_Management.exception.GenreNotFoundException;
import com.app.Library_Management.model.Book;
import com.app.Library_Management.payload.dto.BookDTO;
import com.app.Library_Management.payload.request.BookSearchRequest;
import com.app.Library_Management.payload.response.ApiResponse;
import com.app.Library_Management.payload.response.PageResponse;
import com.app.Library_Management.service.BookService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;
//    @GetMapping("/")
//    public ResponseEntity<List<BookDTO>> getBooks() {
//        return ResponseEntity.ok(bookService.getAllBooks());
//    }

    @GetMapping("/")
    public ResponseEntity<PageResponse<BookDTO>> searchBooks(
            @RequestParam(required = false)Long genreId,
            @RequestParam(required = false,defaultValue = "false") Boolean availableOnly,
            @RequestParam(defaultValue = "true") boolean activeOnly,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(defaultValue = "createdDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection
    ) {
        BookSearchRequest bookSearchRequest = new BookSearchRequest();
        bookSearchRequest.setGenreId(genreId);
        bookSearchRequest.setAvailableOnly(availableOnly);
//        bookSearchRequest.setAvailableOnly(activeOnly);
        bookSearchRequest.setPage(page);
        bookSearchRequest.setPageSize(pageSize);
        bookSearchRequest.setSortBy(sortBy);
        bookSearchRequest.setSortDirection(sortDirection);
        PageResponse<BookDTO> pageResponse = bookService.searchBookWithFilters(bookSearchRequest);
        return ResponseEntity.ok(pageResponse);
    }

    @PostMapping("/create")
    public ResponseEntity<BookDTO> createBook(@Valid @RequestBody BookDTO bookDTO) throws BookAlreadyExistException, GenreNotFoundException {
        BookDTO created = bookService.createBook(bookDTO);
        return ResponseEntity.ok(created);
    }

    @PostMapping("/create/bulk")
    public ResponseEntity<List<BookDTO>> createBookBulk(@Valid @RequestBody List<BookDTO> bookDTOs) throws BookAlreadyExistException, GenreNotFoundException {
        List<BookDTO> created = bookService.createBookBulk(bookDTOs);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable long id) throws BookNotFoundException {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable long id, @Valid @RequestBody BookDTO bookDTO) throws BookNotFoundException, GenreNotFoundException {
        BookDTO result = bookService.updateBook(id,bookDTO);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBookById(@PathVariable long id) throws BookNotFoundException {
        bookService.deleteBook(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Book Deleted Temporary !",false));
    }

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<?> deleteHardBookById(@PathVariable long id) throws BookNotFoundException {
        bookService.hardDeleteBook(id);
        return ResponseEntity.ok(new ApiResponse("Book Deleted Permanently  !",false));
    }

    @PostMapping("/search")
    public ResponseEntity<PageResponse<BookDTO>> advancedSearch(@RequestBody BookSearchRequest bookSearchRequest) {
        PageResponse<BookDTO> pageResponse = bookService.searchBookWithFilters(bookSearchRequest);
        return ResponseEntity.ok(pageResponse);
    }

    @GetMapping("/states")
    public ResponseEntity<BookStatesResponse> getBookStates() {
        long totalActiveBooks = bookService.getTotalActiveCopies();
        long totalAvailableBooks = bookService.getTotalAvailableCopies();
        return ResponseEntity.ok(new BookStatesResponse(totalActiveBooks, totalAvailableBooks));
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class BookStatesResponse {
        public long totalActiveBooks;
        public long totalAvailableBooks;
    }
}
