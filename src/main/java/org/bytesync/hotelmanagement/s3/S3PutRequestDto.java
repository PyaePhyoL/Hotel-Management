package org.bytesync.hotelmanagement.s3;

public record S3PutRequestDto(
        String key,
        String contentType
) {
}
