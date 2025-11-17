package org.bytesync.hotelmanagement.util.mapper;

import org.bytesync.hotelmanagement.dto.guest.ContactDto;
import org.bytesync.hotelmanagement.model.Contact;

public class ContactMapper {

    private ContactMapper() {
    }

    public static Contact toEntity(ContactDto dto) {
        return Contact.builder()
                .name(dto.name())
                .phoneNumber(dto.phone())
                .relation(dto.relation())
                .build();
    }

    public static ContactDto toDto(Contact entity) {
        return ContactDto.builder()
                .name(entity.getName())
                .phone(entity.getPhoneNumber())
                .relation(entity.getRelation())
                .build();
    }
}
