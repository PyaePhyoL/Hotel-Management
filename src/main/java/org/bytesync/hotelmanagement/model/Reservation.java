package org.bytesync.hotelmanagement.model;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bytesync.hotelmanagement.dto.reservation.ReservationInfo;
import org.bytesync.hotelmanagement.model.enums.StayType;

import java.time.LocalDateTime;
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
    private Integer pricePerNight;
    private Integer depositAmount;
    @Enumerated(EnumType.STRING) @Column(columnDefinition = "VARCHAR(50)")
    private StayType stayType;
    private String registeredStaff;
    private Integer noOfGuests;
    @ManyToOne
    private Guest guest;
    @ManyToOne
    private Room room;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DailyVoucher>  dailyVouchers;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> paymentList;

    private Boolean isActive;


    public void addPayment(Payment payment) {
        this.paymentList.add(payment);
    }

    public void addDailyVoucher(DailyVoucher dailyVoucher) {
        this.dailyVouchers.add(dailyVoucher);
    }

    public void incrementDaysOfStayByOne() {
        this.daysOfStay++;
    }
}
