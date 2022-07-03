package com.nju.edu.erp.utils.sheet;

import com.nju.edu.erp.dao.EmployeeDao;
import com.nju.edu.erp.dao.JobDao;
import com.nju.edu.erp.dao.SalarySheetDao;
import com.nju.edu.erp.enums.sheetState.SalarySheetState;
import com.nju.edu.erp.enums.sheetState.SheetState;
import com.nju.edu.erp.model.po.JobPO;
import com.nju.edu.erp.model.po.PurchaseReturnsSheetPO;
import com.nju.edu.erp.model.po.SalarySheetPO;
import com.nju.edu.erp.model.po.SaleReturnSheetPO;
import com.nju.edu.erp.model.vo.Salary.SalarySheetVO;
import com.nju.edu.erp.model.vo.SaleReturns.SaleReturnSheetVO;
import com.nju.edu.erp.model.vo.SheetVO;
import com.nju.edu.erp.model.vo.TaxVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.utils.IdGenerator;
import com.nju.edu.erp.utils.salary.CalMethods;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeanUtils;

public class SalarySheet implements Sheet {

  private SalarySheetDao salarySheetDao;
  private EmployeeDao employeeDao;
  private JobDao jobDao;

  public SalarySheet(SalarySheetDao salarySheetDao, EmployeeDao employeeDao, JobDao jobDao) {
    this.salarySheetDao = salarySheetDao;
    this.employeeDao = employeeDao;
    this.jobDao = jobDao;
  }

  @Override
  public void makeSheet(UserVO userVO, SheetVO sheetVO) {
    assert sheetVO instanceof SalarySheetVO;
    SalarySheetVO salarySheetVO = (SalarySheetVO) sheetVO;
    SalarySheetPO salarySheetPO = new SalarySheetPO();
    BeanUtils.copyProperties(salarySheetVO, salarySheetPO);

    salarySheetPO.setOperator(userVO.getName());
    SalarySheetPO latest = salarySheetDao.getLatest();
    String id = IdGenerator.generateSheetId(latest == null ? null : latest.getId(), "GZD");
    salarySheetPO.setId(id);
    salarySheetPO.setState(SalarySheetState.PENDING_LEVEL_1);

    JobPO jobPO = jobDao.findJobByEmployee(salarySheetPO.getEmployee_id());
    salarySheetPO.setBasic_salary(jobPO.getBasicSalary());
    salarySheetPO.setJob_salary(jobPO.getJobSalary());
    salarySheetPO.setCommission(new BigDecimal(0)); //TODO:提成策略
    TaxVO taxVO = CalMethods.get(jobPO.getCalculateMethod()).calculate_tax(employeeDao.findOneById(salarySheetPO.getEmployee_id()));
    salarySheetPO.setFund(taxVO.getFund());
    salarySheetPO.setIncome_tax(taxVO.getIncome_tax());
    salarySheetPO.setInsurance(taxVO.getInsurance());

    salarySheetDao.saveSheet(salarySheetPO);
  }

  @Override
  public List<SheetVO> findSheetByState(SheetState state) {
    List<SheetVO> res = new ArrayList<>();
    List<SalarySheetPO> all;
    if(state == null) {
      all = salarySheetDao.findAll();
    } else {
      all = salarySheetDao.findAllByState((SalarySheetState) state);
    }

    for (SalarySheetPO po : all) {
      SalarySheetVO vo = new SalarySheetVO();
      BeanUtils.copyProperties(po, vo);
      TaxVO taxVO = new TaxVO();
      taxVO.setFund(po.getFund());
      taxVO.setIncome_tax(po.getIncome_tax());
      taxVO.setInsurance(po.getInsurance());
      res.add(vo);
    }

    return res;
  }

  @Override
  public void approval(String sheetId, SheetState state) {

  }
}
