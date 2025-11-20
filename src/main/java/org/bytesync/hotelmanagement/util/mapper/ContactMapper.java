package org.bytesync.hotelmanagement.util.mapper;

import org.bytesync.hotelmanagement.dto.guest.ContactDto;
import org.bytesync.hotelmanagement.model.Contact;

public class ContactMapper {

    private ContactMapper() {
    }

    public static Contact toEntity(ContactDto dto) {
        return Contact.builder()
                .id(dto.id())
                .name(dto.name())
                .phoneNumber(dto.phone())
                .relation(dto.relation())
                .build();
    }

    public static ContactDto toDto(Contact entity) {
        return ContactDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .phone(entity.getPhoneNumber())
                .relation(entity.getRelation())
                .build();
    }

    public static void updateContent(Contact contact, ContactDto contactDto) {
        contact.setName(contactDto.name());
        contact.setPhoneNumber(contactDto.phone());
        contact.setRelation(contactDto.relation());
    }
}
