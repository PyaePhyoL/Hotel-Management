package org.bytesync.hotelmanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bytesync.hotelmanagement.enums.StayType;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "room_pricing_rules")
public class RoomPricingRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private RoomType roomType;
    @Enumerated(value = EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(50)")
    private StayType stayType;
    private Integer noOfGuests; // for normal stay
    private Integer hours;      // for session stay
    private Integer price;
}
