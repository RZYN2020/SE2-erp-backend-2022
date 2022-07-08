package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.BankAccountDao;
import com.nju.edu.erp.exception.MyServiceException;
import com.nju.edu.erp.model.po.BankAccountPO;
import com.nju.edu.erp.model.vo.BankAccountVO;
import com.nju.edu.erp.service.BankAccountService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountDao bankAccountDao;

    @Autowired
    public BankAccountServiceImpl(BankAccountDao bankAccountDao) {
        this.bankAccountDao = bankAccountDao;
    }

    /**
     * 创建公司银行账户
     * @param inputVO
     * @return BankAccountVO
     */
    @Override
    public BankAccountVO createBankAccount(BankAccountVO inputVO) {
        BankAccountPO savePO = new BankAccountPO();
        BeanUtils.copyProperties(inputVO, savePO);
        bankAccountDao.createBankAccount(savePO);

        BankAccountPO responsePO = bankAccountDao.findOneByAccountName(inputVO.getAccountName());
        BankAccountVO ans = new BankAccountVO();
        BeanUtils.copyProperties(responsePO, ans);

        return ans;
    }

    /**
     * 根据账户名删除公司银行账户
     * @param accountName
     * @return
     */
    @Override
    public void delete(String accountName) {
        BankAccountPO bankAccountPOToDelete = bankAccountDao.findOneByAccountName(accountName);
        if (bankAccountPOToDelete == null) {
            throw new MyServiceException("F0000", "不存在该账户 删除失败！");
        }
        else {
            bankAccountDao.deleteOneByAccountName(accountName);
        }

    }

    /**
     * 查询所有公司银行账户
     */
    @Override
    public List<BankAccountVO> findAll() {
        List<BankAccountPO> bankAccountPOList = bankAccountDao.findAll();
        List<BankAccountVO> ans = new ArrayList<>();
        for (BankAccountPO bankAccountPO : bankAccountPOList) {
            BankAccountVO bankAccountVO = new BankAccountVO();
            BeanUtils.copyProperties(bankAccountPO, bankAccountVO);
            ans.add(bankAccountVO);
        }
        return ans;
    }
}
