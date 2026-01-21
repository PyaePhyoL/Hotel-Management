package org.bytesync.hotelmanagement.service.interfaces.finance;

import org.bytesync.hotelmanagement.dto.finance.BalanceSheetFilterDto;
import org.bytesync.hotelmanagement.dto.finance.BalanceSummarySheet;

import java.time.LocalDate;

public interface IBalanceService {

    BalanceSummarySheet getBalanceSummarySheet(LocalDate from, LocalDate to);
}
