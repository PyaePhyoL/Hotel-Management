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
    private String voucherNo;
    private LocalDate date;
    private String guestName;
    private Integer roomNo;
    private Integer price;
    private Boolean isPaid;

    @ManyToOne
    private Reservation reservation;
    @ManyToOne
    private Payment payment;


}
