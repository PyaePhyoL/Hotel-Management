package org.bytesync.hotelmanagement.api;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.finance.PricingAmountDto;
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

    @PostMapping("/add-rule")
    public ResponseEntity<ResponseMessage<Void>> createNewPricingRule(@RequestBody RoomPricingRuleDto ruleDto) {
        var message = pricingService.createPricingRule(ruleDto);
        return ResponseEntity.ok(new ResponseMessage<>(
                HttpStatus.CREATED.value(),
                message,
                null
        ));
    }

    @GetMapping("/rule/{id}")
    public ResponseEntity<ResponseMessage<RoomPricingRuleDto>> getRoomPricingRuleDto(@PathVariable Integer id) {
        var ruleDto = pricingService.getPricingRuleDtoById(id);
        return ResponseEntity.ok(new ResponseMessage<>(
                HttpStatus.OK.value(),
                "",
                ruleDto
        ));
    }

    @PutMapping("/update-rule/{id}")
    public ResponseEntity<ResponseMessage<Void>> updatePricingRulesDetails(@PathVariable Integer id, @RequestBody RoomPricingRuleDto ruleDto) {
        var message = pricingService.updatePricingRuleById(id, ruleDto);
        return ResponseEntity.ok(new ResponseMessage<>(
                HttpStatus.OK.value(),
                message,
                null
        ));
    }

    @DeleteMapping("/delete-rule/{id}")
    public ResponseEntity<ResponseMessage<Void>> deletePricingRule(@PathVariable Integer id) {
        var message = pricingService.deleteById(id);
        return ResponseEntity.ok(new ResponseMessage<>(
                HttpStatus.OK.value(),
                message,
                null
        ));
    }

    @GetMapping("/get-amount")
    public ResponseEntity<ResponseMessage<PricingAmountDto>> getPricing(
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
