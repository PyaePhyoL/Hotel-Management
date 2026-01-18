package org.bytesync.hotelmanagement.dto.room;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NormalStayPricingRulesDto {
    private Integer id;
    private String roomTypeId;
    private String roomTypeName;
    private Integer noOfGuests;
    private Integer price;
}
