package org.bytesync.hotelmanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.model.enums.VoucherType;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long voucherNo;
    private LocalDate date;
    private String guestName;
    private Long roomNo;
    private Integer price;
    private Boolean isPaid;
    @Enumerated(EnumType.STRING)
    private VoucherType type;

    @ManyToOne
    private Reservation reservation;
    @ManyToOne
    private Payment payment;

}
