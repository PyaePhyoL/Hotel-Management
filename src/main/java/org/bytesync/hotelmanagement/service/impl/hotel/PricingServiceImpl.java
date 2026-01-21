package org.bytesync.hotelmanagement.service.impl.hotel;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.room.RoomPricingRuleDto;
import org.bytesync.hotelmanagement.enums.StayType;
import org.bytesync.hotelmanagement.repository.RoomPricingRuleRepository;
import org.bytesync.hotelmanagement.repository.RoomTypeRepository;
import org.bytesync.hotelmanagement.service.interfaces.hotel.IPricingService;
import org.bytesync.hotelmanagement.util.mapper.PricingRuleMapper;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.bytesync.hotelmanagement.util.EntityOperationUtils.safeCall;

@Service
@RequiredArgsConstructor
public class PricingServiceImpl implements IPricingService {

    private final RoomPricingRuleRepository roomPricingRuleRepository;
    private final RoomTypeRepository roomTypeRepository;

    @Override
    public List<RoomPricingRuleDto> getPricingRuleList() {
        return roomPricingRuleRepository.findAllRoomPricingRuleDtos();
    }

    @Override
    public String updatePricingRuleById(Integer id, RoomPricingRuleDto ruleDto) {
        var roomPricingRule = safeCall(
                roomPricingRuleRepository.findById(id),
                "Room Pricing Rule" ,
                id);
        PricingRuleMapper.updateRoomPricingRule(roomPricingRule, ruleDto);
        roomPricingRuleRepository.save(roomPricingRule);
        return "Pricing rule updated!";
    }

    @Override
    public Integer getPricing(String roomType,
                              StayType stayType,
                              Integer guests,
                              Integer hours) {

         switch (stayType) {
            case NORMAL -> {
                return roomPricingRuleRepository.findPriceForNormalStayRoom(roomType, guests).orElse(0);
            }
            case SECTION -> {
                return roomPricingRuleRepository.findPricingForSectionStayRoom(roomType, hours).orElse(0);
            }
             case LONG -> {
                return roomPricingRuleRepository.findPricingForLongStayRoom(roomType, guests).orElse(0);
             }
             case null, default -> {
                return 0;
             }
        }

    }

    @Override
    public String createPricingRule(RoomPricingRuleDto ruleDto) {
        var roomType = safeCall(roomTypeRepository.findById(ruleDto.getRoomTypeId()), "Room Type", ruleDto.getRoomTypeId());
        var roomPricingRule = PricingRuleMapper.createRoomPricingRule(ruleDto);
        roomPricingRule.setRoomType(roomType);
        roomPricingRuleRepository.save(roomPricingRule);
        return "New Pricing added";
    }

    @Override
    public String deleteById(Integer id) {
        var roomPricingRule = safeCall(roomPricingRuleRepository.findById(id), "Pricing Rule", id);
        roomPricingRuleRepository.delete(roomPricingRule);
        return "Pricing deleted";
    }

    @Override
    public RoomPricingRuleDto getPricingRuleDtoById(Integer id) {
        return safeCall(roomPricingRuleRepository.findRoomPricingRuleDtoById(id), "Pricing Rule", id);
    }
}
