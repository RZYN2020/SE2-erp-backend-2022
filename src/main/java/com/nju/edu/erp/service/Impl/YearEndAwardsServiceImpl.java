package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.EmployeeDao;
import com.nju.edu.erp.dao.SalaryGrantSheetDao;
import com.nju.edu.erp.dao.SalarySheetDao;
import com.nju.edu.erp.dao.YearEndAwardsDao;
import com.nju.edu.erp.model.po.EmployeePO;
import com.nju.edu.erp.model.po.SalaryGrantSheetPO;
import com.nju.edu.erp.model.po.YearEndAwardsPO;
import com.nju.edu.erp.model.vo.YearEndAwardsVO;
import com.nju.edu.erp.service.YearEndAwardsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class YearEndAwardsServiceImpl implements YearEndAwardsService {

    private final SalaryGrantSheetDao salaryGrantSheetDao;
    private final YearEndAwardsDao yearEndAwardsDao;
    private final EmployeeDao employeeDao;

    @Autowired
    public YearEndAwardsServiceImpl(SalaryGrantSheetDao salaryGrantSheetDao, YearEndAwardsDao yearEndAwardsDao, EmployeeDao employeeDao) {
        this.salaryGrantSheetDao = salaryGrantSheetDao;
        this.yearEndAwardsDao = yearEndAwardsDao;
        this.employeeDao = employeeDao;
    }

    @Override
    public List<YearEndAwardsVO> findAllYearEndSalary() {
        List<EmployeePO> employeePOList = employeeDao.findAll();
        for(EmployeePO po : employeePOList) {
            if (yearEndAwardsDao.findById(po.getId()) == null) {
                YearEndAwardsPO yearEndAwardsPO = YearEndAwardsPO.builder()
                        .employeeId(po.getId())
                        .employeeName(po.getName())
                        .yearEndAwards(new BigDecimal(0))
                        .build();
                yearEndAwardsDao.create(yearEndAwardsPO);
            }
        }
        List<YearEndAwardsPO> yearEndSalaryPOList = yearEndAwardsDao.findAll();
        List<YearEndAwardsVO> ans = new ArrayList<>();
        for (YearEndAwardsPO po : yearEndSalaryPOList) {
            YearEndAwardsVO vo = new YearEndAwardsVO();
            BeanUtils.copyProperties(po, vo);
            vo.setTotalSalaryExceptDecember(getTotalSalaryExceptDecember(po.getEmployeeId()));
        }
        return ans;
    }

    private BigDecimal getTotalSalaryExceptDecember(Integer employeeId) {
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
