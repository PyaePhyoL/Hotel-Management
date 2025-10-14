package org.bytesync.hotelmanagement.model;

import jakarta.persistence.*;
import lombok.*;

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
    private Double amount;
    private PaymentMethod paymentMethod;
    private String notes;

    @ManyToOne
    private Reservation reservation;

    public void setReservation(Reservation resv) {
        this.reservation = resv;
        resv.addPayment(this);
    }
}
