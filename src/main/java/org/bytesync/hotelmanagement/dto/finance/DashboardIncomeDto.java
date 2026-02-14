package org.bytesync.hotelmanagement.dto.finance;

import lombok.Builder;

@Builder
public record DashboardIncomeDto (
        Integer dailyIncome,

        Integer dayShiftIncome,
        Integer nightShiftIncome,

        Integer dayShiftKpay,
        Integer dayShiftCash,
        Integer nightShiftKpay,
        Integer nightShiftCash

) {

}
