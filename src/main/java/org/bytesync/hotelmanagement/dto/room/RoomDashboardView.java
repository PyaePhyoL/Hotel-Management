package org.bytesync.hotelmanagement.dto.room;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RoomDashboardView {

    private Integer availableRooms;
    private Integer bookingRooms;
    private Integer longStayRooms;
    private Integer normalStayRooms;
    private Integer sectionRooms;
    private Integer serviceRooms;

    private List<RoomDto> fourth;
    private List<RoomDto> fifth;
    private List<RoomDto> seventh;
    private List<RoomDto> eighth;

}
