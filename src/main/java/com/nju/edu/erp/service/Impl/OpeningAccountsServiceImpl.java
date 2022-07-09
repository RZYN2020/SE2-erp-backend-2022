package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.BankAccountDao;
import com.nju.edu.erp.dao.CustomerDao;
import com.nju.edu.erp.dao.OpeningAccountsDao;
import com.nju.edu.erp.dao.ProductDao;
import com.nju.edu.erp.model.po.BankAccountPO;
import com.nju.edu.erp.model.po.CustomerPO;
import com.nju.edu.erp.model.po.OpeningAccounts.OpeningAccountsBankAccountPO;
import com.nju.edu.erp.model.po.OpeningAccounts.OpeningAccountsCustomerPO;
import com.nju.edu.erp.model.po.OpeningAccounts.OpeningAccountsProductPO;
import com.nju.edu.erp.model.po.ProductPO;
import com.nju.edu.erp.model.vo.OpeningAccounts.OpeningAccountsBankAccountVO;
import com.nju.edu.erp.model.vo.OpeningAccounts.OpeningAccountsCustomerVO;
import com.nju.edu.erp.model.vo.OpeningAccounts.OpeningAccountsProductVO;
import com.nju.edu.erp.model.vo.OpeningAccounts.OpeningAccountsVO;
import com.nju.edu.erp.service.OpeningAccountsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OpeningAccountsServiceImpl implements OpeningAccountsService {

    private final OpeningAccountsDao openingAccountsDao;
    private final BankAccountDao bankAccountDao;
    private final CustomerDao customerDao;
    private final ProductDao productDao;

    @Autowired
    public OpeningAccountsServiceImpl(OpeningAccountsDao openingAccountsDao, BankAccountDao bankAccountDao,
                                      CustomerDao customerDao, ProductDao productDao) {
        this.openingAccountsDao = openingAccountsDao;
        this.bankAccountDao = bankAccountDao;
        this.customerDao = customerDao;
        this.productDao = productDao;
    }

    @Override
    public void open() {
        String standard = getYearAndMonth();
        if (openingAccountsDao.findBankAccountPOsByTime(standard) == null) {
            List<BankAccountPO> poList = bankAccountDao.findAll();
            for (BankAccountPO po : poList) {
                OpeningAccountsBankAccountPO openingAccountsBankAccountPO = new OpeningAccountsBankAccountPO();
                BeanUtils.copyProperties(po, openingAccountsBankAccountPO);
                openingAccountsBankAccountPO.setDate(standard);
                openingAccountsDao.createBankAccount(openingAccountsBankAccountPO);
            }
        }
        if (openingAccountsDao.findCustomerPOsByTime(standard) == null) {
            List<CustomerPO> poList = customerDao.findAll();
            for (CustomerPO po : poList) {
                OpeningAccountsCustomerPO openingAccountsCustomerPO = new OpeningAccountsCustomerPO();
                BeanUtils.copyProperties(po, openingAccountsCustomerPO);
                openingAccountsCustomerPO.setDate(standard);
                openingAccountsDao.createCustomer(openingAccountsCustomerPO);
            }
        }
        if (openingAccountsDao.findProductPOsByTime(standard) == null) {
            List<ProductPO> poList = productDao.findAll();
            for (ProductPO po : poList) {
                OpeningAccountsProductPO openingAccountsProductPO = new OpeningAccountsProductPO();
                BeanUtils.copyProperties(po, openingAccountsProductPO);
                openingAccountsProductPO.setDate(standard);
                openingAccountsProductPO.setRecentPp(null);
                openingAccountsProductPO.setRecentRp(null);
                openingAccountsDao.createProduct(openingAccountsProductPO);
            }
        }
    }

    @Override
    public OpeningAccountsVO find(String time) {
        List<OpeningAccountsBankAccountPO> bankAccountPOList = openingAccountsDao.findBankAccountPOsByTime(time);
        List<OpeningAccountsBankAccountVO> bankAccountVOList = new ArrayList<>();
        for (OpeningAccountsBankAccountPO po : bankAccountPOList) {
            OpeningAccountsBankAccountVO vo = new OpeningAccountsBankAccountVO();
            BeanUtils.copyProperties(po, vo);
            bankAccountVOList.add(vo);
        }

        List<OpeningAccountsCustomerPO> customerPOList = openingAccountsDao.findCustomerPOsByTime(time);
        List<OpeningAccountsCustomerVO> customerVOList = new ArrayList<>();
        for (OpeningAccountsCustomerPO po : customerPOList) {
            OpeningAccountsCustomerVO vo = new OpeningAccountsCustomerVO();
            BeanUtils.copyProperties(po, vo);
            customerVOList.add(vo);
        }

        List<OpeningAccountsProductPO> productPOList = openingAccountsDao.findProductPOsByTime(time);
        List<OpeningAccountsProductVO> productVOList = new ArrayList<>();
        for (OpeningAccountsProductPO po : productPOList) {
            OpeningAccountsProductVO vo = new OpeningAccountsProductVO();
            BeanUtils.copyProperties(po, vo);
            productVOList.add(vo);
        }

        return OpeningAccountsVO.builder()
                .bankAccountVOList(bankAccountVOList)
                .customerVOList(customerVOList)
                .productVOList(productVOList)
                .build();
    }

    private String getYearAndMonth() {
        Date now = new Date();
        StringBuilder sb = new StringBuilder();
        sb.append(now.getYear() + 1900);
        sb.append('-');
        int month = now.getMonth() + 1;
        if (month < 10) sb.append('0');
        sb.append(month);
        return sb.toString();
    }
}
