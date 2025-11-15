package org.bytesync.hotelmanagement.model;

import jakarta.persistence.*;
import lombok.*;
import org.bytesync.hotelmanagement.model.enums.PaymentMethod;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "payments")
public class Payment extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private LocalDate paymentDate;
    private Integer amount;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    private Reservation reservation;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.PERSIST)
    private List<DailyVoucher> dailyVouchers;

    public void setReservation(Reservation resv) {
        this.reservation = resv;
        resv.addPayment(this);
    }

    public void addDailyVoucher(DailyVoucher voucher) {
        this.dailyVouchers.add(voucher);
        voucher.setPayment(this);
    }
}
