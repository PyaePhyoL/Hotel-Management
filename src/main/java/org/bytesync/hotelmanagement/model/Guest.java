package org.bytesync.hotelmanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private Integer id;
    private String name;
    @Column(unique = true)
    private String email;
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> phoneList = new HashSet<>();
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

    @OneToMany(mappedBy = "guest", cascade = CascadeType.ALL)
    private List<Reservation> reservationList = new ArrayList<>();

    @OneToMany(mappedBy = "guest", cascade = CascadeType.ALL)
    private List<Relation> relations = new ArrayList<>();

    public void addReservation(Reservation reservation) {
        this.reservationList.add(reservation);
        this.setCurrentReservationId(reservation.getId());
        this.setIsStaying(true);
    }

    public void addRelation(Relation relation) {
        this.relations.add(relation);
        relation.setGuest(this);
    }

    public void addPhone(String phone) {
        this.phoneList.add(phone);
    }

}
