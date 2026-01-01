package org.bytesync.hotelmanagement.repository;

import org.bytesync.hotelmanagement.dto.auth.StaffDetailsDto;
import org.bytesync.hotelmanagement.dto.auth.StaffDto;
import org.bytesync.hotelmanagement.model.Staff;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, Long>, JpaSpecificationExecutor<Staff> {

    @Query("""
    select u from Staff u
    where u.email = :email
""")
    Optional<Staff> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("""
    select new org.bytesync.hotelmanagement.dto.auth.StaffDto(
    u.id,
    u.photoUrl,
    u.name,
    u.email,
    u.joinDate,
    u.position,
    u.nrc,
    u.birthDate,
    u.enabled
    )
    from Staff u
""")
    List<StaffDto> findAllDtos(Pageable pageable);

    boolean existsByName(String name);

    boolean existsByNrc(String nrc);

    @Query("""
    select new org.bytesync.hotelmanagement.dto.auth.StaffDetailsDto(
    u.id,
    u.name,
    u.email,
    u.phoneNumber,
    u.position,
    u.role,
    u.nrc,
    u.birthDate,
    u.joinDate,
    u.address,
    u.fatherName,
    u.notes,
    u.enabled,
    u.photoUrl,
    u.nrcUrl
    ) from Staff u
    where u.id = :id
""")
    Optional<StaffDetailsDto> findDetailsById(Long id);

    @Query("""
    select s.name from Staff s
    where s.email = :email
""")
    Optional<String> findUsernameByEmail(String email);
}
