package org.bytesync.hotelmanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "guests")
public class Guest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(unique = true, nullable = false)
    private String phone;
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
    private String note;

    @OneToMany(mappedBy = "guest", cascade = CascadeType.ALL)
    private List<Reservation> reservationList = new ArrayList<>();

    @ManyToMany
    private List<Guest> relations;

    public void addReservation(Reservation reservation) {
        this.reservationList.add(reservation);
        this.setCurrentReservationId(reservation.getId());
        this.setIsStaying(true);
    }

    public void addRelation(Guest guest) {
        this.relations.add(guest);
        guest.addRelation(this);
    }

}
