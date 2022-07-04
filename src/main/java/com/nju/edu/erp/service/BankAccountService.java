package com.nju.edu.erp.service;

import com.nju.edu.erp.model.vo.BankAccountVO;

import java.util.List;

public interface BankAccountService {

    BankAccountVO createBankAccount(BankAccountVO inputVO);

    void delete(String accountName);

    List<BankAccountVO> findAll();
}
