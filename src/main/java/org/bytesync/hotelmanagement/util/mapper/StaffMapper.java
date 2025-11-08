package org.bytesync.hotelmanagement.util.mapper;

import org.bytesync.hotelmanagement.dto.auth.StaffRegisterForm;
import org.bytesync.hotelmanagement.dto.auth.StaffDto;
import org.bytesync.hotelmanagement.model.Staff;

public class StaffMapper {

    private StaffMapper() {
    }

    public static Staff toEntity(StaffRegisterForm form) {
        return Staff.builder()
                .name(form.name())
                .email(form.email())
                .role(form.role())
                .position(form.position())
                .nrc(form.nrc())
                .birthDate(form.birthDate())
                .joinDate(form.joinDate())
                .address(form.address())
                .enabled(true)
                .phoneNumber(form.phoneNumber())
                .fatherName(form.fatherName())
                .notes(form.notes())
                .build();
    }

    public static void update(Staff staff, StaffRegisterForm form) {
        staff.setName(form.name());
        staff.setEmail(form.email());
        staff.setRole(form.role());
        staff.setPosition(form.position());
        staff.setNrc(form.nrc());
        staff.setBirthDate(form.birthDate());
        staff.setJoinDate(form.joinDate());
        staff.setAddress(form.address());

    }

    public static StaffDto toStaffDto(Staff staff) {
        return StaffDto.builder()
                .id(staff.getId())
                .name(staff.getName())
                .email(staff.getEmail())
                .joinDate(staff.getJoinDate())
                .position(staff.getPosition())
                .nrc(staff.getNrc())
                .birthDate(staff.getBirthDate())
                .enabled(staff.isEnabled())
                .build();
    }
}
