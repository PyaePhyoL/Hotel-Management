package org.bytesync.hotelmanagement.service.impl.hotel;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.room.RoomPricingRuleDto;
import org.bytesync.hotelmanagement.enums.StayType;
import org.bytesync.hotelmanagement.model.RoomPricingRule;
import org.bytesync.hotelmanagement.repository.RoomPricingRuleRepository;
import org.bytesync.hotelmanagement.service.interfaces.hotel.IPricingService;
import org.bytesync.hotelmanagement.util.mapper.PricingRuleMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PricingServiceImpl implements IPricingService {

    private final RoomPricingRuleRepository roomPricingRuleRepository;
    private final PricingRuleMapper pricingRuleMapper;

    @Override
    public List<RoomPricingRuleDto> getPricingRuleList() {
        return roomPricingRuleRepository.findAllRoomPricingRuleDtos();
    }

    @Override
    public List<RoomPricingRuleDto> updatePricingRulesDetails(List<RoomPricingRuleDto> ruleDtoList) {

        List<Integer> updatedIds = ruleDtoList.stream()
                .map(RoomPricingRuleDto::getId)
                .filter(Objects::nonNull)
                .toList();

        List<RoomPricingRule> deleteRules = roomPricingRuleRepository.findAll()
                .stream()
                .filter(rule -> !updatedIds.contains(rule.getId()))
                .toList();

        ruleDtoList.forEach(ruleDto -> {
            if(ruleDto.getId() != null) {
                pricingRuleMapper.updateRoomPricingRule(ruleDto);
            } else {
                pricingRuleMapper.createRoomPricingRule(ruleDto);
            }
        });

        roomPricingRuleRepository.deleteAll(deleteRules);

        return roomPricingRuleRepository.findAllRoomPricingRuleDtos();
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
}
