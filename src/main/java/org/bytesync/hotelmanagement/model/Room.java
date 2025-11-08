package org.bytesync.hotelmanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bytesync.hotelmanagement.model.enums.Floor;
import org.bytesync.hotelmanagement.model.enums.RoomStatus;
import org.bytesync.hotelmanagement.model.enums.RoomType;
import org.bytesync.hotelmanagement.model.enums.StayType;

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
    private Integer roomNo;
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
        this.currentStatus = reservation.getStayType() == StayType.NORMAL
                ? RoomStatus.NORMAL_STAY : RoomStatus.LONG_STAY;
    }

}
