package org.bytesync.hotelmanagement.dto.guest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class GuestDto {
    private Integer id;
    @NotBlank(message = "Name cannot be blank")
    private String name;
    @Email(message = "Email must be in correct format")
    @NotBlank(message = "Email cannot be blank")
    private String email;
    @NotBlank(message = "Phone cannot be blank")
    private String phone;
    @NotBlank(message = "NRC cannot be blank")
    private String nrc;
    private String passport;
    private String occupation;
    private String maritalStatus;
    private String address;
    private LocalDate birthDate;
}
