package org.bytesync.hotelmanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
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

    @ManyToOne
    private Guest guest;

    @ManyToOne
    private Room room;

    private Integer price;
    private Boolean isPaid;
}
