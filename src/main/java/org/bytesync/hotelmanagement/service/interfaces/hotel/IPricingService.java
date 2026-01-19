package org.bytesync.hotelmanagement.service.interfaces.hotel;

import org.bytesync.hotelmanagement.dto.room.RoomPricingRuleDto;
import org.bytesync.hotelmanagement.enums.StayType;

import java.util.List;

public interface IPricingService {

    List<RoomPricingRuleDto> getPricingRuleList();

    List<RoomPricingRuleDto> updatePricingRulesDetails(List<RoomPricingRuleDto> ruleDtoList);

    Integer getPricing(String roomType, StayType stayType, Integer guests, Integer hours);
}
