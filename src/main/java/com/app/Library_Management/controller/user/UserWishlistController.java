package com.app.Library_Management.controller.user;

import com.app.Library_Management.exception.BookNotFoundException;
import com.app.Library_Management.exception.UserNotFoundException;
import com.app.Library_Management.exception.WishlistException;
import com.app.Library_Management.payload.dto.WishlistDto;
import com.app.Library_Management.payload.response.ApiResponse;
import com.app.Library_Management.payload.response.PageResponse;
import com.app.Library_Management.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishlist")
public class UserWishlistController {
    private final WishlistService wishlistService;

    @PostMapping("/add")
    public ResponseEntity<WishlistDto> addToWishlist(
            @RequestParam Long bookId,
            @RequestParam(required = false) String notes
    ) throws UserNotFoundException, BookNotFoundException, WishlistException {
        WishlistDto wishlistDto = wishlistService.addToWishlist(bookId, notes);
        return ResponseEntity.status(HttpStatus.CREATED).body(wishlistDto);
    }

    @DeleteMapping("/remove/{bookId}")
    public ResponseEntity<ApiResponse> removeFromWishlist(@PathVariable Long bookId)
            throws UserNotFoundException, WishlistException {
        wishlistService.removeFromWishlist(bookId);
        return ResponseEntity.ok(new ApiResponse("Book removed from wishlist successfully",false));
    }

    @GetMapping("/my-wishlist")
    public ResponseEntity<PageResponse<WishlistDto>> getMyWishlist(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) throws UserNotFoundException {
        PageResponse<WishlistDto> pageResponse = wishlistService.getMyWishlist(page, size);
        return ResponseEntity.ok(pageResponse);
    }
}

