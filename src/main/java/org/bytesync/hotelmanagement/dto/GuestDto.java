package org.bytesync.hotelmanagement.dto;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class GuestDto {
    private Integer id;
    private String name;
    private String email;
    private String phone;
    private String nationalId;
    private String passport;
    private String occupation;
    private String maritalStatus;
    private String address;
    private LocalDate birthDate;
}
