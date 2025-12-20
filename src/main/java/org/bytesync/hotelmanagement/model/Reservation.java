package org.bytesync.hotelmanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bytesync.hotelmanagement.model.enums.Status;
import org.bytesync.hotelmanagement.model.enums.StayType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reservations")
public class Reservation {

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
    private String registeredStaff;
    private Integer noOfGuests;
    @ManyToOne
    private Guest guest;
    @ManyToOne
    private Room room;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Voucher> vouchers;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> paymentList;

    @Enumerated(EnumType.STRING) @Column(columnDefinition = "VARCHAR(50)")
    private Status status;
    @Column(columnDefinition = "TEXT")
    private String notes;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contact> contacts = new ArrayList<>();

    public void addPayment(Payment payment) {
        this.paymentList.add(payment);
    }

    public void addDailyVoucher(Voucher voucher) {
        this.vouchers.add(voucher);
    }

    public void incrementDaysOfStayByOne() {
        this.daysOfStay++;
    }

    public void addRelation(Contact contact) {
        this.contacts.add(contact);
        contact.setReservation(this);
    }
}
