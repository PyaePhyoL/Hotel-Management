package org.bytesync.hotelmanagement.dto.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bytesync.hotelmanagement.enums.StayType;

@Data
@AllArgsConstructor
public class RoomPricingRuleDto {
    private Integer id;
    private String roomTypeId;
    private String roomTypeName;
    private StayType stayType;
    private Integer noOfGuests;
    private Integer hours;
    private Integer price;
}
