package com.app.Library_Management.mapper;

import com.app.Library_Management.model.Book;
import com.app.Library_Management.model.BookLoan;
import com.app.Library_Management.model.User;
import com.app.Library_Management.payload.dto.BookLoanDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class BookLoanMapper {

    public BookLoanDTO toDTO(BookLoan bookLoan) {
        if (bookLoan == null) {
            return null;
        }

        BookLoanDTO.BookLoanDTOBuilder builder = BookLoanDTO.builder()
                .id(bookLoan.getId())
                .bookLoanType(bookLoan.getBookLoanType())
                .bookLoanStatus(bookLoan.getBookLoanStatus())
                .checkoutDate(bookLoan.getCheckoutDate())
                .dueDate(bookLoan.getDueDate())
                .returnDate(bookLoan.getReturnDate())
                .renewalCount(bookLoan.getRenewalCount())
                .maxRenewalAllowed(bookLoan.getMaxRenewalAllowed())
                .notes(bookLoan.getNotes())
                .remainingDays(ChronoUnit.DAYS.between(LocalDate.now(), bookLoan.getDueDate()))
                .isOverdue(bookLoan.getIsOverdue())
                .overdueDays(bookLoan.getOverdueDays())
                .createdAt(bookLoan.getCreatedAt())
                .updatedAt(bookLoan.getUpdatedAt());

        User user = bookLoan.getUser();
        if (user != null) {
            builder.userId(user.getId())
                    .userName(user.getFullName())
                    .userEmail(user.getEmail());
        }

        Book book = bookLoan.getBook();
        if (book != null) {
            builder.bookId(book.getId())
                    .bookTitle(book.getTitle())
                    .bookAuthor(book.getAuthor())
                    .bookISBN(book.getIsbn())
                    .bookCoverPageURL(book.getCoverImgUrl());
        }

        return builder.build();
    }

    public BookLoan toEntity(BookLoanDTO dto, User user, Book book) {
        if (dto == null) {
            return null;
        }

        return BookLoan.builder()
                .user(user)
                .book(book)
                .bookLoanType(dto.getBookLoanType())
                .bookLoanStatus(dto.getBookLoanStatus())
                .checkoutDate(dto.getCheckoutDate())
                .dueDate(dto.getDueDate())
                .returnDate(dto.getReturnDate())
                .renewalCount(dto.getRenewalCount())
                .maxRenewalAllowed(dto.getMaxRenewalAllowed())
                .notes(dto.getNotes())
                .isOverdue(dto.getIsOverdue())
                .overdueDays(dto.getOverdueDays())
                .build();
    }

    public List<BookLoanDTO> toDTOList(List<BookLoan> bookLoans) {
        if (bookLoans == null) {
            return List.of();
        }
        return bookLoans.stream().map(this::toDTO).toList();
    }

    public List<BookLoan> toEntityList(List<BookLoanDTO> dtos, User user, Book book) {
        if (dtos == null) {
            return List.of();
        }
        return dtos.stream().map(dto -> toEntity(dto, user, book)).toList();
    }

    public void updateEntityFromDTO(BookLoanDTO dto, BookLoan bookLoan, User user, Book book) {
        if (dto == null || bookLoan == null) {
            return;
        }

        if (user != null) {
            bookLoan.setUser(user);
        }
        if (book != null) {
            bookLoan.setBook(book);
        }
        if (dto.getBookLoanType() != null) {
            bookLoan.setBookLoanType(dto.getBookLoanType());
        }
        if (dto.getBookLoanStatus() != null) {
            bookLoan.setBookLoanStatus(dto.getBookLoanStatus());
        }
        if (dto.getCheckoutDate() != null) {
            bookLoan.setCheckoutDate(dto.getCheckoutDate());
        }
        if (dto.getDueDate() != null) {
            bookLoan.setDueDate(dto.getDueDate());
        }
        if (dto.getReturnDate() != null) {
            bookLoan.setReturnDate(dto.getReturnDate());
        }
        if (dto.getRenewalCount() != null) {
            bookLoan.setRenewalCount(dto.getRenewalCount());
        }
        if (dto.getMaxRenewalAllowed() != null) {
            bookLoan.setMaxRenewalAllowed(dto.getMaxRenewalAllowed());
        }
        if (dto.getNotes() != null) {
            bookLoan.setNotes(dto.getNotes());
        }
        if (dto.getIsOverdue() != null) {
            bookLoan.setIsOverdue(dto.getIsOverdue());
        }
        if (dto.getOverdueDays() != null) {
            bookLoan.setOverdueDays(dto.getOverdueDays());
        }
    }
}

