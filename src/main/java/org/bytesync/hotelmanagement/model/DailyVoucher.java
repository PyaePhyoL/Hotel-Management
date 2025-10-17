package org.bytesync.hotelmanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
public class DailyVoucher {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID voucherNo;
    private LocalDate date;

    @ManyToOne
    private Reservation reservation;

    private Integer price;
    private Boolean isPaid;
}
