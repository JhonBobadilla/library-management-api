package com.library.management.controller;

import com.library.management.dto.LoanCreateRequest;
import com.library.management.dto.LoanResponse;
import com.library.management.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/loans")
@Tag(name = "Loans", description = "Loan management endpoints")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    @Operation(summary = "Register a new loan")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Loan created"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "User or book copy not found"),
            @ApiResponse(responseCode = "409", description = "Business rule violation")
    })
    public ResponseEntity<LoanResponse> create(@Valid @RequestBody LoanCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(loanService.create(request));
    }

    @GetMapping
    @Operation(summary = "List loans with optional filters")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Loans retrieved")
    })
    public ResponseEntity<List<LoanResponse>> findAll(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long bookId) {
        return ResponseEntity.ok(loanService.findAll(userId, bookId));
    }

    @PatchMapping("/{id}/return")
    @Operation(summary = "Return a loaned book copy")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Loan returned"),
            @ApiResponse(responseCode = "404", description = "Loan not found"),
            @ApiResponse(responseCode = "409", description = "Loan already returned")
    })
    public ResponseEntity<LoanResponse> returnBook(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.returnBook(id));
    }
}
