package org.bytesync.hotelmanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.model.enums.VoucherType;

import java.time.LocalDate;

@Data
@Builder
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String voucherNo;
    private LocalDate date;
    private String guestName;
    private Integer roomNo;
    private Integer price;
    private Boolean isPaid;
    private VoucherType type;

    @ManyToOne
    private Reservation reservation;
    @ManyToOne
    private Payment payment;


}
