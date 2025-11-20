package org.bytesync.hotelmanagement.dto.guest;

import lombok.Builder;

@Builder
public record ContactDto(
        Integer id,
        String name,
        String phone,
        String relation
) {
}
