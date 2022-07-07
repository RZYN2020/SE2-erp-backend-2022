package com.nju.edu.erp.model.vo.businessSituation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BusinessOutcomeVO {
    // 支出信息：总支出，销售退货支出，进货支出，人力成本（工资）
    private BigDecimal outcome;
    private BigDecimal saleReturnOutcome;
    private BigDecimal purchaseOutcome;
    private BigDecimal salary;
}
