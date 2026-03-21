package com.app.Library_Management.controller.admin;

import com.app.Library_Management.payload.dto.BookLoanDTO;
import com.app.Library_Management.payload.request.BookLoanSearchRequest;
import com.app.Library_Management.payload.request.CheckoutRequest;
import com.app.Library_Management.payload.response.ApiResponse;
import com.app.Library_Management.payload.response.PageResponse;
import com.app.Library_Management.service.BookLoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/book-loan")
@RequiredArgsConstructor
public class AdminBookLoanController {
    private final BookLoanService bookLoanService;

    @PostMapping("/checkout/{userId}")
    public ResponseEntity<BookLoanDTO> checkoutBookForUser(
            @PathVariable Long userId,
            @RequestBody CheckoutRequest request) throws Exception {
        BookLoanDTO bookLoanDTO = bookLoanService.checkoutBookForUser(userId, request);
        return ResponseEntity.ok(bookLoanDTO);
    }

    @PostMapping("/search")
    public ResponseEntity<PageResponse<BookLoanDTO>> getBookLoans(@RequestBody BookLoanSearchRequest request) {
        PageResponse<BookLoanDTO> response = bookLoanService.getBookLoans(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/update-overdue")
    public ResponseEntity<ApiResponse> updateOverdueBookLoan() {
        Integer count = bookLoanService.updateOverdueBookLoan();
        return ResponseEntity.ok(new ApiResponse( count + " overdue book loans updated",true));

    }
}

