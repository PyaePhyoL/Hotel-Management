package org.bytesync.hotelmanagement.repository;

import org.bytesync.hotelmanagement.dto.room.RoomPricingRuleDto;
import org.bytesync.hotelmanagement.model.RoomPricingRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoomPricingRuleRepository extends JpaRepository<RoomPricingRule, Integer> {


    @Query("""
    select new org.bytesync.hotelmanagement.dto.room.RoomPricingRuleDto(
    r.id,
    r.roomType.id,
    r.roomType.description,
    r.stayType,
    r.noOfGuests,
    r.hours,
    r.price
    )
    from RoomPricingRule r
""")
    List<RoomPricingRuleDto> findAllRoomPricingRuleDtos();
}
