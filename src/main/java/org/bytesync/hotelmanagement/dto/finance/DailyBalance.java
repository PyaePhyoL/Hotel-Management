package org.bytesync.hotelmanagement.dto.finance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyBalance {
    private LocalDate date;
    private Integer totalIncomes;
    private Integer totalExpenses;
    private Integer totalRefunds;
    private Integer dailyProfit;

    private Integer totalServiceIncomes;
    private Integer totalFoodIncomes;
}
