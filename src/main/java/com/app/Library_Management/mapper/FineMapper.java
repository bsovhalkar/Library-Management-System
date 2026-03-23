package com.app.Library_Management.mapper;

import com.app.Library_Management.model.BookLoan;
import com.app.Library_Management.model.Fine;
import com.app.Library_Management.model.User;
import com.app.Library_Management.payload.dto.FineDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FineMapper {

    public FineDTO toDTO(Fine fine) {
        if (fine == null) {
            return null;
        }

        FineDTO.FineDTOBuilder builder = FineDTO.builder()
                .id(fine.getId())
                .fineType(fine.getFineType())
                .amount(fine.getAmount())
                .fineStatus(fine.getFineStatus())
                .reason(fine.getReason())
                .notes(fine.getNotes())
                .waivedAt(fine.getWaivedAt())
                .paidAt(fine.getPaidAt())
                .transactionId(fine.getTransactionId())
                .createdAt(fine.getCreatedAt())
                .updatedAt(fine.getUpdatedAt());

        User user = fine.getUser();
        if (user != null) {
            builder.userId(user.getId())
                    .userName(user.getFullName())
                    .userEmail(user.getEmail());
        }

        BookLoan bookLoan = fine.getBookLoan();
        if (bookLoan != null) {
            builder.bookLoanId(bookLoan.getId());
            if (bookLoan.getBook() != null) {
                builder.bookTitle(bookLoan.getBook().getTitle())
                        .bookIsbn(bookLoan.getBook().getIsbn());
            }
        }

        User waivedByUser = fine.getWaivedBy();
        if (waivedByUser != null) {
            builder.waivedByUserId(waivedByUser.getId())
                    .waivedByUserName(waivedByUser.getFullName());
        }

        User processedByUser = fine.getProcessedBy();
        if (processedByUser != null) {
            builder.processedByUserId(processedByUser.getId());
        }

        Long amountPaid = fine.getPaidAt() != null ? fine.getAmount() : 0L;
        builder.amountPaid(amountPaid);
        builder.amountOutstanding(fine.getAmount() - amountPaid);

        return builder.build();
    }

    public Fine toEntity(FineDTO dto, User user, BookLoan bookLoan) {
        if (dto == null) {
            return null;
        }

        return Fine.builder()
                .user(user)
                .bookLoan(bookLoan)
                .fineType(dto.getFineType())
                .amount(dto.getAmount())
                .fineStatus(dto.getFineStatus())
                .reason(dto.getReason())
                .notes(dto.getNotes())
                .transactionId(dto.getTransactionId())
                .build();
    }

    public List<FineDTO> toDTOList(List<Fine> fines) {
        if (fines == null) {
            return List.of();
        }
        return fines.stream().map(this::toDTO).toList();
    }

    public List<Fine> toEntityList(List<FineDTO> dtos, User user, BookLoan bookLoan) {
        if (dtos == null) {
            return List.of();
        }
        return dtos.stream().map(dto -> toEntity(dto, user, bookLoan)).toList();
    }

    public void updateEntityFromDTO(FineDTO dto, Fine fine, User user, BookLoan bookLoan, User waivedByUser, User processedByUser) {
        if (dto == null || fine == null) {
            return;
        }

        if (user != null) {
            fine.setUser(user);
        }
        if (bookLoan != null) {
            fine.setBookLoan(bookLoan);
        }
        if (dto.getFineType() != null) {
            fine.setFineType(dto.getFineType());
        }
        if (dto.getAmount() != null) {
            fine.setAmount(dto.getAmount());
        }
        if (dto.getFineStatus() != null) {
            fine.setFineStatus(dto.getFineStatus());
        }
        if (dto.getReason() != null) {
            fine.setReason(dto.getReason());
        }
        if (dto.getNotes() != null) {
            fine.setNotes(dto.getNotes());
        }
        if (waivedByUser != null) {
            fine.setWaivedBy(waivedByUser);
        }
        if (dto.getWaivedAt() != null) {
            fine.setWaivedAt(dto.getWaivedAt());
        }
        if (dto.getPaidAt() != null) {
            fine.setPaidAt(dto.getPaidAt());
        }
        if (processedByUser != null) {
            fine.setProcessedBy(processedByUser);
        }
        if (dto.getTransactionId() != null) {
            fine.setTransactionId(dto.getTransactionId());
        }
    }
}

