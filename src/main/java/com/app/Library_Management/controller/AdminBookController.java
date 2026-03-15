package com.app.Library_Management.controller;

import com.app.Library_Management.exception.BookAlreadyExistException;
import com.app.Library_Management.exception.BookNotFoundException;
import com.app.Library_Management.exception.GenreNotFoundException;
import com.app.Library_Management.payload.dto.BookDTO;
import com.app.Library_Management.payload.response.ApiResponse;
import com.app.Library_Management.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/book")
public class AdminBookController {
    private final BookService bookService;

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

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable long id, @Valid @RequestBody BookDTO bookDTO) throws BookNotFoundException, GenreNotFoundException {
        BookDTO result = bookService.updateBook(id, bookDTO);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBookById(@PathVariable long id) throws BookNotFoundException {
        bookService.deleteBook(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Book Deleted Temporary !", false));
    }

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<?> deleteHardBookById(@PathVariable long id) throws BookNotFoundException {
        bookService.hardDeleteBook(id);
        return ResponseEntity.ok(new ApiResponse("Book Deleted Permanently !", false));
    }
}

