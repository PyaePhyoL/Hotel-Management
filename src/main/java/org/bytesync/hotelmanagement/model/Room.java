package org.bytesync.hotelmanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Double basePrice;
    private Double addOnPrice;
    private Integer capacity;
    private Floor floor;
    @Enumerated(EnumType.STRING)
    private RoomStatus currentStatus;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<Reservation> reservationList = new ArrayList<>();

    public void addReservation(Reservation reservation) {
        this.reservationList.add(reservation);
    }

    public enum RoomStatus {
        AVAILABLE, NORMAL_STAY, LONG_STAY, IN_SERVICE, STORE
    }

    public enum Floor {
        FOURTH, FIFTH, SEVENTH, EIGHTH
    }
}
