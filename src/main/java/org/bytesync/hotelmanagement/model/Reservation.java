package org.bytesync.hotelmanagement.model;

import jakarta.persistence.*;
import lombok.*;
import org.bytesync.hotelmanagement.enums.Status;
import org.bytesync.hotelmanagement.enums.StayType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.bytesync.hotelmanagement.util.EntityOperationUtils.getDaysBetween;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reservations")
public class Reservation extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime checkInDateTime;
    private LocalDateTime checkOutDateTime;
    private Integer daysOfStay;
    private Integer price;
    private Integer discount;
    private Integer deposit;
    @Enumerated(EnumType.STRING) @Column(columnDefinition = "VARCHAR(50)")
    private StayType stayType;
    private Integer noOfGuests;
    @ManyToOne
    private Guest guest;
    @ManyToOne
    private Room room;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Voucher> voucherList;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> paymentList;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Refund> refundList;

    @Enumerated(EnumType.STRING) @Column(columnDefinition = "VARCHAR(50)")
    private Status status;
    @Column(columnDefinition = "TEXT")
    private String notes;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contact> contactList;

    public void addPayment(Payment payment) {
        this.paymentList.add(payment);
    }

    public void addVoucher(Voucher voucher) {
        this.voucherList.add(voucher);
    }

    public void addRefund(Refund refund) {
        this.refundList.add(refund);
    }

    public void incrementDaysOfStayByOne() {
        this.daysOfStay++;
    }

    public void addRelation(Contact contact) {
        this.contactList.add(contact);
        contact.setReservation(this);
    }

    public void setNewCheckOutDateTime(LocalDateTime newCheckOutDateTime) {
        this.daysOfStay = getDaysBetween(this.checkInDateTime.toLocalDate(),
                newCheckOutDateTime.toLocalDate());
    }
}
