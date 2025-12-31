package org.bytesync.hotelmanagement.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordDto(
        @NotBlank(message = "Old password cannot be blank")
        String oldPassword,
        @NotBlank(message = "New password cannot be blank")
        String newPassword,
        @NotBlank(message = "Confirm password cannot be blank")
        String confirmPassword
) {
}
