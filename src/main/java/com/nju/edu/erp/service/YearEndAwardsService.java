package com.nju.edu.erp.service;

import com.nju.edu.erp.model.vo.YearEndAwardsVO;

import java.math.BigDecimal;
import java.util.List;

public interface YearEndAwardsService {

    /**
     * 获取全部员工年终奖信息
     * @return List<YearEndAwardsVO>
     */
    List<YearEndAwardsVO> findAllYearEndSalary();

    /**
     * 给指定员工发放指定金额年终奖
     * @param employeeId, awards
     */
    void establishYearEndAwards(Integer employeeId, BigDecimal awards);
}
