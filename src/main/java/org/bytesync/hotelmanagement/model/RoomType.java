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
@Table(name = "room_types")
public class RoomType {

    @Id
    @Column(columnDefinition = "VARCHAR(50)")
    private String id;
    private String description;
    private Integer price;
    private Integer capacity;

    @OneToMany(mappedBy = "roomType")
    private List<Room> roomList = new ArrayList<>();

    @OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomPricingRule> roomPricingRuleList = new ArrayList<>();

    public void addRoom(Room room) {
        this.roomList.add(room);
    }

    public void addRoomPricingRule(RoomPricingRule rule) {
        this.roomPricingRuleList.add(rule);
        rule.setRoomType(this);
    }

}
