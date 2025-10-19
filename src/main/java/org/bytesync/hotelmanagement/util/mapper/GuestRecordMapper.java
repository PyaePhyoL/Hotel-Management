package org.bytesync.hotelmanagement.util.mapper;

import org.bytesync.hotelmanagement.dto.guest.GuestRecordDto;
import org.bytesync.hotelmanagement.model.GuestRecord;

public class GuestRecordMapper {

    private GuestRecordMapper() {
    }

    public static GuestRecordDto toDto(GuestRecord guestRecord) {
        return GuestRecordDto.builder()
                .id(guestRecord.getId())
                .guestName(guestRecord.getGuest().getName())
                .roomNo(guestRecord.getRoom().getRoomNo())
                .checkInTime(guestRecord.getCheckInTime())
                .checkOutTime(guestRecord.getCheckOutTime())
                .daysOfStay(guestRecord.getDaysOfStay())
                .build();
    }
}
