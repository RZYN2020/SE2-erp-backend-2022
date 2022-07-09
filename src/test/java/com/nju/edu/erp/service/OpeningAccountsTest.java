package com.nju.edu.erp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class OpeningAccountsTest {

    @Autowired
    OpeningAccountsService openingAccountsService;

    @Test
    @Transactional
    @Rollback
    public void Test1() {
        openingAccountsService.find("2022-07");
    }
}
