package org.bytesync.hotelmanagement.dto.finance;

import lombok.Data;
import org.bytesync.hotelmanagement.dto.room.RoomDashboardView;

@Data
public class MainDashboard {
    private Integer noOfCheckins;
    private Integer dailyIncome;
    private Integer cashIncome;
    private Integer kpayIncome;

    private Integer morningShiftReservations;
    private Integer nightShiftReservations;

    private RoomDashboardView rooms;
}
