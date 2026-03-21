package com.app.Library_Management.controller.user;

import com.app.Library_Management.domain.BookLoanStatus;
import com.app.Library_Management.payload.dto.BookLoanDTO;
import com.app.Library_Management.payload.request.CheckInRequest;
import com.app.Library_Management.payload.request.CheckoutRequest;
import com.app.Library_Management.payload.request.RenewalRequest;
import com.app.Library_Management.payload.response.PageResponse;
import com.app.Library_Management.service.BookLoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/book-loan")
@RequiredArgsConstructor
public class UserBookLoanController {
    private final BookLoanService bookLoanService;

    @PostMapping("/checkout")
    public ResponseEntity<BookLoanDTO> checkoutBook(@RequestBody CheckoutRequest request) throws Exception {
        BookLoanDTO bookLoanDTO = bookLoanService.checkoutBook(request);
        return new ResponseEntity<>(bookLoanDTO, HttpStatus.CREATED );
    }

    @PostMapping("/checkin")
    public ResponseEntity<BookLoanDTO> checkInBook(@RequestBody CheckInRequest request) throws Exception {
        BookLoanDTO bookLoanDTO = bookLoanService.checkInBook(request);
        return ResponseEntity.ok(bookLoanDTO);
    }

    @PostMapping("/renew")
    public ResponseEntity<BookLoanDTO> renewCheckout(@RequestBody RenewalRequest request) throws Exception {
        BookLoanDTO bookLoanDTO = bookLoanService.renewCheckout(request);
        return ResponseEntity.ok(bookLoanDTO);
    }

    @GetMapping("/my-loans")
    public ResponseEntity<PageResponse<BookLoanDTO>> getMyBookLoans(
            @RequestParam(required = false) BookLoanStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) throws Exception {
        PageResponse<BookLoanDTO> response = bookLoanService.getMyBookLoans(status, page, pageSize);
        return ResponseEntity.ok(response);
    }
}

