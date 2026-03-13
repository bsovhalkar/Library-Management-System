package com.app.Library_Management.controller;


import com.app.Library_Management.exception.GenreNotFoundException;
import com.app.Library_Management.exception.ParentAndChildCantBeSame;
import com.app.Library_Management.exception.ParentNotFoundException;
import com.app.Library_Management.payload.dto.GenreDTO;
import com.app.Library_Management.payload.response.ApiResponse;
import com.app.Library_Management.service.GenreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/genres")
public class GenreController {
    private final GenreService genreService;

    @GetMapping("/")
    public ResponseEntity<List<GenreDTO>> getAllGenre() {
//        genreService.getAllGenre();
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


    @PostMapping("/create")
    public ResponseEntity<?> createGenre(@Valid @RequestBody GenreDTO genreDTO){
        try{
        GenreDTO res = genreService.createGenre(genreDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(res);
        }
        catch(Exception e){
            return new  ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }



    @PutMapping("/{genreId}")
    public ResponseEntity<?> updateGenreById(@PathVariable("genreId") Long genreId, @RequestBody GenreDTO genreDTO) throws GenreNotFoundException, ParentNotFoundException, ParentAndChildCantBeSame {
        GenreDTO updatedGenre = genreService.updateGenre(genreId, genreDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updatedGenre);
    }

    @DeleteMapping("/{genreId}")
    public ResponseEntity<?> deleteGenre(@PathVariable("genreId") Long genreId) throws GenreNotFoundException {
        genreService.deleteGenreById(genreId);
        ApiResponse apiResponse = new ApiResponse("Genre with id "+ genreId + " is Deleted Temporary!",true);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    @DeleteMapping("/{genreId}/hard")
    public ResponseEntity<ApiResponse> hardDeleteGenre(@PathVariable("genreId") Long genreId) throws GenreNotFoundException {
        genreService.hardDeleteGenreById(genreId);
        ApiResponse apiResponse = new ApiResponse("Genre with id "+ genreId + " is Deleted Permanently!",true);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }





}
