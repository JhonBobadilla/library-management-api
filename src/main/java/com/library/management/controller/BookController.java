package com.library.management.controller;

import com.library.management.dto.*;
import com.library.management.service.BookService;
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
@RequestMapping("/api/v1/books")
@Tag(name = "Libros", description = "Operaciones para administrar libros y consultar sus ejemplares f\u00edsicos.")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo libro con ejemplares",
               description = "Registra un nuevo libro y genera autom\u00e1ticamente la cantidad de ejemplares f\u00edsicos indicada.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Libro creado exitosamente con sus ejemplares"),
            @ApiResponse(responseCode = "400", description = "Error de validaci\u00f3n en los datos de entrada"),
            @ApiResponse(responseCode = "409", description = "El ISBN ya est\u00e1 registrado")
    })
    public ResponseEntity<BookResponse> create(@Valid @RequestBody BookCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.create(request));
    }

    @GetMapping
    @Operation(summary = "Listar todos los libros", description = "Obtiene una lista completa de los libros registrados.")
    public ResponseEntity<List<BookResponse>> findAll() {
        return ResponseEntity.ok(bookService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un libro por ID", description = "Busca y retorna un libro por su identificador \u00fanico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Libro encontrado"),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    public ResponseEntity<BookResponse> findById(
            @Parameter(description = "Identificador \u00fanico del libro") @PathVariable Long id) {
        return ResponseEntity.ok(bookService.findById(id));
    }

    @GetMapping("/isbn/{isbn}")
    @Operation(summary = "Obtener un libro por ISBN", description = "Busca y retorna un libro por su c\u00f3digo ISBN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Libro encontrado"),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    public ResponseEntity<BookResponse> findByIsbn(
            @Parameter(description = "C\u00f3digo ISBN del libro") @PathVariable String isbn) {
        return ResponseEntity.ok(bookService.findByIsbn(isbn));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un libro", description = "Actualiza los datos de un libro existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Libro actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error de validaci\u00f3n en los datos de entrada"),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado"),
            @ApiResponse(responseCode = "409", description = "El ISBN ya est\u00e1 registrado")
    })
    public ResponseEntity<BookResponse> update(
            @Parameter(description = "Identificador \u00fanico del libro") @PathVariable Long id,
            @Valid @RequestBody BookUpdateRequest request) {
        return ResponseEntity.ok(bookService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un libro",
               description = "Elimina un libro y sus ejemplares si no tiene pr\u00e9stamos asociados.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Libro eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado"),
            @ApiResponse(responseCode = "409", description = "El libro tiene pr\u00e9stamos o ejemplares prestados y no puede ser eliminado")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Identificador \u00fanico del libro") @PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/isbn/{isbn}/available-copies")
    @Operation(summary = "Consultar ejemplares disponibles por ISBN",
               description = "Retorna la cantidad y el listado de ejemplares f\u00edsicos disponibles para un libro seg\u00fan su ISBN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ejemplares disponibles obtenidos exitosamente"),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    public ResponseEntity<AvailableCopiesResponse> getAvailableCopies(
            @Parameter(description = "C\u00f3digo ISBN del libro") @PathVariable String isbn) {
        return ResponseEntity.ok(bookService.getAvailableCopiesByIsbn(isbn));
    }
}
