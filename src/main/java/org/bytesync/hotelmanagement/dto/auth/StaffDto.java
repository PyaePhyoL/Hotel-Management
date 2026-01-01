package org.bytesync.hotelmanagement.dto.auth;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record StaffDto(
        Long id,
        String photoUrl,
        String name,
        String email,
        LocalDate joinDate,
        String position,
        String nrc,
        LocalDate birthDate,
        boolean enabled
) {
}
