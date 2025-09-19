package org.bytesync.hotelmanagement.dto.auth;

import org.bytesync.hotelmanagement.model.Role;

public record UserDto(
        long id,
        String username,
        String email,
        Role role,
        boolean enabled
) {
}
