package org.bytesync.hotelmanagement.model;

import jakarta.persistence.*;
import lombok.*;
import org.bytesync.hotelmanagement.enums.RefundType;

import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "refunds")
public class Refund extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;

    @ManyToOne
    private Guest guest;
    private Integer amount;

    @Column(columnDefinition = "TEXT")
    private String notes;
    private RefundType type;

    @ManyToOne
    private Reservation reservation;

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
        reservation.addRefund(this);
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
        guest.addRefund(this);
    }
}
