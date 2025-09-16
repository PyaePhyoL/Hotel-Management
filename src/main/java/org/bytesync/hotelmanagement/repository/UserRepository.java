package org.bytesync.hotelmanagement.repository;

import org.bytesync.hotelmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("""
    select u from User u
    where u.userEmail = :email
""")
    Optional<User> findByEmail(String email);
}
