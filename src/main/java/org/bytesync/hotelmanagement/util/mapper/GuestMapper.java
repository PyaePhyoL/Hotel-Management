package org.bytesync.hotelmanagement.util.mapper;

import org.bytesync.hotelmanagement.dto.guest.GuestDto;
import org.bytesync.hotelmanagement.model.Guest;
import org.bytesync.hotelmanagement.enums.GuestStatus;

public class GuestMapper {

    private GuestMapper() {
    }

    public static Guest toEntity(GuestDto guestDto) {
        return Guest.builder()
                .name(guestDto.getName())
                .email(guestDto.getEmail())
                .phoneList(guestDto.getPhoneList())
                .nrc(guestDto.getNrc())
                .passport(guestDto.getPassport())
                .occupation(guestDto.getOccupation())
                .maritalStatus(guestDto.getMaritalStatus())
                .address(guestDto.getAddress())
                .birthDate(guestDto.getBirthDate())
                .isStaying(false)
                .notes(guestDto.getNotes())
                .status(GuestStatus.GOOD)
                .build();
    }

    public static GuestDto toDto(Guest guest) {
        if (guest == null) return null;
        return GuestDto.builder()
                .id(guest.getId())
                .name(guest.getName())
                .email(guest.getEmail())
                .phoneList(guest.getPhoneList())
                .nrc(guest.getNrc())
                .passport(guest.getPassport())
                .occupation(guest.getOccupation())
                .maritalStatus(guest.getMaritalStatus())
                .address(guest.getAddress())
                .birthDate(guest.getBirthDate())
                .notes(guest.getNotes())
                .status(guest.getStatus())
                .build();
    }

    public static void updateGuest(Guest guest, GuestDto form) {
        guest.setName(form.getName());
        guest.setEmail(form.getEmail());
        guest.setPhoneList(form.getPhoneList());
        guest.setNrc(form.getNrc());
        guest.setPassport(form.getPassport());
        guest.setOccupation(form.getOccupation());
        guest.setMaritalStatus(form.getMaritalStatus());
        guest.setAddress(form.getAddress());
        guest.setBirthDate(form.getBirthDate());
        guest.setNotes(form.getNotes());
        guest.setStatus(form.getStatus());
    }
}
