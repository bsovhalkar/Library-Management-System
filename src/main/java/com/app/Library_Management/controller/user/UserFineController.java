package com.app.Library_Management.controller.user;

import com.app.Library_Management.domain.FineStatus;
import com.app.Library_Management.domain.FineType;
import com.app.Library_Management.exception.FineException;
import com.app.Library_Management.exception.UserNotFoundException;
import com.app.Library_Management.payload.dto.FineDTO;
import com.app.Library_Management.service.FineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fines")
public class UserFineController {
    private final FineService fineService;

    @PostMapping("/{fineId}/pay")
    public ResponseEntity<?> payFine(@Valid @RequestBody Long amount, @PathVariable Long fineId) throws FineException, UserNotFoundException {
        return ResponseEntity.ok(fineService.payFine(fineId, amount));
    }

    @GetMapping("/my")
    public ResponseEntity<List<FineDTO>> getMyFines(
            @RequestParam(required = false) FineStatus status,
            @RequestParam(required = false) FineType type
    ) throws UserNotFoundException {
        return ResponseEntity.ok(fineService.getMyFines(status, type));
    }
}

