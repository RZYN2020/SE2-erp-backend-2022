package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.SalaryGrantSheetDao;
import com.nju.edu.erp.dao.SalarySheetDao;
import com.nju.edu.erp.model.po.SalaryGrantSheetPO;
import com.nju.edu.erp.model.po.SalarySheetPO;
import com.nju.edu.erp.service.YearEndAwardsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class YearEndAwardsServiceImpl implements YearEndAwardsService {

    private final SalaryGrantSheetDao salaryGrantSheetDao;
    private final SalarySheetDao salarySheetDao;

    @Autowired
    public YearEndAwardsServiceImpl(SalaryGrantSheetDao salaryGrantSheetDao, SalarySheetDao salarySheetDao) {
        this.salaryGrantSheetDao = salaryGrantSheetDao;
        this.salarySheetDao = salarySheetDao;
    }

    @Override
    public BigDecimal getTotalSalaryExceptDecember(Integer employeeId) {
        BigDecimal sum = new BigDecimal(0);
        List<SalaryGrantSheetPO> list = salaryGrantSheetDao.getSheetByEmployeeId(employeeId);
        int thisYear = getYear(new Date());
        for (SalaryGrantSheetPO po : list) {
            Date date = po.getCreateTime();
            if (getYear(date) == thisYear && !isDec(date)) {
                sum = sum.add(po.getRealSalary());
            }
        }
        return sum;
    }

    @Override
    public void establishYearEndAwards(Integer employeeId, BigDecimal awards) {
        int thisYear = getYear(new Date());
        // 工资发放单
        List<SalaryGrantSheetPO> salaryGrantSheetPOList = salaryGrantSheetDao.getSheetByEmployeeId(employeeId);
        for (SalaryGrantSheetPO po : salaryGrantSheetPOList) {
            Date date = po.getCreateTime();
            if (getYear(date) == thisYear && isDec(date)) {
                salaryGrantSheetDao.addAwards(po.getId(), awards);
            }
        }
    }

    private int getYear(Date date) {
        String[] tmp = date.toString().split(" ");
        return Integer.parseInt(tmp[tmp.length - 1]);
    }

    private boolean isDec(Date date) {
        String[] tmp = date.toString().split(" ");
        return tmp[1].equals("Dec");
    }
}
