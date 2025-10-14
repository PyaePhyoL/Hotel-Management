package org.bytesync.hotelmanagement.repository;

import org.bytesync.hotelmanagement.dto.guest.GuestDto;
import org.bytesync.hotelmanagement.model.Guest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GuestRepository extends JpaRepository<Guest, Integer>, JpaSpecificationExecutor<Guest> {
    boolean existsByEmail(String email);

    boolean existsByPassport(String passport);

    boolean existsByPhone(String phone);

    boolean existsByNrc(String nrc);

    @Query("""
    select new org.bytesync.hotelmanagement.dto.guest.GuestDto(
    g.id,
    g.name,
    g.email,
    g.phone,
    g.nrc,
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

    @Query("""
    select new org.bytesync.hotelmanagement.dto.guest.GuestDto(
    g.id,
    g.name,
    g.email,
    g.phone,
    g.nrc,
    g.passport,
    g.occupation,
    g.maritalStatus,
    g.address,
    g.birthDate
    )
    from Guest g
""")
    List<GuestDto> findAllGuestDto(Pageable pageable);

    boolean existsByName(String name);

    Optional<Guest> findByNameAndNrc(String name, String nrc);
}
