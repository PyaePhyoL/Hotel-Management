package org.bytesync.hotelmanagement.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.bytesync.hotelmanagement.enums.Role;

import java.time.LocalDate;

public record StaffRegisterForm(
        @NotBlank(message = "Name cannot be blank")
        String name,
        @Email(message = "Email must be in correct format")
        @NotBlank(message = "Email cannot be blank")
        String email,
        @NotBlank(message = "Phone Number cannot be blank")
        String phoneNumber,
        String position,
        @NotNull(message = "Role cannot be blank")
        Role role,
        String nrc,
        LocalDate birthDate,
        LocalDate joinDate,
        String address,
        String fatherName,
        String notes
) {
}
