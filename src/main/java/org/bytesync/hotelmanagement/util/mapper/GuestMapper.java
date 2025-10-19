package org.bytesync.hotelmanagement.util.mapper;

import org.bytesync.hotelmanagement.dto.guest.GuestDto;
import org.bytesync.hotelmanagement.model.Guest;

public class GuestMapper {

    private GuestMapper() {
    }

    public static Guest toEntity(GuestDto guestDto) {
        return Guest.builder()
                .name(guestDto.getName())
                .email(guestDto.getEmail())
                .phone(guestDto.getPhone())
                .nrc(guestDto.getNrc())
                .passport(guestDto.getPassport())
                .occupation(guestDto.getOccupation())
                .maritalStatus(guestDto.getMaritalStatus())
                .address(guestDto.getAddress())
                .birthDate(guestDto.getBirthDate())
                .isStaying(false)
                .build();
    }

    public static GuestDto toDto(Guest guest) {
        if (guest == null) return null;
        return GuestDto.builder()
                .id(guest.getId())
                .name(guest.getName())
                .email(guest.getEmail())
                .phone(guest.getPhone())
                .nrc(guest.getNrc())
                .passport(guest.getPassport())
                .occupation(guest.getOccupation())
                .maritalStatus(guest.getMaritalStatus())
                .address(guest.getAddress())
                .birthDate(guest.getBirthDate())
                .build();
    }

    public static void updateGuest(Guest guest, GuestDto form) {
        guest.setName(form.getName());
        guest.setEmail(form.getEmail());
        guest.setPhone(form.getPhone());
        guest.setNrc(form.getNrc());
        guest.setPassport(form.getPassport());
        guest.setOccupation(form.getOccupation());
        guest.setMaritalStatus(form.getMaritalStatus());
        guest.setAddress(form.getAddress());
        guest.setBirthDate(form.getBirthDate());
    }
}
