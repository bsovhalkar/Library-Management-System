package com.app.Library_Management.service.impl;

import com.app.Library_Management.domain.BookLoanStatus;
import com.app.Library_Management.domain.BookLoanType;
import com.app.Library_Management.exception.BookNotFoundException;
import com.app.Library_Management.exception.SubscriptionException;
import com.app.Library_Management.exception.UserNotFoundException;
import com.app.Library_Management.mapper.BookLoanMapper;
import com.app.Library_Management.model.Book;
import com.app.Library_Management.model.BookLoan;
import com.app.Library_Management.model.User;
import com.app.Library_Management.payload.dto.BookLoanDTO;
import com.app.Library_Management.payload.dto.SubscriptionDTO;
import com.app.Library_Management.payload.request.BookLoanSearchRequest;
import com.app.Library_Management.payload.request.CheckInRequest;
import com.app.Library_Management.payload.request.CheckoutRequest;
import com.app.Library_Management.payload.request.RenewalRequest;
import com.app.Library_Management.payload.response.PageResponse;
import com.app.Library_Management.repository.BookLoanRepository;
import com.app.Library_Management.repository.BookRepository;
import com.app.Library_Management.repository.UserRepository;
import com.app.Library_Management.service.BookLoanService;
import com.app.Library_Management.service.SubscriptionService;
import com.app.Library_Management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookLoanServiceImp implements BookLoanService {
    private final BookLoanRepository bookLoanRepository;
    private final UserService  userService;
    private final SubscriptionService subscriptionService;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BookLoanMapper bookLoanMapper;
    @Override
    public BookLoanDTO checkoutBook(CheckoutRequest request) throws Exception {
        User currentUser = userService.getCurrentUser();
        return checkoutBookForUser(currentUser.getId(),request);

    }

    @Override
    public BookLoanDTO checkInBook(CheckInRequest request) throws Exception {

        BookLoan bookLoan = bookLoanRepository.findById(request.getBookLoanId())
                .orElseThrow(() -> new Exception("Book loan not found with id: " + request.getBookLoanId()));

        if (!bookLoan.isActive()) {
            throw new Exception("Book loan with id: " + request.getBookLoanId() + " is not active and cannot be checked in");
        }

        LocalDate today = LocalDate.now();

        // ✅ Set return date
        bookLoan.setReturnDate(today);

        // ✅ Use request status OR default
        BookLoanStatus status = request.getBookLoanStatus() != null
                ? request.getBookLoanStatus()
                : BookLoanStatus.RETURNED;

        bookLoan.setBookLoanStatus(status);

        // ✅ FIX: Proper overdue calculation (CRITICAL)
        if (today.isAfter(bookLoan.getDueDate())) {
            long days = ChronoUnit.DAYS.between(bookLoan.getDueDate(), today);
            bookLoan.setIsOverdue(true);
            bookLoan.setOverdueDays(days);
        } else {
            bookLoan.setIsOverdue(false);
            bookLoan.setOverdueDays(0L);
        }

        // ✅ Notes handling
        bookLoan.setNotes(
                request.getNotes() == null
                        ? "Book returned by User"
                        : request.getNotes()
        );

        // ✅ Update book copies ONLY when returned
        if (status == BookLoanStatus.RETURNED) {
            Book book = bookLoan.getBook();
            book.setAvailableCopies(book.getAvailableCopies() + 1);
            bookRepository.save(book);
        }

        BookLoan savedBookLoan = bookLoanRepository.save(bookLoan);

        return bookLoanMapper.toDTO(savedBookLoan);
    }

    @Override
    public BookLoanDTO checkoutBookForUser(Long userId, CheckoutRequest request) throws Exception {
        User currentUser = userService.getUserById(userId);
        SubscriptionDTO subscriptionDTO = subscriptionService.getUsersActiveSubscriptions(userId);
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + request.getBookId()));
        if(!book.getActive()){
            throw new Exception("Book is not Active");
        }

        if(book.getAvailableCopies() <= 0){
            throw new Exception("No available copies for book id: " + request.getBookId());
        }


        if(bookLoanRepository.hasActiveCheckout(userId,book.getId())){
            throw new Exception("User already has an active checkout for book id: " + request.getBookId());
        }

        Long activeCheckouts = bookLoanRepository.countActiveBookLoansByUserId(userId);
        if(activeCheckouts >= subscriptionDTO.getMaxBooksAllowed()){
            throw new Exception("User has reached the maximum number of active checkouts allowed by their subscription plan");
        }

        Long overdueCount = bookLoanRepository.countOverdueBookLoansByUser(userId);

        if(overdueCount>0){
            throw new Exception("User has overdue book loans and cannot checkout new books until they are returned or renewed");
        }

        BookLoan bookLoan = BookLoan.builder()
                .user(currentUser)
                .book(book)
                .bookLoanType(BookLoanType.CHECKOUT)
                .bookLoanStatus(BookLoanStatus.CHECKED_OUT)
                .checkoutDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(request.getCheckoutDays()))
                .renewalCount(0L)
                .maxRenewalAllowed(2L)
                .notes(request.getNotes())
                .isOverdue(false)
                .overdueDays(0L)

                .build();
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);
        BookLoan saved = bookLoanRepository.save(bookLoan);
        return bookLoanMapper.toDTO(saved);
    }

    @Override
    public BookLoanDTO renewCheckout(RenewalRequest request) throws Exception {
        BookLoan bookLoan = bookLoanRepository.findById(request.getBookLoanId())
                .orElseThrow(() -> new Exception("Book loan not found with id: " + request.getBookLoanId()));

        if(!bookLoan.canRenew()){
            throw new Exception("Book loan with id: " + request.getBookLoanId() + " cannot be renewed. It may have already been renewed the maximum number of times or is not active.");
        }

        bookLoan.setDueDate(bookLoan.getDueDate().plusDays(request.getExtensionDays()));
        bookLoan.setRenewalCount(bookLoan.getRenewalCount() + 1);
        bookLoan.setNotes(request.getNotes()==null?"Book renewed by User":request.getNotes());
        BookLoan updatedBookLoan = bookLoanRepository.save(bookLoan);
        return bookLoanMapper.toDTO(updatedBookLoan);


    }

    @Override
    public PageResponse<BookLoanDTO> getMyBookLoans(BookLoanStatus status, int page, int pageSize) throws UserNotFoundException {
        User currentUser = userService.getCurrentUser();
        Page<BookLoan> bookLoanPage;
        if (status != null) {
            Pageable pageable = PageRequest.of(page, pageSize, Sort.by("dueDate").ascending());
            bookLoanPage = bookLoanRepository.findByUserIdAndBookLoanStatus(currentUser.getId(), status, pageable);

        } else {
            Pageable pageable = PageRequest.of(page, pageSize,Sort.by("createdAt").descending());
            bookLoanPage = bookLoanRepository.findByUserId(currentUser.getId(), pageable);
        }

        return convertToPageResponse(bookLoanPage);
    }

    private Pageable createPageRequest(int pageNumber, int pageSize,String sortBy, String sortDirection) {

        pageSize = Math.max(pageSize, 100);
        pageSize =Math.max(pageSize, 1);
        Sort sort = Sort.by(sortBy);
        if(sortDirection!=null){
        sort = sortDirection.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        }
        return PageRequest.of(pageNumber, pageSize, sort);

    }

    private PageResponse<BookLoanDTO> convertToPageResponse(Page<BookLoan> bookLoanPage) {
        List<BookLoanDTO> bookLoanDTOList = bookLoanPage.getContent().stream()
                .map(bookLoanMapper::toDTO)
                .collect(Collectors.toList());
        return new PageResponse<>(
                bookLoanDTOList,
                bookLoanPage.getNumber(),
                bookLoanPage.getSize(),
                bookLoanPage.getTotalElements(),
                bookLoanPage.getTotalPages(),
                bookLoanPage.isLast(),
                bookLoanPage.isFirst(),
                bookLoanPage.isEmpty()
        );
    }

    @Override
    public PageResponse<BookLoanDTO> getBookLoans(BookLoanSearchRequest request) {
        Pageable pageable = createPageRequest(request.getPage(), request.getPageSize(), request.getSortBy(), request.getSortDirection());
        Page<BookLoan> bookLoanPage;
        if(Boolean.TRUE.equals(request.getOverdueOnly())){
            bookLoanPage = bookLoanRepository.findOverdueBookLoansPage(LocalDate.now(),pageable);
        }
        else if(Boolean.TRUE.equals(request.getUnpaidOnly())){
            bookLoanPage = bookLoanRepository.findUnpaidBookLoans(pageable);
        }
        else if(request.getUserId() != null){
            bookLoanPage = bookLoanRepository.findByUserId(request.getUserId(),pageable);
        }
        else if(request.getBookId() != null){
            bookLoanPage = bookLoanRepository.findByBookId(request.getBookId(),pageable);
        }
        else if(request.getBookLoanStatus() != null){
            bookLoanPage = bookLoanRepository.findByBookLoanStatus(request.getBookLoanStatus(),pageable);
        }
        else if(request.getStartDate() != null && request.getEndDate() != null){
            bookLoanPage = bookLoanRepository.findBookLoansByDateRange(request.getStartDate(),request.getEndDate(),pageable);
        }
        else{
            bookLoanPage = bookLoanRepository.findAll(pageable);
        }

        return convertToPageResponse(bookLoanPage);
    }

    @Override
    public Integer updateOverdueBookLoan() {
        List<BookLoan> overdueLoans = bookLoanRepository.findOverdueBookLoans(LocalDate.now());
        int count = 0;
        for (BookLoan loan : overdueLoans) {
            if (loan.getBookLoanStatus() == BookLoanStatus.CHECKED_OUT) {
                loan.setBookLoanStatus(BookLoanStatus.OVERDUE);
                loan.setIsOverdue(true);
                loan.setOverdueDays(java.time.temporal.ChronoUnit.DAYS.between(loan.getDueDate(), LocalDate.now()));
                bookLoanRepository.save(loan);
                count++;
            }
        }
        return count;
    }
}
