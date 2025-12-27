package org.bytesync.hotelmanagement.service.impl;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.finance.MainDashboard;
import org.bytesync.hotelmanagement.enums.RoomStatus;
import org.bytesync.hotelmanagement.service.interfaces.IDashboardService;
import org.bytesync.hotelmanagement.service.interfaces.finance.IPaymentService;
import org.bytesync.hotelmanagement.service.interfaces.hotel.IReservationService;
import org.bytesync.hotelmanagement.service.interfaces.hotel.IRoomService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService implements IDashboardService {

    private final IRoomService roomService;
    private final IPaymentService paymentService;
    private final IReservationService reservationService;

    @Override
    public MainDashboard getDashboardData(RoomStatus status) {
        var dashboard = new MainDashboard();

        var rooms = roomService.getAllRoomsInGridView(status);
        var dailyIncome = paymentService.getDailyIncomeAmount();
        var checkIns = reservationService.getActiveReservationCount();

        dashboard.setRooms(rooms);
        dashboard.setNoOfCheckins(checkIns);
        dashboard.setDailyIncome(dailyIncome);
        return dashboard;
    }
}
