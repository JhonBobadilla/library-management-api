package com.library.management.controller;

import com.library.management.dto.LoanCreateRequest;
import com.library.management.dto.LoanResponse;
import com.library.management.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@Tag(name = "Pr\u00e9stamos", description = "Operaciones para registrar, consultar y devolver pr\u00e9stamos.")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    @Operation(summary = "Registrar un nuevo pr\u00e9stamo",
               description = "Registra un pr\u00e9stamo de un ejemplar a un usuario. "
                       + "El usuario solo puede tener un pr\u00e9stamo abierto a la vez. "
                       + "El ejemplar debe estar disponible. "
                       + "loanDate no puede ser posterior a dueDate.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pr\u00e9stamo registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error de validaci\u00f3n en los datos de entrada"),
            @ApiResponse(responseCode = "404", description = "Usuario o ejemplar no encontrado"),
            @ApiResponse(responseCode = "409", description = "Conflicto: el usuario ya tiene un pr\u00e9stamo abierto o el ejemplar no est\u00e1 disponible")
    })
    public ResponseEntity<LoanResponse> create(@Valid @RequestBody LoanCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(loanService.create(request));
    }

    @GetMapping
    @Operation(summary = "Listar pr\u00e9stamos",
               description = "Obtiene una lista de pr\u00e9stamos. "
                       + "Se pueden filtrar por userId y/o bookId. "
                       + "Sin filtros retorna todos los pr\u00e9stamos registrados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de pr\u00e9stamos obtenida exitosamente")
    })
    public ResponseEntity<List<LoanResponse>> findAll(
            @Parameter(description = "Identificador del usuario (opcional)") @RequestParam(required = false) Long userId,
            @Parameter(description = "Identificador del libro (opcional)") @RequestParam(required = false) Long bookId) {
        return ResponseEntity.ok(loanService.findAll(userId, bookId));
    }

    @PatchMapping("/{id}/return")
    @Operation(summary = "Devolver un ejemplar prestado",
               description = "Marca el pr\u00e9stamo como devuelto, registra la fecha y hora de devoluci\u00f3n "
                       + "y cambia el estado del ejemplar f\u00edsico a disponible.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pr\u00e9stamo devuelto exitosamente"),
            @ApiResponse(responseCode = "404", description = "Pr\u00e9stamo no encontrado"),
            @ApiResponse(responseCode = "409", description = "El pr\u00e9stamo ya fue devuelto anteriormente")
    })
    public ResponseEntity<LoanResponse> returnBook(
            @Parameter(description = "Identificador del pr\u00e9stamo") @PathVariable Long id) {
        return ResponseEntity.ok(loanService.returnBook(id));
    }
}
