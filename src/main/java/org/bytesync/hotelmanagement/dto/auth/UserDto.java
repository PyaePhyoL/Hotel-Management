package org.bytesync.hotelmanagement.dto.auth;

import java.time.LocalDate;

public record UserDto(
        long id,
        String name,
        String email,
        LocalDate joinDate,
        String position,
        String nrc,
        LocalDate birthDate,
        boolean enabled
) {
}
