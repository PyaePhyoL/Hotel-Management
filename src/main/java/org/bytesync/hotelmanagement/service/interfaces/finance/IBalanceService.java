package org.bytesync.hotelmanagement.service.interfaces.finance;

import org.bytesync.hotelmanagement.dto.finance.MonthlyBalanceSheet;

public interface IBalanceService {

    MonthlyBalanceSheet getMonthlyBalance(int year, int month);
}
