package org.bytesync.hotelmanagement.repository;

import org.bytesync.hotelmanagement.dto.auth.UserDetailsDto;
import org.bytesync.hotelmanagement.dto.auth.UserDto;
import org.bytesync.hotelmanagement.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("""
    select u from User u
    where u.email = :email
""")
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("""
    select new org.bytesync.hotelmanagement.dto.auth.UserDto(
    u.id,
    u.name,
    u.email,
    u.joinDate,
    u.position,
    u.nrc,
    u.birthDate,
    u.enabled
    )
    from User u
""")
    List<UserDto> findAllDtos(Pageable pageable);

    boolean existsByName(String name);

    boolean existsByNrc(String nrc);

    @Query("""
    select new org.bytesync.hotelmanagement.dto.auth.UserDetailsDto(
    u.id,
    u.name,
    u.email,
    u.position,
    u.role,
    u.nrc,
    u.birthDate,
    u.joinDate,
    u.address,
    u.enabled
    ) from User u
    where u.id = :id
""")
    Optional<UserDetailsDto> findDetailsById(Integer id);
}
