package com.nju.edu.erp.utils.salary;

import com.nju.edu.erp.dao.JobDao;
import com.nju.edu.erp.model.po.EmployeePO;
import com.nju.edu.erp.model.po.JobPO;
import com.nju.edu.erp.model.vo.TaxVO;
import com.nju.edu.erp.service.Impl.JobServiceImpl;
import java.math.BigDecimal;

public class CM1 implements CalculateMethod{
  private final String identifier = "基本工资 + 岗位工资 - 税款";

  private JobDao jobDao;
  private SignIn signIn;

  public CM1(JobDao jobDao, SignIn signIn) {
    this.jobDao = jobDao;
    this.signIn = signIn;
  }

  public BigDecimal doCalculate(EmployeePO employeePO) {
    JobPO jobPO = jobDao.findJob(employeePO.getName(), employeePO.getJobLevel());
    BigDecimal payable = jobPO.getBasicSalary().add(jobPO.getJobSalary());
    TaxVO taxVO = TaxMethod.calculateTax(payable);
    BigDecimal total_tax = taxVO.getIncome_tax().add(taxVO.getInsurance()).add(taxVO.getFund());
    BigDecimal actually_paid = payable.subtract(total_tax);
    BigDecimal absence = new BigDecimal(signIn.findAbsence(employeePO.getUsername()));
    actually_paid = actually_paid.multiply(absence).divide(new BigDecimal((30)));
    return actually_paid;
  }

  public String display() {
    return identifier;
  }
}
