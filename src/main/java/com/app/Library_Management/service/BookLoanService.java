package com.app.Library_Management.service;

import com.app.Library_Management.domain.BookLoanStatus;
import com.app.Library_Management.exception.BookNotFoundException;
import com.app.Library_Management.exception.SubscriptionException;
import com.app.Library_Management.exception.UserNotFoundException;
import com.app.Library_Management.model.BookLoan;
import com.app.Library_Management.payload.dto.BookLoanDTO;
import com.app.Library_Management.payload.request.BookLoanSearchRequest;
import com.app.Library_Management.payload.request.CheckInRequest;
import com.app.Library_Management.payload.request.CheckoutRequest;
import com.app.Library_Management.payload.request.RenewalRequest;
import com.app.Library_Management.payload.response.PageResponse;
import org.springframework.data.domain.PageRequest;

public interface BookLoanService {
    BookLoanDTO checkoutBook(CheckoutRequest request) throws Exception;

    BookLoanDTO checkInBook(CheckInRequest request) throws Exception;

    BookLoanDTO checkoutBookForUser(Long userId,CheckoutRequest request) throws Exception;

    BookLoanDTO renewCheckout(RenewalRequest request) throws Exception;
    PageResponse<BookLoanDTO> getMyBookLoans(BookLoanStatus status,int page,int pageSize) throws UserNotFoundException;

    // for admin
    PageResponse<BookLoanDTO> getBookLoans(BookLoanSearchRequest request);


    Integer updateOverdueBookLoan();
}
