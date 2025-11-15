package org.bytesync.hotelmanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.model.enums.ExpenseType;

import java.time.LocalDate;

@Data
@Builder
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private LocalDate date;
    private String title;
    @Enumerated(EnumType.STRING)
    private ExpenseType type;
    private Integer amount;
    private String notes;
}
