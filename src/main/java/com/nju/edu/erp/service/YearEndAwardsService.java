package com.nju.edu.erp.service;

import java.math.BigDecimal;

public interface YearEndAwardsService {
    BigDecimal getTotalSalaryExceptDecember(Integer employeeId);

    void establishYearEndAwards(Integer employeeId, BigDecimal awards);
}
