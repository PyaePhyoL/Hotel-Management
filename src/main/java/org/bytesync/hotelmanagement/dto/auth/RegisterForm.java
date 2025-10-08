package org.bytesync.hotelmanagement.dto.auth;

import lombok.NonNull;
import org.bytesync.hotelmanagement.model.Role;

import java.time.LocalDate;

public record RegisterForm(
        @NonNull String name,
        @NonNull String email,
        String password,
        String position,
        @NonNull Role role,
        String nrc,
        LocalDate birthDate,
        LocalDate joinDate,
        String address
) {
}
