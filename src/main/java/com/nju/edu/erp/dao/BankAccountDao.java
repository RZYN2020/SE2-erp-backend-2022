package com.nju.edu.erp.dao;

import com.nju.edu.erp.model.po.BankAccountPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface BankAccountDao {

    void createBankAccount(BankAccountPO bankAccountPO);

    List<BankAccountPO> findAll();

    BankAccountPO findOneByAccountName(String accountName);

    void deleteOneByAccountName(String accountName);
}
