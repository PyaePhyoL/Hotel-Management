package org.bytesync.hotelmanagement.dto.room;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SessionStayPricingRulesDto {
    private Integer id;
    private String roomTypeId;
    private String roomTypeName;
    private Integer hours;
    private Integer price;
}
