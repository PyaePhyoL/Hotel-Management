package org.bytesync.hotelmanagement.dto.auth;

import lombok.Builder;

@Builder
public record UserInfo(
        Long userId,
        String userName,
        String email,
        String role,
        String accessToken,
        String refreshToken
) {
}
