package com.nju.edu.erp.service;

import com.nju.edu.erp.model.vo.BankAccountVO;
import org.junit.jupiter.api.Assertions;
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
        Assertions.assertEquals("Horizon", list.get(list.size() - 2).getAccountName());
        Assertions.assertEquals(7, list.get(list.size() - 2).getAmount());
        Assertions.assertEquals("Seer", list.get(list.size() - 1).getAccountName());
        Assertions.assertEquals(10, list.get(list.size() - 1).getAmount());
        bankAccountService.delete("Seer");
        list = bankAccountService.findAll();
        Assertions.assertEquals("Horizon", list.get(list.size() - 1).getAccountName());
        Assertions.assertEquals(7, list.get(list.size() - 1).getAmount());
        System.out.println("Horizon");
    }


}
