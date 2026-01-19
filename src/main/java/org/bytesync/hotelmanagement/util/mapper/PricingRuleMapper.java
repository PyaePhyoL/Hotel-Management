package org.bytesync.hotelmanagement.util.mapper;

import org.bytesync.hotelmanagement.dto.room.RoomPricingRuleDto;
import org.bytesync.hotelmanagement.model.RoomPricingRule;

public class PricingRuleMapper {

    private PricingRuleMapper() {
    }

    public static void updateRoomPricingRule(RoomPricingRule roomPricingRule, RoomPricingRuleDto dto) {
        roomPricingRule.setId(dto.getId());
        roomPricingRule.setNoOfGuests(dto.getNoOfGuests());
        roomPricingRule.setHours(dto.getHours());
        roomPricingRule.setPrice(dto.getPrice());
    }

    public static RoomPricingRule createRoomPricingRule(RoomPricingRuleDto dto) {
        return RoomPricingRule.builder()
                .id(dto.getId())
                .stayType(dto.getStayType())
                .noOfGuests(dto.getNoOfGuests())
                .hours(dto.getHours())
                .price(dto.getPrice())
                .build();
    }

}
