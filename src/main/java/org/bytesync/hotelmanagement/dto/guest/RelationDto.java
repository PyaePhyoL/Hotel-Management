package org.bytesync.hotelmanagement.dto.guest;

import lombok.Builder;

@Builder
public record RelationDto(
        String name,
        String phone,
        String relation
) {
}
