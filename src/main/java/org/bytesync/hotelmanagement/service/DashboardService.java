package org.bytesync.hotelmanagement.service;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.finance.MainDashboard;
import org.bytesync.hotelmanagement.model.enums.RoomStatus;
import org.bytesync.hotelmanagement.service.finance.PaymentService;
import org.bytesync.hotelmanagement.service.hotel.ReservationService;
import org.bytesync.hotelmanagement.service.hotel.RoomService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final RoomService roomService;
    private final PaymentService paymentService;
    private final ReservationService reservationService;

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
