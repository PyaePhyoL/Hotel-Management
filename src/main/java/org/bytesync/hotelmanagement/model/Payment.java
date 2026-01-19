package org.bytesync.hotelmanagement.model;

import jakarta.persistence.*;
import lombok.*;
import org.bytesync.hotelmanagement.enums.PaymentMethod;
import org.bytesync.hotelmanagement.enums.PaymentType;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "payments")
public class Payment extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate paymentDate;
    private Integer amount;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    private Reservation reservation;

    @Enumerated(value = EnumType.STRING)
    private PaymentType paymentType;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.PERSIST)
    private List<Voucher> vouchers;

    public void setReservation(Reservation resv) {
        this.reservation = resv;
        resv.addPayment(this);
    }

    public void addDailyVoucher(Voucher voucher) {
        this.vouchers.add(voucher);
        voucher.setPayment(this);
    }

}
