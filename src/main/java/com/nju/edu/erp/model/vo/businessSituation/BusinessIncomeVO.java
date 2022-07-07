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
public class BusinessIncomeVO {
    // 收入信息：折让后总收入，折扣（0.84 0.93这样），销售收入，进货退货收入
    private BigDecimal incomeAfterDiscount;
    private double discount;
    private BigDecimal saleIncome;
    private BigDecimal purchaseReturnsIncome;
}
