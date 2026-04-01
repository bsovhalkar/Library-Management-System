package com.app.Library_Management.service;

import com.app.Library_Management.exception.BookNotFoundException;
import com.app.Library_Management.exception.UserNotFoundException;
import com.app.Library_Management.exception.WishlistException;
import com.app.Library_Management.payload.dto.WishlistDto;
import com.app.Library_Management.payload.response.PageResponse;
import org.springframework.data.domain.PageRequest;

public interface WishlistService {
        WishlistDto addToWishlist(Long bookId,String notes) throws UserNotFoundException, BookNotFoundException, WishlistException;
//        void removeFromWishlist(Long userId, Long bookId);
        void removeFromWishlist(Long bookId) throws UserNotFoundException, WishlistException;
        PageResponse<WishlistDto> getMyWishlist(int page, int size) throws UserNotFoundException;
}
