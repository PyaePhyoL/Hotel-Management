package org.bytesync.hotelmanagement.model;

import jakarta.persistence.*;
import lombok.*;
import org.bytesync.hotelmanagement.model.enums.PaymentMethod;

import java.time.LocalDate;

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
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    private Guest guest;

    private Integer roomNo;

    @ManyToOne(fetch = FetchType.LAZY)
    private Reservation reservation;

    public void setReservation(Reservation resv) {
        this.reservation = resv;
        resv.addPayment(this);
    }
}
