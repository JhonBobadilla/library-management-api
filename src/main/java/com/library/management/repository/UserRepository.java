package com.library.management.repository;

import com.library.management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email = :email AND u.id <> :id")
    boolean existsByEmailAndIdNot(@Param("email") String email, @Param("id") Long id);

    @Query("SELECT COUNT(l) > 0 FROM Loan l WHERE l.user.id = :userId")
    boolean hasLoans(@Param("userId") Long userId);
}
