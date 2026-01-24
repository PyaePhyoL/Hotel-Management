package org.bytesync.hotelmanagement.dto.finance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BalanceSummarySheet {
    private String period;
    private Integer totalIncome;
    private Integer totalExpense;
    private Integer totalRefund;
    private Integer profit;

    private List<DailyBalance> dailyBalances;

}
