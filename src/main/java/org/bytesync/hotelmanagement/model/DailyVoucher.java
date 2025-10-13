package org.bytesync.hotelmanagement.model;

import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@RequiredArgsConstructor
public class DailyVoucher {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID voucherNo;
    private LocalDate date;

    @ManyToOne
    private Reservation reservation;

    private Double price;
    private String paymentMethod;
    private Boolean isPaid;
}
