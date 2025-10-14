package org.bytesync.hotelmanagement.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.bytesync.hotelmanagement.model.enums.Role;

import java.time.LocalDate;

public record RegisterForm(
        @NotBlank(message = "Name cannot be blank")
        String name,
        @Email(message = "Email must be in correct format")
        @NotBlank(message = "Email cannot be blank")
        String email,
        @NotBlank(message = "Password cannot be blank")
        String password,
        String position,
        @NotBlank(message = "Role cannot be blank")
        Role role,
        String nrc,
        LocalDate birthDate,
        LocalDate joinDate,
        String address
) {
}
