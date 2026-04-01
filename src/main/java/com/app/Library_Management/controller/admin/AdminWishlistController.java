package com.app.Library_Management.controller.admin;

import com.app.Library_Management.exception.UserNotFoundException;
import com.app.Library_Management.exception.WishlistException;
import com.app.Library_Management.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/wishlist")
public class AdminWishlistController {
    private final WishlistService wishlistService;

    @DeleteMapping("/remove/{bookId}")
    public ResponseEntity<String> removeFromWishlist(@PathVariable Long bookId)
            throws UserNotFoundException, WishlistException {
        wishlistService.removeFromWishlist(bookId);
        return ResponseEntity.ok("Book removed from wishlist successfully");
    }
}

