package com.app.Library_Management.service.impl;

import com.app.Library_Management.domain.FineStatus;
import com.app.Library_Management.domain.FineType;
import com.app.Library_Management.exception.FineException;
import com.app.Library_Management.exception.UserNotFoundException;
import com.app.Library_Management.mapper.FineMapper;
import com.app.Library_Management.model.BookLoan;
import com.app.Library_Management.model.Fine;
import com.app.Library_Management.model.User;
import com.app.Library_Management.payload.dto.FineDTO;
import com.app.Library_Management.payload.request.CreateFineRequest;
import com.app.Library_Management.payload.request.WaiveFineRequest;
import com.app.Library_Management.payload.response.FinePayResponse;
import com.app.Library_Management.payload.response.PageResponse;
import com.app.Library_Management.repository.BookLoanRepository;
import com.app.Library_Management.repository.FineRepository;
import com.app.Library_Management.service.FineService;
import com.app.Library_Management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FineServiceImp implements FineService {
    private final FineRepository  fineRepository;
    private final BookLoanRepository bookLoanRepository;
    private final FineMapper  fineMapper;
    private final UserService  userService;
    @Override
    public FineDTO createFine(CreateFineRequest request) throws FineException {

        BookLoan bookLoan = bookLoanRepository.findById(request.getBookLoanId()).orElseThrow(() -> new FineException("Book loan not found with id: " + request.getBookLoanId()));
        Fine fine = Fine.builder()
                .bookLoan(bookLoan)
                .user(bookLoan.getUser())
                .fineType(request.getFineType())
                .amount(request.getAmount())
                .fineStatus(FineStatus.PENDING)
                .reason(request.getReason())
                .notes(request.getNotes())
                .build();
        Fine savedFine = fineRepository.save(fine);
        return fineMapper.toDTO(savedFine);
    }

    @Override
    public FinePayResponse payFine(Long fineId, Long amount) throws FineException, UserNotFoundException {
        Fine fine = fineRepository.findById(fineId).orElseThrow(() -> new FineException("Fine not found with id: " + fineId));

        if (fine.getFineStatus().equals(FineStatus.PAID)) {
            throw new FineException("Fine is already paid");
        }
        if (fine.getFineStatus().equals(FineStatus.WAIVED)) {
            throw new FineException("Fine is already waived");
        }

        User currentUser = userService.getCurrentUser();
        
        if (amount == null || amount <= 0) {
            throw new FineException("Amount must be greater than 0");
        }

        if (amount < fine.getAmount()) {
            throw new FineException("Insufficient amount. Fine amount is: " + fine.getAmount());
        }

        fine.setFineStatus(FineStatus.PAID);
        fine.setPaidAt(java.time.LocalDate.now());
        fine.setProcessedBy(currentUser);

        Fine savedFine = fineRepository.save(fine);

        return FinePayResponse.builder()
                .fineId(savedFine.getId())
                .userId(savedFine.getUser().getId())
                .bookLoanId(savedFine.getBookLoan().getId())
                .amount(savedFine.getAmount())
                .reason(savedFine.getReason())
                .transactionId(savedFine.getTransactionId())
                .paidAt(savedFine.getPaidAt())
                .message("Fine paid successfully")
                .status(true)
                .build();
    }

    @Override
    public void markFineAsPaid(Long fineId, Long amount) throws FineException {
        Fine fine = fineRepository.findById(fineId).orElseThrow(() -> new FineException("Fine not found with id: " + fineId));
        
        if (fine.getFineStatus().equals(FineStatus.PAID)) {
            throw new FineException("Fine is already paid");
        }
        if (fine.getFineStatus().equals(FineStatus.WAIVED)) {
            throw new FineException("Fine is already waived");
        }

        fine.setFineStatus(FineStatus.PAID);
        fine.setPaidAt(java.time.LocalDate.now());
        fineRepository.save(fine);
    }

    @Override
    public FineDTO waiveFine(WaiveFineRequest request) throws FineException, UserNotFoundException {
        Fine fine = fineRepository.findById(request.getFineId()).orElseThrow(() -> new FineException("Fine not found with id: " + request.getFineId()));

        if (fine.getFineStatus().equals(FineStatus.PAID)) {
            throw new FineException("Cannot waive a fine that is already paid");
        }
        if (fine.getFineStatus().equals(FineStatus.WAIVED)) {
            throw new FineException("Fine is already waived");
        }

        User currentUser = userService.getCurrentUser();

        fine.setFineStatus(FineStatus.WAIVED);
        fine.setWaivedBy(currentUser);
        fine.setWaivedAt(java.time.LocalDate.now());
        fine.setNotes(request.getNotes() != null ? request.getNotes() : "Waived: " + request.getReason());

        Fine savedFine = fineRepository.save(fine);
        return fineMapper.toDTO(savedFine);
    }

    @Override
    public List<FineDTO> getMyFines(FineStatus status, FineType type) throws UserNotFoundException {
        User currentUser = userService.getCurrentUser();
        List<Fine> fines = fineRepository.findAllWithFilters(currentUser.getId(), status, type, org.springframework.data.domain.Pageable.unpaged()).getContent();
        return fineMapper.toDTOList(fines);
    }

    @Override
    public PageResponse<FineDTO> getAllFines(int page, int size, FineStatus status, FineType type, Long userId) {
        Pageable pageable = PageRequest.of(page, size, org.springframework.data.domain.Sort.by("createdAt").descending());
        Page<Fine> finesPage = fineRepository.findAllWithFilters(userId, status, type, pageable);

        return PageResponse.<FineDTO>builder()
                .content(fineMapper.toDTOList(finesPage.getContent()))
                .pageNumber(finesPage.getNumber())
                .pageSize(finesPage.getSize())
                .totalElements(finesPage.getTotalElements())
                .totalPages(finesPage.getTotalPages())
                .last(finesPage.isLast())
                .first(finesPage.isFirst())
                .empty(finesPage.isEmpty())
                .build();
    }
}
