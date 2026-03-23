package com.app.Library_Management.service;

import com.app.Library_Management.domain.FineStatus;
import com.app.Library_Management.domain.FineType;
import com.app.Library_Management.exception.FineException;
import com.app.Library_Management.exception.UserNotFoundException;
import com.app.Library_Management.payload.dto.FineDTO;
import com.app.Library_Management.payload.request.CreateFineRequest;
import com.app.Library_Management.payload.request.WaiveFineRequest;
import com.app.Library_Management.payload.response.FinePayResponse;
import com.app.Library_Management.payload.response.PageResponse;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface FineService {
    FineDTO createFine(CreateFineRequest request) throws FineException;
    FinePayResponse payFine(Long fineId, Long amount) throws FineException, UserNotFoundException;
    void markFineAsPaid(Long fineId, Long amount) throws FineException;

    FineDTO waiveFine(WaiveFineRequest request) throws FineException, UserNotFoundException;

    List<FineDTO> getMyFines(FineStatus status, FineType type) throws UserNotFoundException;

    PageResponse<FineDTO> getAllFines(int page, int size, FineStatus status, FineType type,Long userId);
}
