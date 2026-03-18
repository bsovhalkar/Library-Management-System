package com.app.Library_Management.controller.user;

import com.app.Library_Management.exception.GenreNotFoundException;
import com.app.Library_Management.payload.dto.GenreDTO;
import com.app.Library_Management.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/genres")
public class UserGenreController {
    private final GenreService genreService;

    @GetMapping("/")
    public ResponseEntity<List<GenreDTO>> getAllGenre() {
        return new ResponseEntity<>(genreService.getAllGenre(), HttpStatus.OK);
    }

    @GetMapping("/{genreId}")
    public ResponseEntity<?> getGenreById(@PathVariable("genreId") Long genreId) throws GenreNotFoundException {
        GenreDTO res = genreService.getGenreById(genreId);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @GetMapping("/top-level")
    public ResponseEntity<List<GenreDTO>> getTopLevelGenre() {
        return new ResponseEntity<>(genreService.getTopLevelGenres(), HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getGenreCount() {
        return ResponseEntity.status(HttpStatus.OK).body(genreService.getTotalActiveGenres());
    }

    @GetMapping("/{genreId}/book-count")
    public ResponseEntity<Long> getBookCount(@PathVariable("genreId") Long genreId) {
        Long count = genreService.getBookCountByGenreId(genreId);
        return ResponseEntity.status(HttpStatus.OK).body(count);
    }
}

