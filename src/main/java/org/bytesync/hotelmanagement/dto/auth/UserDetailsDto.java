package org.bytesync.hotelmanagement.dto.auth;

import org.bytesync.hotelmanagement.model.enums.Role;

import java.time.LocalDate;

public record UserDetailsDto(
        Integer id,
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
