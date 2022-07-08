package com.nju.edu.erp.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YearEndAwardsVO {
    private int employeeId;
    private String employeeName;
    private BigDecimal totalSalaryExceptDecember;
    private BigDecimal yearEndAwards;
}
