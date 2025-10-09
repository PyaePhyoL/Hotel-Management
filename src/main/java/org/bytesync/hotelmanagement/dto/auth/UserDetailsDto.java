package org.bytesync.hotelmanagement.dto.auth;

import org.bytesync.hotelmanagement.model.Role;

import java.time.LocalDate;

public record UserDetailsDto(
        Long id,
        String name,
        String email,
        String position,
        Role role,
        String nrc,
        LocalDate birthDate,
        LocalDate joinDate,
        String address,
        boolean enabled
) {
}
