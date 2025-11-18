package org.bytesync.hotelmanagement.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "refunds")
public class Refund extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate refundDate;
    private Integer amount;
    private String notes;

    @ManyToOne
    private Reservation reservation;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Expense expense;
}
