package org.bytesync.hotelmanagement.dto.auth;

import org.bytesync.hotelmanagement.enums.Role;

import java.time.LocalDate;

public record StaffDetailsDto(
        Long id,
        String name,
        String email,
        String phoneNumber,
        String position,
        Role role,
        String nrc,
        LocalDate birthDate,
        LocalDate joinDate,
        String address,
        String fatherName,
        String notes,
        boolean enabled,
        String photoUrl,
        String nrcUrl
) {
}
