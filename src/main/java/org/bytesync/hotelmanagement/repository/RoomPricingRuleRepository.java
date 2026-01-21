package org.bytesync.hotelmanagement.repository;

import org.bytesync.hotelmanagement.dto.room.RoomPricingRuleDto;
import org.bytesync.hotelmanagement.model.RoomPricingRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

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
    order by r.id
""")
    List<RoomPricingRuleDto> findAllRoomPricingRuleDtos();

    @Query("""
    select r.price from RoomPricingRule r
    where r.stayType = 'NORMAL' and
    r.roomType.id = :roomType and
    r.noOfGuests = :guests
    order by r.id desc limit 1
""")
    Optional<Integer> findPriceForNormalStayRoom(String roomType, Integer guests);

    @Query("""
    select r.price from RoomPricingRule r
    where r.stayType = 'SECTION' and
    r.roomType.id = :roomType and
    r.hours = :hours
    order by r.id desc limit 1
""")
    Optional<Integer> findPricingForSectionStayRoom(String roomType, Integer hours);

    @Query("""
    select r.price from RoomPricingRule r
    where r.stayType = 'LONG' and
    r.roomType.id = :roomType and
    r.noOfGuests = :guests
    order by r.id desc limit 1
""")
    Optional<Integer> findPricingForLongStayRoom(String roomType, Integer guests);

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
    where r.id = :id
""")
    Optional<RoomPricingRuleDto> findRoomPricingRuleDtoById(Integer id);
}
