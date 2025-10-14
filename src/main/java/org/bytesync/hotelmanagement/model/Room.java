package org.bytesync.hotelmanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bytesync.hotelmanagement.model.enums.Floor;
import org.bytesync.hotelmanagement.model.enums.RoomStatus;
import org.bytesync.hotelmanagement.model.enums.RoomType;

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
    private Integer no;
    @Enumerated(EnumType.STRING)
    private RoomType roomtype;
    private Double basePrice;
    private Double addOnPrice;
    private Integer capacity;
    @Enumerated(EnumType.STRING)
    private Floor floor;
    @Enumerated(EnumType.STRING)
    private RoomStatus currentStatus;

    private Long currentReservationId;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reservation> reservationList = new ArrayList<>();

    public void addReservation(Reservation reservation) {
        this.reservationList.add(reservation);
    }

}
