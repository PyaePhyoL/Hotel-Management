package org.bytesync.hotelmanagement.dto.guest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bytesync.hotelmanagement.enums.GuestStatus;

import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GuestDto {
    private Long id;
    @NotBlank(message = "Name cannot be blank")
    private String name;
    @Email(message = "Email must be in correct format")
    private String email;
    private Set<String> phoneList;
    @NotBlank(message = "NRC cannot be blank")
    private String nrc;
    private String passport;
    private String occupation;
    private String maritalStatus;
    private String address;
    private LocalDate birthDate;
    private GuestStatus status;
    private String notes;
}
