package org.bytesync.hotelmanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private Integer daysOfStay;
    private Double pricePerNight;
    private StayType stayType;
    private String registeredStaff;
    private Integer noOfGuest;
    @ManyToOne
    private Guest guest;
    @ManyToOne
    private Room room;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DailyVoucher>  dailyVouchers;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> paymentList = new ArrayList<>();



    public void addPayment(Payment payment) {
        this.paymentList.add(payment);
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
        guest.addReservation(this);
    }

    public void setRoom(Room room) {
        this.room = room;
        room.addReservation(this);
    }

    public enum StayType {
        NORMAL, LONG
    }

}
