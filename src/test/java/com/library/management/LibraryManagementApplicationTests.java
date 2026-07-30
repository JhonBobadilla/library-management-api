package com.library.management;

import com.library.management.config.TimeProvider;
import com.library.management.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LibraryManagementApplicationTests {

    @Autowired
    private TestRestTemplate rest;

    @Autowired
    private TimeProvider timeProvider;

    @BeforeEach
    void setUp() {
        timeProvider.setClock(Clock.fixed(Instant.parse("2026-07-29T10:00:00Z"), ZoneOffset.UTC));
    }

    @Test
    void createUserCorrectly() {
        var resp = createUser("jhon", "bobadilla", "jhon@example.com", LocalDate.of(1990, 5, 20));
        assertEquals(HttpStatus.CREATED, resp.getStatusCode());
        assertNotNull(resp.getBody().id());
        assertEquals("jhon", resp.getBody().firstName());
    }

    @Test
    void rejectDuplicateEmail() {
        createUser("first", "user", "dup@example.com", LocalDate.of(1990, 1, 1));
        var dup = new UserCreateRequest("second", "user", "dup@example.com", LocalDate.of(1990, 1, 1));
        var resp = rest.postForEntity("/api/v1/users", dup, ErrorResponse.class);
        assertEquals(HttpStatus.CONFLICT, resp.getStatusCode());
    }

    @Test
    void createBookWithFullDetailsReturns201AndCorrectCounts() {
        var bookReq = new BookCreateRequest(
                "Cien años de soledad",
                "9780307474728",
                "Primera",
                LocalDate.of(1967, 5, 30),
                "Gabriel García Márquez",
                3);
        var resp = rest.postForEntity("/api/v1/books", bookReq, BookResponse.class);
        assertEquals(HttpStatus.CREATED, resp.getStatusCode());
        assertNotNull(resp.getBody().id());
        assertEquals("Cien años de soledad", resp.getBody().title());
        assertEquals("9780307474728", resp.getBody().isbn());
        assertEquals("Primera", resp.getBody().edition());
        assertEquals(LocalDate.of(1967, 5, 30), resp.getBody().publicationDate());
        assertEquals("Gabriel García Márquez", resp.getBody().author());
        assertEquals(3, resp.getBody().totalCopies());
        assertEquals(3, resp.getBody().availableCopies());

        var availResp = rest.getForEntity(
                "/api/v1/books/isbn/9780307474728/available-copies", AvailableCopiesResponse.class);
        assertEquals(3, availResp.getBody().availableCopies());
        assertEquals(3, availResp.getBody().copies().size());
        for (var copy : availResp.getBody().copies()) {
            assertEquals("AVAILABLE", copy.status());
            assertNotNull(copy.inventoryCode());
            assertFalse(copy.inventoryCode().isBlank());
        }
        var codes = availResp.getBody().copies().stream().map(c -> c.inventoryCode()).distinct().toList();
        assertEquals(3, codes.size(), "All inventory codes must be unique");
    }

    @Test
    void createBookWithNullOptionalsWorks() {
        var bookReq = new BookCreateRequest("Test Book", "9781234567890", null, null, "Author", 3);
        var resp = rest.postForEntity("/api/v1/books", bookReq, BookResponse.class);
        assertEquals(HttpStatus.CREATED, resp.getStatusCode());
        assertNotNull(resp.getBody().id());
        assertEquals(3, resp.getBody().totalCopies());
        assertEquals(3, resp.getBody().availableCopies());
    }

    @Test
    void registerLoanCorrectly() {
        var userResp = createUser("loan", "user", "loan@example.com", LocalDate.of(1990, 1, 1));
        createBook("Loan Book", "9781111111111", "Author", 2);
        var availResp = rest.getForEntity(
                "/api/v1/books/isbn/9781111111111/available-copies", AvailableCopiesResponse.class);
        Long copyId = availResp.getBody().copies().get(0).id();

        var loanReq = new LoanCreateRequest(userResp.getBody().id(), copyId,
                LocalDate.of(2026, 7, 29), LocalDate.of(2026, 8, 5));
        var resp = rest.postForEntity("/api/v1/loans", loanReq, LoanResponse.class);
        assertEquals(HttpStatus.CREATED, resp.getStatusCode());
        assertEquals("ACTIVE", resp.getBody().status());

        var availAfter = rest.getForEntity(
                "/api/v1/books/isbn/9781111111111/available-copies", AvailableCopiesResponse.class);
        assertEquals(1, availAfter.getBody().availableCopies());
    }

    @Test
    void rejectSecondOpenLoanForSameUser() {
        var userResp = createUser("multi", "loan", "multi@example.com", LocalDate.of(1990, 1, 1));
        createBook("Multi Loan Book", "9782222222222", "Author", 2);
        var availResp = rest.getForEntity(
                "/api/v1/books/isbn/9782222222222/available-copies", AvailableCopiesResponse.class);
        Long copy1 = availResp.getBody().copies().get(0).id();
        Long copy2 = availResp.getBody().copies().get(1).id();

        var loanReq1 = new LoanCreateRequest(userResp.getBody().id(), copy1,
                LocalDate.of(2026, 7, 29), LocalDate.of(2026, 8, 5));
        assertEquals(HttpStatus.CREATED,
                rest.postForEntity("/api/v1/loans", loanReq1, LoanResponse.class).getStatusCode());

        var loanReq2 = new LoanCreateRequest(userResp.getBody().id(), copy2,
                LocalDate.of(2026, 7, 30), LocalDate.of(2026, 8, 6));
        var resp2 = rest.postForEntity("/api/v1/loans", loanReq2, ErrorResponse.class);
        assertEquals(HttpStatus.CONFLICT, resp2.getStatusCode());
    }

    @Test
    void rejectLoanOfUnavailableCopy() {
        var user1 = createUser("first", "user", "first@example.com", LocalDate.of(1990, 1, 1));
        var user2 = createUser("second", "user", "second@example.com", LocalDate.of(1990, 1, 1));
        createBook("Unavailable Copy", "9783333333333", "Author", 1);
        var availResp = rest.getForEntity(
                "/api/v1/books/isbn/9783333333333/available-copies", AvailableCopiesResponse.class);
        Long copyId = availResp.getBody().copies().get(0).id();

        var loanReq1 = new LoanCreateRequest(user1.getBody().id(), copyId,
                LocalDate.of(2026, 7, 29), LocalDate.of(2026, 8, 5));
        assertEquals(HttpStatus.CREATED,
                rest.postForEntity("/api/v1/loans", loanReq1, LoanResponse.class).getStatusCode());

        var loanReq2 = new LoanCreateRequest(user2.getBody().id(), copyId,
                LocalDate.of(2026, 7, 30), LocalDate.of(2026, 8, 6));
        var resp2 = rest.postForEntity("/api/v1/loans", loanReq2, ErrorResponse.class);
        assertEquals(HttpStatus.CONFLICT, resp2.getStatusCode());
    }

    @Test
    void returnLoanAndReleaseCopy() {
        var userResp = createUser("return", "user", "return@example.com", LocalDate.of(1990, 1, 1));
        createBook("Return Book", "9784444444444", "Author", 1);
        var availResp = rest.getForEntity(
                "/api/v1/books/isbn/9784444444444/available-copies", AvailableCopiesResponse.class);
        Long copyId = availResp.getBody().copies().get(0).id();

        var loanReq = new LoanCreateRequest(userResp.getBody().id(), copyId,
                LocalDate.of(2026, 7, 29), LocalDate.of(2026, 8, 5));
        var loanResp = rest.postForEntity("/api/v1/loans", loanReq, LoanResponse.class).getBody();

        var returnResp = rest.exchange(
                RequestEntity.patch("/api/v1/loans/{id}/return", loanResp.id()).build(),
                LoanResponse.class);
        assertEquals(HttpStatus.OK, returnResp.getStatusCode());
        assertEquals("RETURNED", returnResp.getBody().status());

        var availAfter = rest.getForEntity(
                "/api/v1/books/isbn/9784444444444/available-copies", AvailableCopiesResponse.class);
        assertEquals(1, availAfter.getBody().availableCopies());
    }

    @Test
    void calculateOverdueCorrectly() {
        var userResp = createUser("overdue", "user", "overdue@example.com", LocalDate.of(1990, 1, 1));
        createBook("Overdue Book", "9785555555555", "Author", 1);
        var availResp = rest.getForEntity(
                "/api/v1/books/isbn/9785555555555/available-copies", AvailableCopiesResponse.class);
        Long copyId = availResp.getBody().copies().get(0).id();

        var loanReq = new LoanCreateRequest(userResp.getBody().id(), copyId,
                LocalDate.of(2026, 7, 20), LocalDate.of(2026, 7, 25));
        var resp = rest.postForEntity("/api/v1/loans", loanReq, LoanResponse.class);
        assertEquals(HttpStatus.CREATED, resp.getStatusCode());
        assertEquals("OVERDUE", resp.getBody().status());
    }

    @Test
    void calculateScheduledCorrectly() {
        var userResp = createUser("scheduled", "user", "scheduled@example.com", LocalDate.of(1990, 1, 1));
        createBook("Scheduled Book", "9786666666666", "Author", 1);
        var availResp = rest.getForEntity(
                "/api/v1/books/isbn/9786666666666/available-copies", AvailableCopiesResponse.class);
        Long copyId = availResp.getBody().copies().get(0).id();

        var loanReq = new LoanCreateRequest(userResp.getBody().id(), copyId,
                LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 10));
        var resp = rest.postForEntity("/api/v1/loans", loanReq, LoanResponse.class);
        assertEquals(HttpStatus.CREATED, resp.getStatusCode());
        assertEquals("SCHEDULED", resp.getBody().status());
    }

    @Test
    void malformedJsonReturns400Not500() {
        var headers = new org.springframework.http.HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        var entity = new org.springframework.http.HttpEntity<>("{bad json}", headers);
        var resp = rest.exchange("/api/v1/books", org.springframework.http.HttpMethod.POST, entity, ErrorResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        assertEquals("INVALID_JSON", resp.getBody().code());
    }

    @Test
    void existingStatusEndpointsStillWork() {
        var statusResp = rest.getForEntity("/api/v1/status", String.class);
        assertEquals(HttpStatus.OK, statusResp.getStatusCode());

        var healthResp = rest.getForEntity("/actuator/health", String.class);
        assertEquals(HttpStatus.OK, healthResp.getStatusCode());
    }

    private ResponseEntity<UserResponse> createUser(String firstName, String lastName, String email, LocalDate birthDate) {
        var req = new UserCreateRequest(firstName, lastName, email, birthDate);
        return rest.postForEntity("/api/v1/users", req, UserResponse.class);
    }

    private ResponseEntity<BookResponse> createBook(String title, String isbn, String author, int copies) {
        var req = new BookCreateRequest(title, isbn, null, null, author, copies);
        return rest.postForEntity("/api/v1/books", req, BookResponse.class);
    }
}
