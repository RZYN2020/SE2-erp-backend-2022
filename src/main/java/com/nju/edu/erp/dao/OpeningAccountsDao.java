package com.nju.edu.erp.dao;

import com.nju.edu.erp.model.po.OpeningAccounts.OpeningAccountsBankAccountPO;
import com.nju.edu.erp.model.po.OpeningAccounts.OpeningAccountsCustomerPO;
import com.nju.edu.erp.model.po.OpeningAccounts.OpeningAccountsProductPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface OpeningAccountsDao {

    List<OpeningAccountsBankAccountPO> findBankAccountPOsByTime(String date);

    List<OpeningAccountsProductPO> findProductPOsByTime(String date);

    List<OpeningAccountsCustomerPO> findCustomerPOsByTime(String date);

    void createBankAccount(OpeningAccountsBankAccountPO openingAccountsBankAccountPO);

    void createCustomer(OpeningAccountsCustomerPO openingAccountsCustomerPO);

    void createProduct(OpeningAccountsProductPO openingAccountsProductPO);
}
