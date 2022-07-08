package com.nju.edu.erp.service;

import com.nju.edu.erp.model.vo.YearEndAwardsVO;

import java.math.BigDecimal;
import java.util.List;

public interface YearEndAwardsService {

    List<YearEndAwardsVO> findAllYearEndSalary();

    void establishYearEndAwards(Integer employeeId, BigDecimal awards);
}
