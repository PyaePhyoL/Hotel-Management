package org.bytesync.hotelmanagement.model;

import jakarta.persistence.*;
import lombok.*;
import org.bytesync.hotelmanagement.enums.IncomeType;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "vouchers")
public class Voucher extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long voucherNo;
    private LocalDate date;
    private String guestName;
    private Long roomNo;
    private Integer price;
    private Boolean isPaid;
    @Enumerated(EnumType.STRING)
    private IncomeType type;

    @ManyToOne
    private Reservation reservation;
    @ManyToOne
    private Payment payment;

    @Column(columnDefinition = "TEXT")
    private String notes;

}
