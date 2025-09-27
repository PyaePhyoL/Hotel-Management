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
    private String email;
    @Column(unique = true)
    private String phone;
    @Column(unique = true)
    private String nationalId;
    @Column(unique = true)
    private String passport;
    private String occupation;
    private String maritalStatus;
    private String address;
    private LocalDate birthDate;

    @OneToMany(mappedBy = "guest", cascade = CascadeType.ALL)
    private List<Reservation> reservationList = new ArrayList<>();

    public void addReservation(Reservation reservation) {
        this.reservationList.add(reservation);
    }
}
