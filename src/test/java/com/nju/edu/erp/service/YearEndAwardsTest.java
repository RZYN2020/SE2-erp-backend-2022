package com.nju.edu.erp.service;

import com.nju.edu.erp.model.vo.YearEndAwardsVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
public class YearEndAwardsTest {
    @Autowired
    YearEndAwardsService yearEndAwardsService;

    @Test
    @Transactional
    @Rollback
    public void Test1() {
        List<YearEndAwardsVO> list = yearEndAwardsService.findAllYearEndSalary();
        Assertions.assertEquals("DTA", list.get(0).getEmployeeName());
        Assertions.assertEquals(0, new BigDecimal(550).compareTo(list.get(0).getYearEndAwards()));
    }
}
