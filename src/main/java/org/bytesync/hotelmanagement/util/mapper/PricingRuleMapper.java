package org.bytesync.hotelmanagement.util.mapper;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.room.RoomPricingRuleDto;
import org.bytesync.hotelmanagement.model.RoomPricingRule;
import org.bytesync.hotelmanagement.repository.RoomPricingRuleRepository;
import org.bytesync.hotelmanagement.repository.RoomTypeRepository;
import org.springframework.stereotype.Component;

import static org.bytesync.hotelmanagement.util.EntityOperationUtils.safeCall;

@Component
@RequiredArgsConstructor
public class PricingRuleMapper {


    private final RoomTypeRepository roomTypeRepository;
    private final RoomPricingRuleRepository roomPricingRuleRepository;

    public void updateRoomPricingRule(RoomPricingRuleDto dto) {
        var roomPricingRule = safeCall(
                roomPricingRuleRepository.findById(dto.getId()),
                "Room Pricing Rule" ,
                dto.getId());

        roomPricingRule.setId(dto.getId());
        roomPricingRule.setNoOfGuests(dto.getNoOfGuests());
        roomPricingRule.setHours(dto.getHours());
        roomPricingRule.setPrice(dto.getPrice());

        roomPricingRuleRepository.save(roomPricingRule);
    }

    public void createRoomPricingRule(RoomPricingRuleDto dto) {
        var roomType = safeCall(roomTypeRepository.findById(dto.getRoomTypeId()), "Room Type", dto.getRoomTypeId());
        RoomPricingRule roomPricingRule = RoomPricingRule.builder()
                .id(dto.getId())
                .roomType(roomType)
                .stayType(dto.getStayType())
                .noOfGuests(dto.getNoOfGuests())
                .hours(dto.getHours())
                .price(dto.getPrice())
                .build();

        roomPricingRuleRepository.save(roomPricingRule);
    }

}
