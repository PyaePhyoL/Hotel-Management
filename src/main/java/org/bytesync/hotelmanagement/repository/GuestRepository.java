package org.bytesync.hotelmanagement.repository;

import org.bytesync.hotelmanagement.dto.GuestDto;
import org.bytesync.hotelmanagement.model.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface GuestRepository extends JpaRepository<Guest, Long> {
    boolean existsByEmail(String email);

    boolean existsByPassport(String passport);

    boolean existsByPhone(String phone);

    boolean existsByNationalId(String nationalId);

    @Query("""
    select new org.bytesync.hotelmanagement.dto.GuestDto(
    g.id,
    g.name,
    g.email,
    g.phone,
    g.nationalId,
    g.passport,
    g.occupation,
    g.maritalStatus,
    g.address,
    g.birthDate
    )
    from Guest g
    where g.id = :id
""")
    Optional<GuestDto> findGuestDtoById(Integer id);
}
