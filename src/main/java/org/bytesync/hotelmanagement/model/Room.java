package org.bytesync.hotelmanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bytesync.hotelmanagement.enums.Floor;
import org.bytesync.hotelmanagement.enums.RoomStatus;
import org.bytesync.hotelmanagement.enums.RoomType;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomNo;
    @Enumerated(EnumType.STRING)
    private RoomType roomType;
    private Integer basePrice;
    private Integer capacity;
    @Enumerated(EnumType.STRING) @Column(columnDefinition = "VARCHAR(50)")
    private Floor floor;
    @Enumerated(EnumType.STRING) @Column(columnDefinition = "VARCHAR(50)")
    private RoomStatus currentStatus;
    @Column(columnDefinition = "TEXT")
    private String notes;
    private Long currentReservationId;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reservation> reservationList = new ArrayList<>();

    public void addReservation(Reservation reservation) {
        this.reservationList.add(reservation);
        this.setCurrentReservationId(reservation.getId());
    }
}
