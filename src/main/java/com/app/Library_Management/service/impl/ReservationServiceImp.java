package com.app.Library_Management.service.impl;

import com.app.Library_Management.domain.BookLoanStatus;
import com.app.Library_Management.domain.ReservationStatus;
import com.app.Library_Management.domain.UserRole;
import com.app.Library_Management.exception.ReservationException;
import com.app.Library_Management.exception.UserNotFoundException;
import com.app.Library_Management.mapper.ReservationMapper;
import com.app.Library_Management.model.Book;
import com.app.Library_Management.model.Reservation;
import com.app.Library_Management.model.User;
import com.app.Library_Management.payload.dto.ReservationDTO;
import com.app.Library_Management.payload.request.CheckoutRequest;
import com.app.Library_Management.payload.request.ReservationRequest;
import com.app.Library_Management.payload.request.ReservationSearchRequest;
import com.app.Library_Management.payload.response.PageResponse;
import com.app.Library_Management.repository.BookLoanRepository;
import com.app.Library_Management.repository.BookRepository;
import com.app.Library_Management.repository.ReservationRepository;
import com.app.Library_Management.repository.impl.ReservationRepositoryCustom;
import com.app.Library_Management.service.BookLoanService;
import com.app.Library_Management.service.ReservationService;
import com.app.Library_Management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImp implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final BookLoanRepository  bookLoanRepository;
    private final BookLoanService bookLoanService;
    private final UserService  userService;
    private final BookRepository bookRepository;
    private final ReservationMapper reservationMapper;
    private final ReservationRepositoryCustom reservationRepositoryCustom;

    @Override
    public ReservationDTO createReservation(ReservationRequest request) throws UserNotFoundException, ReservationException {
        User currentUser = userService.getCurrentUser();
        return createReservationForUser(currentUser.getId(), request);
    }

    @Override
    public ReservationDTO createReservationForUser(Long userId, ReservationRequest request) throws ReservationException, UserNotFoundException {
        boolean alreadyHasLoan = bookLoanRepository.existsByUserIdAndBookIdAndBookLoanStatus(userId,request.getBookId(), BookLoanStatus.CHECKED_OUT);
        if (alreadyHasLoan) {
            throw new ReservationException("Book has already been checked out");
        }

        User currentUser = userService.getCurrentUser();

        Book userWantBook = bookRepository.findById(request.getBookId()).orElseThrow(() -> new ReservationException("Book not found"));

        if(reservationRepository.existsByUserIdAndBookIdAndReservationStatusIn(userId,userWantBook.getId(), List.of(ReservationStatus.PENDING, ReservationStatus.AVAILABLE))){
            throw new ReservationException("You already have an active reservation for this book");
        }

        if(userWantBook.getAvailableCopies() > 0){
            throw new ReservationException("Book is currently available, no need to reserve");
        }

        long activeReservations = reservationRepository.countByUserIdAndReservationStatusIn(
                userId,
                List.of(ReservationStatus.PENDING, ReservationStatus.AVAILABLE)
        );

        if(activeReservations > 5){
            throw new ReservationException("You have reached the maximum number of active reservations (5)");
        }

        Reservation reservation = new Reservation();
        reservation.setUser(currentUser);
        reservation.setBook(userWantBook);
        reservation.setReservationStatus(ReservationStatus.PENDING);
        reservation.setReservedAt(LocalDateTime.now());
        reservation.setNotificationSent(false);
        reservation.setNotes(request.getNotes());
        long pendingReservationsForBook = reservationRepository.countByBookIdAndReservationStatus(
                request.getBookId(),
                ReservationStatus.PENDING
        );
        reservation.setQueuePosition(pendingReservationsForBook + 1);

        Reservation savedReservation = reservationRepository.save(reservation);
        return reservationMapper.toDTO(savedReservation);
    }

    @Override
    public ReservationDTO cancelReservation(Long reservationId) throws ReservationException, UserNotFoundException {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new ReservationException("Reservation not found"));
        User currentUser = userService.getCurrentUser();
        if(!reservation.getUser().getId().equals(currentUser.getId()) && !currentUser.getRole().equals(UserRole.ROLE_ADMIN.toString())){
            throw new ReservationException("You can't cancel your reservation");
        }

        if(!reservation.canBeCancelled()) {
            throw new ReservationException("This reservation cannot be cancelled");
        }

        reservation.setReservationStatus(ReservationStatus.CANCELLED);
        reservation.setCancelledAt(LocalDateTime.now());

        Reservation updatedReservation = reservationRepository.save(reservation);
        return reservationMapper.toDTO(updatedReservation);
    }

    @Override
    public ReservationDTO getReservationById(Long reservationId) throws ReservationException {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationException("Reservation not found"));
        return reservationMapper.toDTO(reservation);
    }

    @Override
    public ReservationDTO fulfillReservation(Long reservationId) throws Exception {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationException("Reservation not found"));

        if(reservation.getBook().getAvailableCopies() <= 0) {
            throw new ReservationException("Reservation can't to be fulfilled (Book available count has been empty)");
        }

        reservation.setReservationStatus(ReservationStatus.FULFILLED);
        reservation.setFulfilledAt(LocalDateTime.now());

        Reservation updatedReservation = reservationRepository.save(reservation);

        CheckoutRequest reservationCheckoutRequest = new CheckoutRequest();
        reservationCheckoutRequest.setBookId(reservation.getBook().getId());
        reservationCheckoutRequest.setNotes("Auto checkout from reservation fulfillment");
        bookLoanService.checkoutBookForUser(reservation.getUser().getId(), reservationCheckoutRequest);

        return reservationMapper.toDTO(updatedReservation);
    }

    @Override
    public PageResponse<ReservationDTO> getMyReservations(ReservationSearchRequest request) throws UserNotFoundException {
        User currentUser = userService.getCurrentUser();

        Sort.Direction direction = request.getSortDirection().equalsIgnoreCase("ASC")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(request.getPage(), request.getPageSize(), Sort.by(direction, request.getSortBy()));

        Page<Reservation> reservationPage = reservationRepository.findByUserId(currentUser.getId(), pageable);

        PageResponse<ReservationDTO> pageResponse = new PageResponse<>();
        pageResponse.setContent(reservationPage.getContent().stream().map(reservationMapper::toDTO).toList());
        pageResponse.setPageNumber(reservationPage.getNumber());
        pageResponse.setPageSize(reservationPage.getSize());
        pageResponse.setTotalElements(reservationPage.getTotalElements());
        pageResponse.setTotalPages(reservationPage.getTotalPages());
        pageResponse.setLast(reservationPage.isLast());
        pageResponse.setFirst(reservationPage.isFirst());
        pageResponse.setEmpty(reservationPage.isEmpty());

        return pageResponse;
    }

    @Override
    public PageResponse<ReservationDTO> searchReservations(ReservationSearchRequest request) {
        Sort.Direction direction = request.getSortDirection().equalsIgnoreCase("ASC")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(request.getPage(), request.getPageSize(), Sort.by(direction, request.getSortBy()));

        Page<Reservation> reservationPage = reservationRepositoryCustom.searchReservationsWithFilters(request, pageable);

        PageResponse<ReservationDTO> pageResponse = new PageResponse<>();
        pageResponse.setContent(reservationPage.getContent().stream().map(reservationMapper::toDTO).toList());
        pageResponse.setPageNumber(reservationPage.getNumber());
        pageResponse.setPageSize(reservationPage.getSize());
        pageResponse.setTotalElements(reservationPage.getTotalElements());
        pageResponse.setTotalPages(reservationPage.getTotalPages());
        pageResponse.setLast(reservationPage.isLast());
        pageResponse.setFirst(reservationPage.isFirst());
        pageResponse.setEmpty(reservationPage.isEmpty());
        
        return pageResponse;
    }
}
