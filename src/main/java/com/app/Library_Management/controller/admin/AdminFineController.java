package com.app.Library_Management.controller.admin;

import com.app.Library_Management.domain.FineStatus;
import com.app.Library_Management.domain.FineType;
import com.app.Library_Management.exception.FineException;
import com.app.Library_Management.exception.UserNotFoundException;
import com.app.Library_Management.payload.dto.FineDTO;
import com.app.Library_Management.payload.request.CreateFineRequest;
import com.app.Library_Management.payload.request.WaiveFineRequest;
import com.app.Library_Management.payload.response.PageResponse;
import com.app.Library_Management.service.FineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/fines")
public class AdminFineController {
    private final FineService fineService;

    @PostMapping
    public ResponseEntity<?> createFine(@Valid @RequestBody CreateFineRequest createFineRequest) throws FineException {
        return ResponseEntity.ok(fineService.createFine(createFineRequest));
    }

    @PostMapping("/waive")
    public ResponseEntity<?> waiveFine(@Valid @RequestBody WaiveFineRequest waiveFineRequest) throws UserNotFoundException, FineException {
        return ResponseEntity.ok(fineService.waiveFine(waiveFineRequest));
    }

    @GetMapping
    public ResponseEntity<PageResponse<FineDTO>> getAllFines(
            @RequestParam(required = false) FineStatus status,
            @RequestParam(required = false) FineType type,
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(fineService.getAllFines(page, size, status, type, userId));
    }
}

