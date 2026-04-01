package com.app.Library_Management.service.impl;

import com.app.Library_Management.exception.BookNotFoundException;
import com.app.Library_Management.exception.UserNotFoundException;
import com.app.Library_Management.exception.WishlistException;
import com.app.Library_Management.mapper.WishlistMapper;
import com.app.Library_Management.model.Book;
import com.app.Library_Management.model.User;
import com.app.Library_Management.model.Wishlist;
import com.app.Library_Management.payload.dto.WishlistDto;
import com.app.Library_Management.payload.response.PageResponse;
import com.app.Library_Management.repository.BookRepository;
import com.app.Library_Management.repository.WishlistRepository;
import com.app.Library_Management.service.UserService;
import com.app.Library_Management.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class WishlistServiceImp implements WishlistService {
    private final UserService userService;
    private final WishlistRepository wishlistRepository;
//    private final BookService bookService;
    private final BookRepository bookRepository;
    private final WishlistMapper  wishlistMapper;
    @Override
    public WishlistDto addToWishlist(Long bookId,String notes) throws UserNotFoundException, BookNotFoundException, WishlistException {
        User currentUser = userService.getCurrentUser();

        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException("Book not found with id: " + bookId));
        if(wishlistRepository.existsByBookIdAndUserId(bookId,currentUser.getId())){
            throw new WishlistException("Book is already in the wishlist");
        }
        Wishlist wishlist = new Wishlist();
        wishlist.setUser(currentUser);
        wishlist.setBook(book);
        wishlist.setNotes(notes);
        Wishlist saved = wishlistRepository.save(wishlist);

        return wishlistMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public void removeFromWishlist(Long bookId) throws UserNotFoundException, WishlistException {
        User currentUser = userService.getCurrentUser();
        if(!wishlistRepository.existsByBookIdAndUserId(bookId,currentUser.getId())){
            throw new WishlistException("Book is not in the wishlist");
        }
        wishlistRepository.deleteByUserIdAndBookId(currentUser.getId(),bookId);
    }

    @Override
    public PageResponse<WishlistDto> getMyWishlist(int page, int size) throws UserNotFoundException {
        Pageable pageable = PageRequest.of(page, size, Sort.by("addedAt").descending());
        Page<Wishlist> pageResponse = wishlistRepository.findByUserId(userService.getCurrentUser().getId(), pageable);

        PageResponse<WishlistDto> response = new PageResponse<>();
        response.setContent(pageResponse.getContent().stream().map(wishlistMapper::toDTO).toList());
        response.setPageNumber(pageResponse.getNumber());
        response.setPageSize(pageResponse.getSize());
        response.setTotalElements(pageResponse.getTotalElements());
        response.setTotalPages(pageResponse.getTotalPages());
        response.setLast(pageResponse.isLast());
        response.setFirst(pageResponse.isFirst());
        response.setEmpty(pageResponse.isEmpty());

        return response;
    }
}
