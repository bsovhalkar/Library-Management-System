package com.app.Library_Management.controller.user;

import com.app.Library_Management.exception.BookNotFoundException;
import com.app.Library_Management.payload.dto.BookDTO;
import com.app.Library_Management.payload.request.BookSearchRequest;
import com.app.Library_Management.payload.response.BookStatesResponse;
import com.app.Library_Management.payload.response.PageResponse;
import com.app.Library_Management.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book")
public class UserBookController {
    private final BookService bookService;

    @GetMapping("/")
    public ResponseEntity<PageResponse<BookDTO>> getAllBooks() {
        PageResponse<BookDTO> pageResponse = new PageResponse<>();
        pageResponse.setContent(bookService.getAllBooks());
        return ResponseEntity.ok(pageResponse);
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<BookDTO>> searchBooks(
            @RequestParam(required = false) Long genreId,
            @RequestParam(required = false, defaultValue = "false") Boolean availableOnly,
            @RequestParam(defaultValue = "true") boolean activeOnly,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(defaultValue = "createdDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection
    ) {
        BookSearchRequest bookSearchRequest = new BookSearchRequest();
        bookSearchRequest.setGenreId(genreId);
        bookSearchRequest.setAvailableOnly(availableOnly);
        bookSearchRequest.setPage(page);
        bookSearchRequest.setPageSize(pageSize);
        bookSearchRequest.setSortBy(sortBy);
        bookSearchRequest.setSortDirection(sortDirection);
        PageResponse<BookDTO> pageResponse = bookService.searchBookWithFilters(bookSearchRequest);
        return ResponseEntity.ok(pageResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable long id) throws BookNotFoundException {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PostMapping("/advanced-search")
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
}

