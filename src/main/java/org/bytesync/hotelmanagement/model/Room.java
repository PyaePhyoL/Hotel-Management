package org.bytesync.hotelmanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bytesync.hotelmanagement.enums.Floor;
import org.bytesync.hotelmanagement.enums.RoomStatus;

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

    @ManyToOne
    private RoomType roomType;

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

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
        roomType.addRoom(this);
    }
}
