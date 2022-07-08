package com.nju.edu.erp.service;

import com.nju.edu.erp.model.vo.BankAccountVO;

import java.util.List;

public interface BankAccountService {

    /**
     * 创建公司银行账户
     * @param inputVO
     * @return BankAccountVO
     */
    BankAccountVO createBankAccount(BankAccountVO inputVO);

    /**
     * 根据账户名删除公司银行账户
     * @param accountName
     * @return
     */
    void delete(String accountName);

    /**
     * 查询所有公司银行账户
     */
    List<BankAccountVO> findAll();
}
