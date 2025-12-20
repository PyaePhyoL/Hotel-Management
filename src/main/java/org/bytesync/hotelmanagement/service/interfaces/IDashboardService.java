package org.bytesync.hotelmanagement.service.interfaces;

import org.bytesync.hotelmanagement.dto.finance.MainDashboard;
import org.bytesync.hotelmanagement.enums.RoomStatus;

public interface IDashboardService {

    MainDashboard getDashboardData(RoomStatus status);
}
