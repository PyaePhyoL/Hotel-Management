package org.bytesync.hotelmanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "guest_records")
public class GuestRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Guest guest;

    @ManyToOne
    private Room room;

    @OneToOne
    private Reservation reservation;

    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private Integer daysOfStay;
    private Boolean current;

}
