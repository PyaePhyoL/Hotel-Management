package org.bytesync.hotelmanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bytesync.hotelmanagement.enums.GuestStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "guests")
public class Guest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true)
    private String email;
    @Column(columnDefinition = "VARCHAR(20)")
    private String phoneNumber;
    @Column(unique = true, nullable = false)
    private String nrc;
    @Column(unique = true)
    private String passport;
    private String occupation;
    private String maritalStatus;
    private String address;
    private LocalDate birthDate;

    private Long currentReservationId;
    private Boolean isStaying;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Enumerated(EnumType.STRING)
    private GuestStatus status;

    private String photoUrl;
    private String nrcUrl;
    private Boolean isDeleted;

    @OneToMany(mappedBy = "guest", cascade = CascadeType.ALL)
    private List<Reservation> reservationList = new ArrayList<>();

    @OneToMany(mappedBy = "guest", cascade = CascadeType.ALL)
    private List<Payment> paymentList = new ArrayList<>();

    @OneToMany(mappedBy = "guest", cascade = CascadeType.ALL)
    private List<Refund> refundList = new ArrayList<>();

    @OneToMany(mappedBy = "guest", cascade = CascadeType.ALL)
    private List<GuestRecord> guestRecordList = new ArrayList<>();

    public void addReservation(Reservation reservation) {
        this.reservationList.add(reservation);
        this.setCurrentReservationId(reservation.getId());
    }

    public void addPayment(Payment payment) {
        this.paymentList.add(payment);
    }

    public void addRefund(Refund refund) {
        this.refundList.add(refund);
    }

}
