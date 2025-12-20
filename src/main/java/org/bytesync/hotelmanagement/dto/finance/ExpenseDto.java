package org.bytesync.hotelmanagement.dto.finance;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import org.bytesync.hotelmanagement.model.enums.ExpenseType;

import java.time.LocalDate;

@Data
@Builder
public class ExpenseDto {
    private Long id;
    @NotNull(message = "Date cannot be null")
    private LocalDate date;
    @NotBlank(message = "Title cannot be blank")
    private String title;
    private ExpenseType type;
    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be a positive number")
    private Integer amount;
    private String notes;
}
