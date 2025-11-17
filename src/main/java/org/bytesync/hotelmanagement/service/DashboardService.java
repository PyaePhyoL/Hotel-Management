package org.bytesync.hotelmanagement.service;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.finance.MainDashboard;
import org.bytesync.hotelmanagement.model.enums.RoomStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final RoomService roomService;
    private final FinanceService financeService;
    private final ReservationService reservationService;

    public MainDashboard getDashboardData(RoomStatus status) {
        var dashboard = new MainDashboard();

        var rooms = roomService.getAllRoomsInGridView(status);
        var dailyIncome = financeService.getDailyIncomeAmount();
        var checkIns = reservationService.getDailyCheckIns();

        dashboard.setRooms(rooms);
        dashboard.setNoOfCheckins(checkIns);
        dashboard.setDailyIncome(dailyIncome);
        return dashboard;
    }
}
