package com.nju.edu.erp.utils.sheet;

import com.nju.edu.erp.dao.EmployeeDao;
import com.nju.edu.erp.dao.JobDao;
import com.nju.edu.erp.dao.SalaryGrantSheetDao;
import com.nju.edu.erp.dao.SalarySheetDao;
import com.nju.edu.erp.dao.SaleSheetDao;
import com.nju.edu.erp.enums.sheetState.SalaryGrantSheetState;
import com.nju.edu.erp.enums.sheetState.SalarySheetState;
import com.nju.edu.erp.enums.sheetState.SaleReturnSheetState;
import com.nju.edu.erp.enums.sheetState.SaleSheetState;
import com.nju.edu.erp.enums.sheetState.SheetState;
import com.nju.edu.erp.model.po.*;
import com.nju.edu.erp.model.vo.Salary.SalarySheetVO;
import com.nju.edu.erp.model.vo.SaleReturns.SaleReturnSheetVO;
import com.nju.edu.erp.model.vo.SheetVO;
import com.nju.edu.erp.model.vo.TaxVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.utils.IdGenerator;
import com.nju.edu.erp.utils.ObjectUtils;
import com.nju.edu.erp.utils.salary.CalMethods;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.nju.edu.erp.utils.salary.CalculateMethod;
import org.springframework.beans.BeanUtils;

public class SalarySheet implements Sheet {

  private final SalarySheetDao salarySheetDao;
  private final EmployeeDao employeeDao;
  private final JobDao jobDao;
  private final SalaryGrantSheetDao salaryGrantSheetDao;
  private final SaleSheetDao saleSheetDao;
  private static final BigDecimal ratio = new BigDecimal(0.01);

  public SalarySheet(SalarySheetDao salarySheetDao, EmployeeDao employeeDao,
      JobDao jobDao, SalaryGrantSheetDao salaryGrantSheetDao
  , SaleSheetDao saleSheetDao) {
    this.salarySheetDao = salarySheetDao;
    this.employeeDao = employeeDao;
    this.jobDao = jobDao;
    this.salaryGrantSheetDao = salaryGrantSheetDao;
    this.saleSheetDao = saleSheetDao;
  }

  @Override
  public void makeSheet(UserVO userVO, SheetVO sheetVO) {
    assert sheetVO instanceof SalarySheetVO; //保证类型转换的正确性
    SalarySheetVO salarySheetVO = (SalarySheetVO) sheetVO;
    /************前置条件***************/
    assert salarySheetVO.getOperator() != null && salarySheetVO.getEmployee_id() != null &&
        salarySheetVO.getEmployee_name() != null;
    /*********保证外部资源可靠性************/

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
    //计算提成, 规则:该销售员在过去30天内参与的所有销售单金额 * ratio
    List<SaleSheetPO> saleSheetPOList = saleSheetDao.findAllByState(SaleSheetState.SUCCESS);
    BigDecimal totalSaleAmount = BigDecimal.ZERO;
    Date monthBefore = new Date(System.currentTimeMillis() - 30 * 24 * 60 * 60 * 1000);
    for (SaleSheetPO saleSheetPO : saleSheetPOList) {
      assert saleSheetPO.getCreate_time() != null; //已经审批完成，应该具有日期
      if (saleSheetPO.getCreate_time().after(monthBefore)) {
        totalSaleAmount = totalSaleAmount.add(saleSheetPO.getFinalAmount());
      }
    }
    salarySheetPO.setCommission(totalSaleAmount.multiply(ratio));
    TaxVO taxVO = CalMethods.get(jobPO.getCalculateMethod()).calculate_tax(employeeDao.findOneById(salarySheetPO.getEmployee_id()));
    salarySheetPO.setFund(taxVO.getFund());
    salarySheetPO.setIncome_tax(taxVO.getIncome_tax());
    salarySheetPO.setInsurance(taxVO.getInsurance());

    int effectLine = salarySheetDao.saveSheet(salarySheetPO);

    /************后置条件***************/
    assert effectLine > 0; //确保持久化成功
    assert salarySheetPO.getBasic_salary() != null && salarySheetPO.getJob_salary() != null
        && salarySheetPO.getFund() != null && salarySheetPO.getInsurance() != null && salarySheetPO.getIncome_tax() != null;
    /*********保证生效************/
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
      JobPO jobPO = jobDao.findJobByEmployee(po.getEmployee_id());
      SalarySheetVO vo = new SalarySheetVO();
      BeanUtils.copyProperties(po, vo);
      EmployeePO employeePO = employeeDao.findOneById(po.getEmployee_id());
      vo.setEmployee_name(employeePO.getName());
      TaxVO taxVO = new TaxVO();
      taxVO.setFund(po.getFund());
      taxVO.setIncome_tax(po.getIncome_tax());
      taxVO.setInsurance(po.getInsurance());
      vo.setTax(taxVO);
      vo.setActual_paid(CalMethods.get(jobPO.getCalculateMethod()).doCalculate(employeeDao.findOneById(po.getEmployee_id())));
      res.add(vo);

      /************后置条件***************/
      assert vo.getOperator() != null && vo.getBasic_salary() != null && vo.getActual_paid() != null && vo.getEmployee_name() != null && vo.getState() != null &&
          vo.getJob_salary() != null;
      /*********保证生效************/
    }

    return res;
  }

  @Override
  public void approval(String sheetId, SheetState state) {
    SalarySheetPO salarySheetPO = salarySheetDao.getSheetById(sheetId);
    SalarySheetState salarySheetState = (SalarySheetState) state;
    if (salarySheetState.equals(SalarySheetState.FAILURE)) {
      if(salarySheetPO.getState() == SalarySheetState.SUCCESS) throw new RuntimeException("状态更新失败");
      int effectLines = salarySheetDao.updateState(sheetId, salarySheetState);
      if(effectLines == 0) throw new RuntimeException("状态更新失败");
    } else {
      //建立正确的状态迁移
      SalarySheetState prevState;
      if (salarySheetState.equals(SalarySheetState.SUCCESS)) {
        prevState = SalarySheetState.PENDING_LEVEL_1;
      } else {
        throw new RuntimeException("状态更新失败");
      }
      int effectLines = salarySheetDao.updateStateV2(sheetId, salarySheetState, prevState);
      if (effectLines == 0) throw new RuntimeException("状态更新失败");

      //修改时间
      salarySheetPO.setCreate_time(new Date());
      effectLines = salarySheetDao.updateDate(salarySheetPO.getId(), new Date());
      assert effectLines > 0;

      //如果工资单审批成功，则生成工资发放单
      createSalaryGrantSheet(salarySheetPO);

    }
  }

  private void createSalaryGrantSheet(SalarySheetPO salarySheetPO) {
    SalaryGrantSheetPO salaryGrantSheetPO = new SalaryGrantSheetPO();

    int employeeId = salarySheetPO.getEmployee_id();
    EmployeePO employeePO = employeeDao.findOneById(employeeId);
    JobPO jobPO = jobDao.findJobByEmployee(employeeId);
    int calculateMethod = jobPO.getCalculateMethod();
    CalMethods.get(calculateMethod).calculate_payable(employeePO); // 税前
    CalMethods.get(calculateMethod).doCalculate(employeePO); // 税后

    // 单据编号

    SalaryGrantSheetPO latest = salaryGrantSheetDao.getLatest();
    String id = IdGenerator.generateSheetId(latest == null ? null : latest.getId(), "GZFFD");
    salaryGrantSheetPO.setId(id);

    //
    salaryGrantSheetPO.setEmployeeId(employeeId);
    salaryGrantSheetPO.setEmployeeName(employeePO.getName());
    salaryGrantSheetPO.setEmployeeAccount(employeePO.getAccount());
    salaryGrantSheetPO.setSalaryBeforeTax(CalMethods.get(calculateMethod).calculate_payable(employeePO));
    salaryGrantSheetPO.setIncomeTax(salarySheetPO.getIncome_tax());
    salaryGrantSheetPO.setInsurance(salarySheetPO.getInsurance());
    salaryGrantSheetPO.setFund(salarySheetPO.getFund());
    salaryGrantSheetPO.setRealSalary(CalMethods.get(calculateMethod).doCalculate(employeePO));
    salaryGrantSheetPO.setState(SalaryGrantSheetState.PENDING);

    salaryGrantSheetDao.saveSheet(salaryGrantSheetPO);
  }
}
