package org.bytesync.hotelmanagement.dto.auth;

import org.bytesync.hotelmanagement.model.Role;

public record RegisterForm(
        String username,
        String email,
        String password,
        Role role
) {
}
