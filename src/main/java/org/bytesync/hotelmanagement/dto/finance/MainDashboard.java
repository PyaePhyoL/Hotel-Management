package org.bytesync.hotelmanagement.dto.finance;

import lombok.Data;
import org.bytesync.hotelmanagement.dto.room.RoomDashboardView;

@Data
public class MainDashboard {
    private Integer noOfCheckins;
    private Integer dailyIncome;

    private RoomDashboardView rooms;
}
