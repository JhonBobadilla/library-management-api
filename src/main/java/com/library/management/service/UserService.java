package com.library.management.service;

import com.library.management.config.TimeProvider;
import com.library.management.dto.UserCreateRequest;
import com.library.management.dto.UserResponse;
import com.library.management.dto.UserUpdateRequest;
import com.library.management.entity.User;
import com.library.management.exception.BusinessRuleException;
import com.library.management.exception.ResourceNotFoundException;
import com.library.management.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserService {

    private static final String USER_NOT_FOUND = "USER_NOT_FOUND";
    private static final String EMAIL_ALREADY_EXISTS = "EMAIL_ALREADY_EXISTS";
    private static final String USER_HAS_LOANS = "USER_HAS_LOANS";

    private final UserRepository userRepository;
    private final TimeProvider timeProvider;

    public UserService(UserRepository userRepository, TimeProvider timeProvider) {
        this.userRepository = userRepository;
        this.timeProvider = timeProvider;
    }

    @Transactional
    public UserResponse create(UserCreateRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessRuleException(EMAIL_ALREADY_EXISTS, "Email already exists");
        }
        if (request.birthDate().isAfter(timeProvider.today())) {
            throw new BusinessRuleException("INVALID_BIRTH_DATE", "Birth date cannot be in the future");
        }
        User user = new User(request.firstName(), request.lastName(), request.email(), request.birthDate());
        user = userRepository.save(user);
        return toResponse(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND, "User", "User not found with id: " + id));
        return toResponse(user);
    }

    @Transactional
    public UserResponse update(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND, "User", "User not found with id: " + id));
        if (userRepository.existsByEmailAndIdNot(request.email(), id)) {
            throw new BusinessRuleException(EMAIL_ALREADY_EXISTS, "Email already exists");
        }
        if (request.birthDate().isAfter(timeProvider.today())) {
            throw new BusinessRuleException("INVALID_BIRTH_DATE", "Birth date cannot be in the future");
        }
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setBirthDate(request.birthDate());
        user = userRepository.save(user);
        return toResponse(user);
    }

    @Transactional
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND, "User", "User not found with id: " + id));
        if (userRepository.hasLoans(id)) {
            throw new BusinessRuleException(USER_HAS_LOANS, "User has registered loans and cannot be deleted");
        }
        userRepository.delete(user);
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getBirthDate(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
