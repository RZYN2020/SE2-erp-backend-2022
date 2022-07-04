package com.nju.edu.erp.service;

import com.nju.edu.erp.model.vo.BankAccountVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
public class BankAccountTest {

    @Autowired
    BankAccountService bankAccountService;

    @Test
    @Transactional
    @Rollback
    public void AllTest() {
        BankAccountVO bankAccountVO1 = BankAccountVO.builder()
                .accountName("Horizon")
                .amount(7)
                .build();
        BankAccountVO bankAccountVO2 = BankAccountVO.builder()
                .accountName("Seer")
                .amount(10)
                .build();
        bankAccountService.createBankAccount(bankAccountVO1);
        bankAccountService.createBankAccount(bankAccountVO2);
        List<BankAccountVO> list = bankAccountService.findAll();
        bankAccountService.delete("Horizon");
        list = bankAccountService.findAll();
        System.out.println("Seer");
    }


}
