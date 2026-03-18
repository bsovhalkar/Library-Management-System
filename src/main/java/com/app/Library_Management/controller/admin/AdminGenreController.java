package com.app.Library_Management.controller.admin;

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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/genres")
public class AdminGenreController {
    private final GenreService genreService;

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

