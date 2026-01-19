package org.bytesync.hotelmanagement.api;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.output.ResponseMessage;
import org.bytesync.hotelmanagement.dto.room.RoomPricingRuleDto;
import org.bytesync.hotelmanagement.enums.StayType;
import org.bytesync.hotelmanagement.service.interfaces.hotel.IPricingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pricing/api")
public class PricingRulesApi {

    private final IPricingService pricingService;

    @GetMapping("/rule-list")
    public ResponseEntity<ResponseMessage<List<RoomPricingRuleDto>>> getPricingRuleList() {
        var pricingRules = pricingService.getPricingRuleList();
        return ResponseEntity.ok(new ResponseMessage<>(
                HttpStatus.OK.value(),
                "",
                pricingRules
        ));
    }

    @PutMapping("/update-rules")
    public ResponseEntity<ResponseMessage<List<RoomPricingRuleDto>>> updatePricingRulesDetails(@RequestBody List<RoomPricingRuleDto> ruleDtoList) {
        var pricingRuleDtos = pricingService.updatePricingRulesDetails(ruleDtoList);
        return ResponseEntity.ok(new ResponseMessage<>(
                HttpStatus.OK.value(),
                "Pricing Rules updated",
                pricingRuleDtos
        ));
    }

    @GetMapping("/get-amount")
    public ResponseEntity<ResponseMessage<Integer>> getPricing(
            @RequestParam String roomType,
            @RequestParam StayType stayType,
            @RequestParam Integer guests,
            @RequestParam(required = false) Integer hours
            ) {
        var amount = pricingService.getPricing(roomType, stayType, guests, hours);
        return ResponseEntity.ok(new ResponseMessage<>(
                HttpStatus.OK.value(),
                "",
                amount
        ));
    }
}
