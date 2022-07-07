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
public class BusinessSituationVO {
    private BusinessIncomeVO businessIncomeVO;
    private BusinessOutcomeVO businessOutcomeVO;
    private BigDecimal profit; // 利润：折让后总收入-总支出
}
