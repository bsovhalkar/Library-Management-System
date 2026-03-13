package com.app.Library_Management.exception;

import com.app.Library_Management.payload.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(GenreNotFoundException.class)
    public ResponseEntity<ApiResponse> handleGenreException(GenreNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), false));
    }

    @ExceptionHandler(ParentNotFoundException.class)
    public ResponseEntity<ApiResponse> handleParentNotFound(ParentNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), false));
    }

    @ExceptionHandler(ParentAndChildCantBeSame.class)
    public ResponseEntity<ApiResponse>  handleParentAndChildCantBeSame(ParentAndChildCantBeSame e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), false));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(BookNotFoundException.class)

    public ResponseEntity<ApiResponse> handleBookNotFound(BookNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), false));
    }
    @ExceptionHandler(BookAlreadyExistException.class)
    public ResponseEntity<ApiResponse> handleBookAlreadyExist(BookAlreadyExistException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), false));
    }
}
