package org.bytesync.hotelmanagement.util.mapper;

import org.bytesync.hotelmanagement.dto.GuestDto;
import org.bytesync.hotelmanagement.model.Guest;

public class GuestMapper {

    private GuestMapper() {
    }

    public static Guest toEntity(GuestDto guestDto) {
        return Guest.builder()
                .name(guestDto.getName())
                .email(guestDto.getEmail())
                .phone(guestDto.getPhone())
                .nationalId(guestDto.getNationalId())
                .passport(guestDto.getPassport())
                .occupation(guestDto.getOccupation())
                .maritalStatus(guestDto.getMaritalStatus())
                .address(guestDto.getAddress())
                .birthDate(guestDto.getBirthDate())
                .build();
    }

    public static GuestDto toDto(Guest guest) {
        return GuestDto.builder()
                .id(guest.getId())
                .name(guest.getName())
                .email(guest.getEmail())
                .phone(guest.getPhone())
                .nationalId(guest.getNationalId())
                .passport(guest.getPassport())
                .occupation(guest.getOccupation())
                .maritalStatus(guest.getMaritalStatus())
                .address(guest.getAddress())
                .birthDate(guest.getBirthDate())
                .build();
    }
}
