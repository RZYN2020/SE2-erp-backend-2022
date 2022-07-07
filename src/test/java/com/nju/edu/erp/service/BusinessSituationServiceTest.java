package com.nju.edu.erp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class BusinessSituationServiceTest {

    @Autowired
    BusinessSituationService businessSituationService;

    @Test
    @Transactional
    @Rollback
    public void Test1() {
        businessSituationService.getBusinessSituationByTime("2022-01-12 11:38:30", "2022-12-12 11:38:30");
    }
}
