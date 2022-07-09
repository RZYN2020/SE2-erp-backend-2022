package com.nju.edu.erp.service;

import com.nju.edu.erp.model.vo.businessSituation.BusinessIncomeVO;
import com.nju.edu.erp.model.vo.businessSituation.BusinessOutcomeVO;
import com.nju.edu.erp.model.vo.businessSituation.BusinessSituationVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@SpringBootTest
public class BusinessSituationServiceTest {

    @Autowired
    BusinessSituationService businessSituationService;

    @Test
    @Transactional
    @Rollback
    public void AllTest() {
        BusinessSituationVO vo = businessSituationService.getBusinessSituationByTime("2022-01-12 11:38:30", "2022-12-12 11:38:30");
        Assertions.assertEquals(0, new BigDecimal(5610400).compareTo(vo.getProfit()));
    }
}
