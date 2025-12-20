package org.bytesync.hotelmanagement.service.impl;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.finance.MainDashboard;
import org.bytesync.hotelmanagement.enums.RoomStatus;
import org.bytesync.hotelmanagement.service.impl.finance.PaymentService;
import org.bytesync.hotelmanagement.service.impl.hotel.ReservationService;
import org.bytesync.hotelmanagement.service.impl.hotel.RoomService;
import org.bytesync.hotelmanagement.service.interfaces.IDashboardService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService implements IDashboardService {

    private final RoomService roomService;
    private final PaymentService paymentService;
    private final ReservationService reservationService;

    @Override
    public MainDashboard getDashboardData(RoomStatus status) {
        var dashboard = new MainDashboard();

        var rooms = roomService.getAllRoomsInGridView(status);
        var dailyIncome = paymentService.getDailyIncomeAmount();
        var checkIns = reservationService.getDailyCheckIns();

        dashboard.setRooms(rooms);
        dashboard.setNoOfCheckins(checkIns);
        dashboard.setDailyIncome(dailyIncome);
        return dashboard;
    }
}
