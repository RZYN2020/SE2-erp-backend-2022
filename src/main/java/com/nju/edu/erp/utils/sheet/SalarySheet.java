package com.nju.edu.erp.utils.sheet;

import com.nju.edu.erp.dao.JobDao;
import com.nju.edu.erp.dao.SalarySheetDao;
import com.nju.edu.erp.enums.sheetState.SalarySheetState;
import com.nju.edu.erp.enums.sheetState.SheetState;
import com.nju.edu.erp.model.po.JobPO;
import com.nju.edu.erp.model.po.PurchaseReturnsSheetPO;
import com.nju.edu.erp.model.po.SalarySheetPO;
import com.nju.edu.erp.model.vo.Salary.SalarySheetVO;
import com.nju.edu.erp.model.vo.SheetVO;
import com.nju.edu.erp.model.vo.TaxVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.utils.IdGenerator;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.BeanUtils;

public class SalarySheet implements Sheet {

  private SalarySheetDao salarySheetDao;
  private JobDao jobDao;

  SalarySheet(SalarySheetDao salarySheetDao, JobDao jobDao) {
    this.salarySheetDao = salarySheetDao;
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
    TaxVO taxVO =

  }

  @Override
  public List<SheetVO> findSheetByState(SheetState state) {
    return null;
  }

  @Override
  public void approval(String sheetId, SheetState state) {

  }
}
